package com.freebills;

import com.freebills.usecases.InsertAdminUser;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableFeignClients
@EnableJpaAuditing
@EnableKafka
@SpringBootApplication
@RequiredArgsConstructor
public class FreebillsApplication {

    private final InsertAdminUser insert;

    public static void main(String[] args) {
        SpringApplication.run(FreebillsApplication.class, args);
    }

    @Bean
    InitializingBean sendDataBase() {
        return insert::insertAdminUser;
    }
}

