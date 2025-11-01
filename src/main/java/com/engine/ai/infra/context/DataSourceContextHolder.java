package com.engine.ai.infra.context;

import com.engine.ai.infra.object.DataSourceType;

public class DataSourceContextHolder {

    private static final ThreadLocal<DataSourceType> contextHolder =
            new ThreadLocal<>();

    public static void setDataSourceType(DataSourceType type) {
        contextHolder.set(type);
    }

    public static DataSourceType getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }

}
