package com.toddy.ws.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class WebConfigCorsRegistery  implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/*")
                .allowedOrigins("*")
                .allowedMethods("POST, PUT, GET, OPTIONS, DELETE")
                .allowedHeaders("*")
                .allowCredentials(false).maxAge(3600);

    }

    /*Este método permite inicializar swagger-ui.html da API REST*/

    @Override

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}