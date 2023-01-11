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
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableFeignClients
@EnableJpaAuditing
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

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addContextCustomizers(context -> {
            Rfc6265CookieProcessor processor = new Rfc6265CookieProcessor();
            processor.setSameSiteCookies("strict");
            context.setCookieProcessor(processor);
        });
        return factory;
    }
}
