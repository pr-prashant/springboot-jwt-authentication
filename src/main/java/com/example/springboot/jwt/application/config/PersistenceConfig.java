package com.example.springboot.jwt.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Prashant Patel
 * Date: 7/22/2020
 **/
@Configuration
@EnableJpaRepositories(basePackages = "com.example.springboot.jwt.application.repository")
public class PersistenceConfig {
}
