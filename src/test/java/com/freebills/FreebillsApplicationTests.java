package com.freebills;

import org.springframework.core.env.Environment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FreebillsApplicationTests {

    private final Environment environment;

    FreebillsApplicationTests(Environment environment) {
        this.environment = environment;
    }

    @Test
    void contextLoads() {
        assertThat(environment.getActiveProfiles()).contains("test");
    }
}
