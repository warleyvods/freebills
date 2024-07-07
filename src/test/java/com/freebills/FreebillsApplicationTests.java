package com.freebills;

import com.freebills.utils.TestContainerBase;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.env.Environment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

@TestInstance(PER_CLASS)
class FreebillsApplicationTests extends TestContainerBase {

    private final Environment environment;

    FreebillsApplicationTests(Environment environment) {
        this.environment = environment;
    }

    @Test
    void contextLoads() {
        assertThat(environment.getActiveProfiles()).contains("test");
    }
}
