package com.study.jpalab.config;

import com.study.jpalab.post.PostConsoleRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUrlConsoleRunnerTest {

    @Mock
    private WebServerApplicationContext applicationContext;

    @Mock
    private WebServer webServer;

    @Mock
    private ApplicationContext nonWebApplicationContext;

    @Test
    void printsBoardUrlUsingActualWebServerPort() {
        when(applicationContext.getWebServer()).thenReturn(webServer);
        when(webServer.getPort()).thenReturn(54321);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream testOut = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(testOut);
            new ApplicationUrlConsoleRunner(applicationContext).run();
        } finally {
            System.setOut(originalOut);
        }

        String lineSeparator = System.lineSeparator();
        assertThat(output.toString(StandardCharsets.UTF_8)).isEqualTo(
                lineSeparator
                        + "=== 애플리케이션 접속 주소 ===" + lineSeparator
                        + "게시판: http://localhost:54321/posts" + lineSeparator
        );
    }

    @Test
    void runsAfterPostConsoleRunner() {
        Order postOrder = PostConsoleRunner.class.getAnnotation(Order.class);
        Order urlOrder = ApplicationUrlConsoleRunner.class.getAnnotation(Order.class);

        assertThat(postOrder).isNotNull();
        assertThat(urlOrder).isNotNull();
        assertThat(postOrder.value()).isLessThan(urlOrder.value());
    }

    @Test
    void skipsOutputWhenWebServerIsNotRunning() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream testOut = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(testOut);
            new ApplicationUrlConsoleRunner(nonWebApplicationContext).run();
        } finally {
            System.setOut(originalOut);
        }

        assertThat(output.toString(StandardCharsets.UTF_8)).isEmpty();
    }
}
