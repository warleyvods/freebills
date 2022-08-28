package com.freebills;

import com.freebills.usecases.InsertAdminUser;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
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

    /* volta para o legacy tomcat (libera o ponto antes do domain em setCookie) ex: ".domain.com"*/
    @Bean
    WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return tomcatServletWebServerFactory -> tomcatServletWebServerFactory.addContextCustomizers(context -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }
}
