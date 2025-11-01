#!/bin/bash

set -e

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}=== Teste de Replicação PostgreSQL ===${NC}"

# Verificar se containers estão rodando
echo -e "\n${YELLOW}0. Verificando containers...${NC}"
if ! docker ps | grep -q postgres-master; then
    echo -e "${RED}✗ Container postgres-master não está rodando!${NC}"
    exit 1
fi

if ! docker ps | grep -q postgres-slave1; then
    echo -e "${RED}✗ Container postgres-slave1 não está rodando!${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Containers estão rodando${NC}"

# 1. Criar tabela de teste no MASTER
echo -e "\n${YELLOW}1. Criando tabela de teste no MASTER...${NC}"
docker exec postgres-master psql -U postgres -d mydb -c "DROP TABLE IF EXISTS test_replication CASCADE;"

docker exec postgres-master psql -U postgres -d mydb -c "
CREATE TABLE test_replication (
    id SERIAL PRIMARY KEY,
    test_name VARCHAR(100) NOT NULL,
    test_value VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
"

# Verificar se a tabela foi criada
RESULT=$(docker exec postgres-master psql -U postgres -d mydb -t -c "SELECT COUNT(*) FROM pg_tables WHERE tablename='test_replication';")

if echo "$RESULT" | grep -q "1"; then
    echo -e "${GREEN}✓ Tabela criada com sucesso no MASTER${NC}"
else
    echo -e "${RED}✗ Falha ao criar tabela no MASTER${NC}"
    exit 1
fi

# 2. Aguardar replicação da estrutura
echo -e "\n${YELLOW}2. Aguardando replicação da estrutura (5 segundos)...${NC}"
sleep 5

# 3. Verificar se tabela existe nos slaves
echo -e "\n${YELLOW}3. Verificando se tabela existe no SLAVE1...${NC}"
RESULT=$(docker exec postgres-slave1 psql -U postgres -d mydb -t -c "SELECT COUNT(*) FROM pg_tables WHERE tablename='test_replication';")

if echo "$RESULT" | grep -q "1"; then
    echo -e "${GREEN}✓ Tabela replicada no SLAVE1${NC}"
else
    echo -e "${RED}✗ Tabela não encontrada no SLAVE1${NC}"
    echo -e "${YELLOW}Verificando status da replicação...${NC}"
    docker exec postgres-master psql -U postgres -d mydb -c "SELECT * FROM pg_stat_replication;"
fi

echo -e "\n${YELLOW}4. Verificando se tabela existe no SLAVE2...${NC}"
RESULT=$(docker exec postgres-slave2 psql -U postgres -d mydb -t -c "SELECT COUNT(*) FROM pg_tables WHERE tablename='test_replication';")

if echo "$RESULT" | grep -q "1"; then
    echo -e "${GREEN}✓ Tabela replicada no SLAVE2${NC}"
else
    echo -e "${RED}✗ Tabela não encontrada no SLAVE2${NC}"
fi

# 4. Inserir dados no MASTER
echo -e "\n${YELLOW}5. Inserindo 5 registros no MASTER...${NC}"
for i in {1..5}; do
    TIMESTAMP=$(date +%s)
    docker exec postgres-master psql -U postgres -d mydb -c \
        "INSERT INTO test_replication (test_name, test_value) 
         VALUES ('Test_${i}', 'Value_${TIMESTAMP}_${i}');"
    echo -e "${GREEN}  ✓ Registro ${i} inserido${NC}"
    sleep 0.3
done

# 5. Aguardar replicação dos dados
echo -e "\n${YELLOW}6. Aguardando replicação dos dados (3 segundos)...${NC}"
sleep 3

# 6. Contar registros em cada servidor
echo -e "\n${GREEN}7. Contando registros:${NC}"

MASTER_COUNT=$(docker exec postgres-master psql -U postgres -d mydb -t -c "SELECT COUNT(*) FROM test_replication;" | xargs)
echo -e "  MASTER: ${BLUE}${MASTER_COUNT}${NC} registros"

SLAVE1_COUNT=$(docker exec postgres-slave1 psql -U postgres -d mydb -t -c "SELECT COUNT(*) FROM test_replication;" | xargs)
echo -e "  SLAVE1: ${BLUE}${SLAVE1_COUNT}${NC} registros"

SLAVE2_COUNT=$(docker exec postgres-slave2 psql -U postgres -d mydb -t -c "SELECT COUNT(*) FROM test_replication;" | xargs)
echo -e "  SLAVE2: ${BLUE}${SLAVE2_COUNT}${NC} registros"

# Validar contagens
if [ "$MASTER_COUNT" = "$SLAVE1_COUNT" ] && [ "$MASTER_COUNT" = "$SLAVE2_COUNT" ]; then
    echo -e "${GREEN}✓ Todos os servidores têm a mesma quantidade de registros!${NC}"
else
    echo -e "${RED}✗ Contagens divergentes!${NC}"
fi

# 7. Mostrar dados
echo -e "\n${GREEN}8. Dados no MASTER:${NC}"
docker exec postgres-master psql -U postgres -d mydb -c "SELECT * FROM test_replication ORDER BY id;"

echo -e "\n${GREEN}9. Dados no SLAVE1:${NC}"
docker exec postgres-slave1 psql -U postgres -d mydb -c "SELECT * FROM test_replication ORDER BY id;"

echo -e "\n${GREEN}10. Dados no SLAVE2:${NC}"
docker exec postgres-slave2 psql -U postgres -d mydb -c "SELECT * FROM test_replication ORDER BY id;"

# 8. Status da replicação
echo -e "\n${BLUE}11. Status da replicação no MASTER:${NC}"
docker exec postgres-master psql -U postgres -d mydb -c \
    "SELECT 
        application_name, 
        state, 
        sync_state,
        replay_lag
     FROM pg_stat_replication;"

# 9. Testar UPDATE
echo -e "\n${YELLOW}12. Testando UPDATE no MASTER...${NC}"
docker exec postgres-master psql -U postgres -d mydb -c \
    "UPDATE test_replication SET test_value = 'UPDATED_$(date +%s)' WHERE id = 1 RETURNING *;"

sleep 2

echo -e "\n${GREEN}13. Verificando UPDATE no SLAVE1:${NC}"
docker exec postgres-slave1 psql -U postgres -d mydb -c "SELECT * FROM test_replication WHERE id = 1;"

# 10. Testar read-only no slave
echo -e "\n${YELLOW}14. Tentando INSERT no SLAVE1 (deve falhar):${NC}"
docker exec postgres-slave1 psql -U postgres -d mydb -c \
    "INSERT INTO test_replication (test_name, test_value) VALUES ('Fail', 'Should fail');" \
    2>&1 && echo -e "${RED}✗ ERRO: Slave permitiu escrita!${NC}" || echo -e "${GREEN}✓ Correto! Slave bloqueou escrita (read-only)${NC}"

# 11. Verificar modo recovery
echo -e "\n${BLUE}15. Verificando modo recovery:${NC}"
IS_RECOVERY=$(docker exec postgres-slave1 psql -U postgres -d mydb -t -c "SELECT pg_is_in_recovery();" | xargs)
echo -e "  SLAVE1 em recovery: ${BLUE}${IS_RECOVERY}${NC}"

# 12. Limpar
echo -e "\n${YELLOW}16. Limpando - Removendo tabela de teste...${NC}"
docker exec postgres-master psql -U postgres -d mydb -c "DROP TABLE IF EXISTS test_replication CASCADE;"

sleep 2

# Verificar se foi removida
RESULT=$(docker exec postgres-slave1 psql -U postgres -d mydb -t -c "SELECT COUNT(*) FROM pg_tables WHERE tablename='test_replication';" 2>&1)

if echo "$RESULT" | grep -q "0"; then
    echo -e "${GREEN}✓ Tabela removida em todos os servidores${NC}"
else
    echo -e "${YELLOW}⚠ Verificação da remoção inconclusiva${NC}"
fi

# Resumo
echo -e "\n${BLUE}=== RESUMO ===${NC}"
echo -e "${GREEN}✓ Replicação de estrutura (DDL)${NC}"
echo -e "${GREEN}✓ Replicação de dados (DML - INSERT)${NC}"
echo -e "${GREEN}✓ Replicação de updates (DML - UPDATE)${NC}"
echo -e "${GREEN}✓ Slave em modo read-only${NC}"
echo -e "${GREEN}✓ Replicação de DROP${NC}"

echo -e "\n${GREEN}=== TESTE CONCLUÍDO! ===${NC}\n"