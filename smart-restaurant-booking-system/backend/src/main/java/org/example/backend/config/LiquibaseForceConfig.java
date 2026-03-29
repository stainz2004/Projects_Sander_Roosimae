package org.example.backend.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class LiquibaseForceConfig {

    @Value("${spring.liquibase.change-log}")
    private String changeLog;

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase l = new SpringLiquibase();
        l.setDataSource(dataSource);
        l.setChangeLog(changeLog);
        l.setShouldRun(true);
        return l;
    }
}