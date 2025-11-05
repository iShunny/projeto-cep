package com.example.api_cep.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api_cep.dto.EnderecoRequestDTO;
import com.example.api_cep.dto.EnderecoResponseDTO;
import com.example.api_cep.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST para gerenciamento de CEPs e endereços
 */
@RestController
@RequestMapping("/api/enderecos")
@RequiredArgsConstructor
@Tag(name = "Endereços", description = "API para consulta e gerenciamento de CEPs e endereços brasileiros")
public class EnderecoController {
    
    private final EnderecoService service;
    
    @GetMapping("/cep/{cep}")
    @Operation(
        summary = "Buscar endereço por CEP",
        description = "Retorna os dados completos de um endereço a partir do CEP informado (8 dígitos)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "CEP não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "CEP inválido", content = @Content)
    })
    public ResponseEntity<EnderecoResponseDTO> buscarPorCep(
            @Parameter(description = "CEP com 8 dígitos", example = "01310100")
            @PathVariable String cep) {
        
        EnderecoResponseDTO response = service.buscarPorCep(cep);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/logradouro")
    @Operation(
        summary = "Buscar endereços por logradouro",
        description = "Retorna uma lista paginada de endereços que contenham o logradouro informado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content)
    })
    public ResponseEntity<Page<EnderecoResponseDTO>> buscarPorLogradouro(
            @Parameter(description = "Nome do logradouro (rua, avenida, etc.)", example = "Paulista")
            @RequestParam String logradouro,
            
            @Parameter(description = "Número da página (inicia em 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Quantidade de itens por página")
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "Campo para ordenação")
            @RequestParam(defaultValue = "logradouro") String sort) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<EnderecoResponseDTO> response = service.buscarPorLogradouro(logradouro, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cidade")
    @Operation(
        summary = "Buscar endereços por cidade",
        description = "Retorna uma lista paginada de todos os endereços de uma cidade específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content)
    })
    public ResponseEntity<Page<EnderecoResponseDTO>> buscarPorCidade(
            @Parameter(description = "Nome da cidade", example = "São Paulo")
            @RequestParam String cidade,
            
            @Parameter(description = "Número da página (inicia em 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Quantidade de itens por página")
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "Campo para ordenação")
            @RequestParam(defaultValue = "logradouro") String sort) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<EnderecoResponseDTO> response = service.buscarPorCidade(cidade, pageable);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    @Operation(
        summary = "Criar novo endereço",
        description = "Cadastra um novo CEP e seus dados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "409", description = "CEP já cadastrado", content = @Content)
    })
    public ResponseEntity<EnderecoResponseDTO> criar(
            @Parameter(description = "Dados do endereço a ser criado")
            @Valid @RequestBody EnderecoRequestDTO request) {
        
        EnderecoResponseDTO response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{cep}")
    @Operation(
        summary = "Atualizar endereço existente",
        description = "Atualiza os dados de um endereço já cadastrado no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "CEP não encontrado", content = @Content),
        @ApiResponse(responseCode = "409", description = "Novo CEP já cadastrado", content = @Content)
    })
    public ResponseEntity<EnderecoResponseDTO> atualizar(
            @Parameter(description = "CEP do endereço a ser atualizado", example = "01310100")
            @PathVariable String cep,
            
            @Parameter(description = "Novos dados do endereço")
            @Valid @RequestBody EnderecoRequestDTO request) {
        
        EnderecoResponseDTO response = service.atualizar(cep, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{cep}")
    @Operation(
        summary = "Deletar endereço",
        description = "Remove um endereço do sistema pelo CEP"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso", content = @Content),
        @ApiResponse(responseCode = "404", description = "CEP não encontrado", content = @Content)
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "CEP do endereço a ser deletado", example = "01310100")
            @PathVariable String cep) {
        
        service.deletar(cep);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
        summary = "Listar todos os endereços",
        description = "Retorna uma lista paginada de todos os endereços cadastrados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content)
    })
    public ResponseEntity<Page<EnderecoResponseDTO>> listarTodos(
            @Parameter(description = "Número da página (inicia em 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Quantidade de itens por página")
            @RequestParam(defaultValue = "20") int size,
            
            @Parameter(description = "Campo para ordenação")
            @RequestParam(defaultValue = "logradouro") String sort) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<EnderecoResponseDTO> response = service.buscarTodos(pageable);
        return ResponseEntity.ok(response);
    }
}