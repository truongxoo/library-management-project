package study.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SprindocConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("LIBRARY API").version("0.0").description("Library Management system")
                    .contact(new Contact().name("PHAM VAN TRUONG").email("vantruong2904078@mail.com")));
    }
    

}
