package apifestivos.apifestivos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



@SpringBootApplication
public class ApifestivosApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApifestivosApplication.class, args);
	}

		@Bean
	public CorsFilter corsFilter() {

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:4200"); // Permitir solo solicitudes desde http://localhost:3000
		config.addAllowedMethod("*"); // Permitir todos los métodos (GET, POST, PUT, DELETE, etc.)
		config.addAllowedHeader("*"); // Permitir todos los encabezados

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}

}
