package com.kejiahp.musicx.cli;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MigrationCommand implements CommandLineRunner {
    @Value("${spring.datasource.url}")
    private String dataSource;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${app.flyway.location}")
    private String flyWayLocation;

    @Override
    public void run(String... args) {
        // Create the Flyway instance and point it to the database
        Flyway flyway = Flyway.configure().dataSource(this.dataSource, this.username, this.password)
                .locations(this.flyWayLocation).validateOnMigrate(true).load();

        if (Arrays.asList(args).contains("migrate")) {
            flyway.migrate();
            System.out.println("Database migrations executed. 🚀");
        }
    }
}