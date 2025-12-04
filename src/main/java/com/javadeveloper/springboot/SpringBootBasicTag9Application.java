package com.javadeveloper.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Basic Tag 9
 * JAX-RS in Spring Boot - REST mit Java Standards
 * 
 * @author Java-Developer.online
 */
@SpringBootApplication
public class SpringBootBasicTag9Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootBasicTag9Application.class, args);
        
        System.out.println("\n" +
                "╔═══════════════════════════════════════════════════════════════╗\n" +
                "║   Spring Boot Basic Tag 9 - JAX-RS REST API gestartet!      ║\n" +
                "╚═══════════════════════════════════════════════════════════════╝\n" +
                "\n" +
                "📍 JAX-RS Endpoints:\n" +
                "   GET    http://localhost:8080/api/persons\n" +
                "   GET    http://localhost:8080/api/persons/{id}\n" +
                "   POST   http://localhost:8080/api/persons\n" +
                "   PUT    http://localhost:8080/api/persons/{id}\n" +
                "   DELETE http://localhost:8080/api/persons/{id}\n" +
                "\n" +
                "📍 Test-Seite:\n" +
                "   http://localhost:8080/index.html\n" +
                "\n" +
                "🔥 Happy Coding!\n");
    }
}
