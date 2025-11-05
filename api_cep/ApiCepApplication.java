package com.example.api_cep;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Classe principal da aplicação API de CEP
 * 
 * Esta é a classe de entrada da aplicação Spring Boot.
 * Ela inicializa todo o contexto Spring e inicia o servidor web.
 * 
 * @SpringBootApplication combina:
 *   - @Configuration: Marca como classe de configuração
 *   - @EnableAutoConfiguration: Ativa configuração automática
 *   - @ComponentScan: Escaneia componentes no pacote e subpacotes
 * 
 * @author API CEP Team
 * @version 1.0.0
 * @since 2024-11-05
 */
@SpringBootApplication
public class ApiCepApplication {

    /**
     * Método principal que inicia a aplicação Spring Boot
     * 
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiCepApplication.class, args);
    }

    /**
     * Bean executado após a aplicação iniciar completamente
     * Exibe informações úteis sobre a API no console
     * 
     * @return CommandLineRunner que será executado após startup
     */
    @Bean
    public CommandLineRunner exibirInformacoesInicializacao() {
        return args -> {
            String separador = "=".repeat(70);
            System.out.println("\n" + separador);
            System.out.println("🚀 API DE CONSULTA DE CEP - INICIADA COM SUCESSO!");
            System.out.println(separador);
            System.out.println();
            System.out.println("📖 Documentação Swagger:");
            System.out.println("   → http://localhost:8080/swagger-ui.html");
            System.out.println();
            System.out.println("🗄️  Console do Banco H2:");
            System.out.println("   → http://localhost:8080/h2-console");
            System.out.println("   → JDBC URL: jdbc:h2:mem:cepdb");
            System.out.println("   → User: sa");
            System.out.println("   → Password: (deixe em branco)");
            System.out.println();
            System.out.println("📡 API Endpoints:");
            System.out.println("   → http://localhost:8080/api/enderecos");
            System.out.println();
            System.out.println("📄 OpenAPI JSON:");
            System.out.println("   → http://localhost:8080/v3/api-docs");
            System.out.println();
            System.out.println(separador);
            System.out.println("💡 Dica: Use o Swagger para testar os endpoints!");
            System.out.println(separador + "\n");
        };
    }
}
