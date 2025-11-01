package com.engine.ai.infra.config;

import com.engine.ai.infra.annotation.ReadOnly;
import com.engine.ai.infra.annotation.WriteOnly;
import com.engine.ai.infra.context.DataSourceContextHolder;
import com.engine.ai.infra.object.DataSourceType;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class DataSourceAspect {

    @Before("@annotation(readOnly)")
    public void setReadDataSource(ReadOnly readOnly) {
        DataSourceContextHolder.setDataSourceType(DataSourceType.SLAVE);
    }

    @Before("@annotation(writeOnly)")
    public void setWriteDataSource(WriteOnly writeOnly) {
        DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
    }

    @After("@annotation(ReadOnly) || @annotation(WriteOnly)")
    public void clearDataSource() {
        DataSourceContextHolder.clearDataSourceType();
    }

}
