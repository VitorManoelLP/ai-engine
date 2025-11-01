package com.engine.ai.infra.config;

import com.engine.ai.infra.context.DataSourceContextHolder;
import com.engine.ai.infra.object.DataSourceType;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Order(1)
public class TransactionalDataSourceAspect {

    @Before("@annotation(transactional)")
    public void setDataSource(Transactional transactional) {
        if (transactional.readOnly()) {
            DataSourceContextHolder.setDataSourceType(DataSourceType.SLAVE);
        } else {
            DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
        }
    }

}
