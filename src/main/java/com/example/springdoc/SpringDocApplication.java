package com.example.springdoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Slf4j
@EnableWebSecurity
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "Customer API",
                description = """
                ##### Customer REST API comes with all basic operations for customers
                
                #### User credentials
                <b>Basic User:</b> user/password
                
                <b>Admin User:</b> admin/password
                """,
                version = "1.0",
                contact=@Contact(name="Prasanta Bhuyan",email = "prasanta.k.bhuyan@gmail.com")),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Server"),
                @Server(url = "http://dev.app.com", description = "Dev Server"),
                @Server(url = "http://qa.app.com", description = "QA Server")
        }
)
@SecurityScheme(name="CustomerSecurityScheme",
        description = "Please provide your credentials",
        scheme = "basic",
        type= SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class SpringDocApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringDocApplication.class, args);
    }

    @Value("${server.port}")
    private String port;


    @Bean
    CommandLineRunner getRunner() {
        return (args)->{
            log.info("http://localhost:"+port);
        };
    }
}
