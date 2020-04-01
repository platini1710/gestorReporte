package cl.fonasa.main;

import java.util.concurrent.TimeUnit;

import javax.servlet.annotation.WebListener;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ch.qos.logback.classic.Logger;

@SpringBootApplication(scanBasePackages = {"cl.fonasa"})
public class SpringBootGestorReporte  implements WebMvcConfigurer  {
	
	@Autowired
	RestTemplate restTemplate;

	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {

	        // Register resource handler for images
	        registry.addResourceHandler("/src/main/resources/imagen/**").addResourceLocations("SpringBootGestorReporte")
	                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
	    }
	    @Bean
	    public RestTemplate getRestTemplate() {
	        return new RestTemplate();
	    }
	public static void main(String[] args) {
		SpringApplication.run(SpringBootGestorReporte.class, args);
	}
	



}
