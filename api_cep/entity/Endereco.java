package com.example.api_cep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidade que representa um endereço no banco de dados
 */
@Entity
@Table(name = "tb_enderecos", 
       indexes = {
           @Index(name = "idx_cep", columnList = "cep"),
           @Index(name = "idx_cidade", columnList = "cidade")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 8)
    private String cep;
    
    @Column(nullable = false, length = 255)
    private String logradouro;
    
    @Column(length = 100)
    private String complemento;
    
    @Column(nullable = false, length = 100)
    private String bairro;
    
    @Column(nullable = false, length = 100)
    private String cidade;
    
    @Column(nullable = false, length = 2)
    private String uf;
    
    @Column(length = 20)
    private String ibge;
    
    @Column(length = 20)
    private String gia;
    
    @Column(length = 3)
    private String ddd;
    
    @Column(length = 10)
    private String siafi;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}