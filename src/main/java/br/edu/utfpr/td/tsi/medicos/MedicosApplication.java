package br.edu.utfpr.td.tsi.medicos;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Gerenciado de Médicos", version = "1.0.0"))
public class MedicosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicosApplication.class, args);
    }
}
