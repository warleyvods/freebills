package com.freebills.utils;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer {

    private static final PostgreSQLContainer<?> postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine");
        postgresContainer.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return postgresContainer;
    }
}