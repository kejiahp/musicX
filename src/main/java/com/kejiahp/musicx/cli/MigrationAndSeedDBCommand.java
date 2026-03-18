package com.kejiahp.musicx.cli;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.kejiahp.musicx.seed.SeedDBService;

import java.util.Arrays;

@Component
public class MigrationAndSeedDBCommand implements CommandLineRunner {
    @Autowired
    private SeedDBService seedDBService;

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

        if (Arrays.asList(args).contains("seed")) {
            try {
                seedDBService.seedAll();
                System.out.println("Database seed sucessful. 🚀");
            } catch (Exception ex) {
                System.out.print("[SeedDBCommand]");
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                System.out.println("Database seed failed 😞");
            }
        }
    }
}