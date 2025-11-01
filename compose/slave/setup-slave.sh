#!/bin/bash
set -e

echo "Aguardando o master ficar disponível..."
until PGPASSWORD=postgres psql -h postgres-master -U postgres -c '\q' 2>/dev/null; do
    echo "Master não está pronto ainda... aguardando"
    sleep 2
done

echo "Master está pronto! Iniciando configuração do slave..."

rm -rf /var/lib/postgresql/data/*

echo "Executando pg_basebackup..."
PGPASSWORD=replicator_password pg_basebackup \
    -h postgres-master \
    -D /var/lib/postgresql/data \
    -U replicator \
    -Fp \
    -Xs \
    -P \
    -R

echo "Backup base concluído!"

chown -R postgres:postgres /var/lib/postgresql/data
chmod 700 /var/lib/postgresql/data

cat > /var/lib/postgresql/data/postgresql.auto.conf << EOF
primary_conninfo = 'host=postgres-master port=5432 user=replicator password=replicator_password application_name=$(hostname)'
hot_standby = on
hot_standby_feedback = on
EOF

touch /var/lib/postgresql/data/standby.signal

echo "Configuração do slave concluída! Iniciando PostgreSQL..."

exec gosu postgres postgres