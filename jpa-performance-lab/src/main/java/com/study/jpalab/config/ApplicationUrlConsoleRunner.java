package com.study.jpalab.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class ApplicationUrlConsoleRunner implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    public ApplicationUrlConsoleRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) {
        if (!(applicationContext instanceof WebServerApplicationContext webServerApplicationContext)) {
            return;
        }

        int port = webServerApplicationContext.getWebServer().getPort();

        System.out.println();
        System.out.println("=== 애플리케이션 접속 주소 ===");
        System.out.printf("게시판: http://localhost:%d/posts%n", port);
    }
}
