package com.javadeveloper.springboot.config;

import com.javadeveloper.springboot.exception.PersonNotFoundExceptionMapper;
import com.javadeveloper.springboot.resource.PersonResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import jakarta.ws.rs.ApplicationPath;

/**
 * Jersey Configuration
 * Registriert alle JAX-RS Resources und Exception Mapper
 * 
 * @ApplicationPath("/api") - Basis-Pfad für alle JAX-RS Endpoints
 */
@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // JAX-RS Resources registrieren
        register(PersonResource.class);
        
        // Exception Mapper registrieren
        register(PersonNotFoundExceptionMapper.class);
        
        System.out.println("✅ Jersey Config initialisiert");
        System.out.println("   - PersonResource registriert");
        System.out.println("   - PersonNotFoundExceptionMapper registriert");
    }
}
