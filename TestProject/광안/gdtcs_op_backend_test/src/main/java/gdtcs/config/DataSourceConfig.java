package gdtcs.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.DisposableBean;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DataSourceConfig implements DisposableBean {

    private HikariDataSource hikariDataSource;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        this.hikariDataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        // 최대 커넥션 50
        hikariDataSource.setMaximumPoolSize(50);
        // 유휴 커넥션 50
        hikariDataSource.setMinimumIdle(50);
        // 커넥션풀 연결 타임아웃 30초
        hikariDataSource.setConnectionTimeout(30000);
        return this.hikariDataSource;
    }

    @Override
    public void destroy() {
        if (this.hikariDataSource != null) {
            log.info("##########[Closing HikariDataSource to prevent Out of Memory.]##########");
            log.info("##########[HikariCP State: Active Connection={}, Idle Connection={}]##########",
                    hikariDataSource.getHikariPoolMXBean().getActiveConnections(),
                    hikariDataSource.getHikariPoolMXBean().getIdleConnections()
            );
            this.hikariDataSource.close();
        }
    }
}