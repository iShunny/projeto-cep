package com.example.api_cep.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.api_cep.dto.EnderecoRequestDTO;
import com.example.api_cep.dto.EnderecoResponseDTO;
import com.example.api_cep.dto.ViaCepResponseDTO;
import com.example.api_cep.entity.Endereco;
import com.example.api_cep.exception.CepJaExisteException;
import com.example.api_cep.exception.EnderecoNaoEncontradoException;
import com.example.api_cep.repository.EnderecoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Camada de serviço para gerenciamento de endereços
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EnderecoService {
    
    private final EnderecoRepository repository;
    private final WebClient webClient;
    
    /**
     * Busca um endereço pelo CEP, consultando primeiro o banco de dados local
     * e, se não encontrar, busca em uma API externa (ViaCEP).
     */
    @Transactional(readOnly = true)
    public EnderecoResponseDTO buscarPorCep(String cep) {
        log.info("Buscando endereço com CEP: {}", cep);
        
        return repository.findByCep(cep)
                .map(this::mapToResponseDTO)
                .orElseGet(() -> {
                    log.info("CEP não encontrado no banco de dados local. Buscando na API externa.");
                    Endereco endereco = buscarCepExternoESalvar(cep);
                    return mapToResponseDTO(endereco);
                });
    }

    /**
     * Busca o CEP em uma API externa (ViaCEP), salva no banco de dados local e retorna a entidade.
     */
    private Endereco buscarCepExternoESalvar(String cep) {
        log.info("Consultando ViaCEP para o CEP: {}", cep);

        ViaCepResponseDTO viaCepDto = webClient.get()
                .uri("/{cep}/json", cep)
                .retrieve()
                .bodyToMono(ViaCepResponseDTO.class)
                .onErrorResume(e -> {
                    log.error("Erro ao consultar ViaCEP para o CEP: {}", cep, e);
                    return Mono.empty();
                })
                .block();

        if (viaCepDto == null || viaCepDto.isErro()) {
            throw new EnderecoNaoEncontradoException("Endereço não encontrado para o CEP: " + cep);
        }

        Endereco endereco = mapViaCepToEntity(viaCepDto);
        
        // Salva o novo endereço no banco de dados
        return repository.save(endereco);
    }
    
    /**
     * Busca endereços por logradouro com paginação
     */
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> buscarPorLogradouro(String logradouro, Pageable pageable) {
        log.info("Buscando endereços com logradouro contendo: {}", logradouro);
        
        Page<Endereco> enderecos = repository.findByLogradouroContainingIgnoreCase(logradouro, pageable);
        return enderecos.map(this::mapToResponseDTO);
    }
    
    /**
     * Busca endereços por cidade com paginação
     */
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> buscarPorCidade(String cidade, Pageable pageable) {
        log.info("Buscando endereços da cidade: {}", cidade);
        
        Page<Endereco> enderecos = repository.findByCidadeIgnoreCase(cidade, pageable);
        return enderecos.map(this::mapToResponseDTO);
    }
    
    /**
     * Cria um novo endereço
     */
    @Transactional
    public EnderecoResponseDTO criar(EnderecoRequestDTO dto) {
        log.info("Criando novo endereço com CEP: {}", dto.getCep());
        
        // Valida se CEP já existe
        if (repository.existsByCep(dto.getCep())) {
            throw new CepJaExisteException("CEP já cadastrado no sistema: " + dto.getCep());
        }
        
        Endereco endereco = mapToEntity(dto);
        endereco = repository.save(endereco);
        
        log.info("Endereço criado com sucesso. ID: {}", endereco.getId());
        return mapToResponseDTO(endereco);
    }
    
    /**
     * Atualiza um endereço existente
     */
    @Transactional
    public EnderecoResponseDTO atualizar(String cep, EnderecoRequestDTO dto) {
        log.info("Atualizando endereço com CEP: {}", cep);
        
        Endereco endereco = repository.findByCep(cep)
                .orElseThrow(() -> new EnderecoNaoEncontradoException("Endereço não encontrado para o CEP: " + cep));
        
        // Se o CEP foi alterado, verifica se o novo CEP já existe
        if (!cep.equals(dto.getCep()) && repository.existsByCep(dto.getCep())) {
            throw new CepJaExisteException("O novo CEP já está cadastrado: " + dto.getCep());
        }
        
        // Atualiza os dados
        atualizarEntidade(endereco, dto);
        endereco = repository.save(endereco);
        
        log.info("Endereço atualizado com sucesso. ID: {}", endereco.getId());
        return mapToResponseDTO(endereco);
    }
    
    /**
     * Deleta um endereço por CEP
     */
    @Transactional
    public void deletar(String cep) {
        log.info("Deletando endereço com CEP: {}", cep);
        
        final Endereco endereco = repository.findByCep(cep)
                .orElseThrow(() -> new EnderecoNaoEncontradoException("Endereço não encontrado para o CEP: " + cep));
        
        repository.delete(endereco);
        log.info("Endereço deletado com sucesso. CEP: {}", cep);
    }
    
    /**
     * Busca todos os endereços com paginação
     */
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> buscarTodos(Pageable pageable) {
        log.info("Buscando todos os endereços.");
        Page<Endereco> enderecos = repository.findAll(pageable);
        return enderecos.map(this::mapToResponseDTO);
    }

    // Métodos auxiliares de mapeamento
    
    private EnderecoResponseDTO mapToResponseDTO(Endereco endereco) {
        return EnderecoResponseDTO.builder()
                .id(endereco.getId())
                .cep(endereco.getCep())
                .logradouro(endereco.getLogradouro())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .uf(endereco.getUf())
                .ibge(endereco.getIbge())
                .gia(endereco.getGia())
                .ddd(endereco.getDdd())
                .siafi(endereco.getSiafi())
                .createdAt(endereco.getCreatedAt())
                .updatedAt(endereco.getUpdatedAt())
                .build();
    }
    
    private Endereco mapToEntity(EnderecoRequestDTO dto) {
        Endereco endereco = new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
        endereco.setIbge(dto.getIbge());
        endereco.setGia(dto.getGia());
        endereco.setDdd(dto.getDdd());
        endereco.setSiafi(dto.getSiafi());
        return endereco;
    }

    private Endereco mapViaCepToEntity(ViaCepResponseDTO viaCepDto) {
        Endereco endereco = new Endereco();
        endereco.setCep(viaCepDto.getCep().replace("-", ""));
        endereco.setLogradouro(viaCepDto.getLogradouro());
        endereco.setComplemento(viaCepDto.getComplemento());
        endereco.setBairro(viaCepDto.getBairro());
        endereco.setCidade(viaCepDto.getLocalidade());
        endereco.setUf(viaCepDto.getUf());
        endereco.setIbge(viaCepDto.getIbge());
        endereco.setGia(viaCepDto.getGia());
        endereco.setDdd(viaCepDto.getDdd());
        endereco.setSiafi(viaCepDto.getSiafi());
        return endereco;
    }
    
    private void atualizarEntidade(Endereco endereco, EnderecoRequestDTO dto) {
        endereco.setCep(dto.getCep());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
        endereco.setIbge(dto.getIbge());
        endereco.setGia(dto.getGia());
        endereco.setDdd(dto.getDdd());
        endereco.setSiafi(dto.getSiafi());
    }
}