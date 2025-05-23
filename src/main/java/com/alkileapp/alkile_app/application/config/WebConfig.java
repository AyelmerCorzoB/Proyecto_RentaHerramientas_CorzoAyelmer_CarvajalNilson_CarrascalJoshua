package com.alkileapp.alkile_app.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.alkileapp.alkile_app.infrastructure.interceptors.RequestLoggingInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Ruta donde se guardan las imágenes (debe coincidir con la del ToolController)
    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLoggingInterceptor())
                .addPathPatterns("/api/**"); // Solo interceptar rutas API
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configuración para servir archivos estáticos de uploads
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + UPLOAD_DIR)
                .setCachePeriod(3600)
                .resourceChain(true);
        
        // Opcional: Configuración para Swagger UI si lo estás usando
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}