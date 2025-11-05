package com.example.api_cep.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnderecoRequestDTO {
    
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^\\d{8}$", message = "CEP deve conter exatamente 8 dígitos numéricos")
    private String cep;
    
    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 255, message = "Logradouro deve ter no máximo 255 caracteres")
    private String logradouro;
    
    @Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
    private String complemento;
    
    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    private String bairro;
    
    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String cidade;
    
    @NotBlank(message = "UF é obrigatório")
    @Pattern(regexp = "^[A-Z]{2}$", message = "UF deve conter exatamente 2 letras maiúsculas")
    private String uf;
    
    @Pattern(regexp = "^\\d{0,20}$", message = "IBGE deve conter apenas dígitos")
    private String ibge;
    
    @Pattern(regexp = "^\\d{0,20}$", message = "GIA deve conter apenas dígitos")
    private String gia;
    
    @Pattern(regexp = "^\\d{0,3}$", message = "DDD deve conter até 3 dígitos")
    private String ddd;
    
    @Pattern(regexp = "^\\d{0,10}$", message = "SIAFI deve conter até 10 dígitos")
    private String siafi;
}