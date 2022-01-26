package com.example.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@PropertySource(value = {"classpath:db.properties", "file:/data/properties/db.properies"}, ignoreResourceNotFound = true)
public class BoardApplication {
    public static void main(String[] args) {
        SpringApplication.run(BoardApplication.class, args);

    }
}
