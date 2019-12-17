package com.qthegamep.pattern.project2.util;

public enum Constants {

    DEFAULT_CONFIG_PROPERTIES_PATH("/config.properties"),
    MONGO_UTC_DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    GMT_TIMEZONE("GMT"),
    JSON_DATE_FIELD_NAME("$date"),
    START_TIME_HEADER("startTime"),
    DURATION_HEADER("duration"),
    X_REQUEST_ID_HEADER("x-request-id"),
    REQUEST_ID_HEADER("requestId"),
    X_FORWARDED_FOR_HEADER("X-Forwarded-For"),
    UK_LANGUAGE("uk"),
    RU_LANGUAGE("ru"),
    DEFAULT_LANGUAGE("uk"),
    ERROR_MESSAGES_LOCALIZATION("localization.error_messages"),
    GRIZZLY("grizzly"),
    APPLICATION_GRIZZLY_POOL_NAME("application-grizzly-worker-thread-"),
    METRICS_GRIZZLY_POOL_NAME("metrics-grizzly-worker-thread-"),
    HTTP("http://"),
    JSON_OBJECT_ID_FIELD_NAME("$oid"),
    STANDALONE_MONGO_DB_TYPE("standalone"),
    CLUSTER_MONGO_DB_TYPE("cluster"),
    JSON_OBJECT_ID_KEY("_id"),
    POOL_REDIS_TYPE("pool"),
    CLUSTER_REDIS_TYPE("cluster");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
