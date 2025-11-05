package com.example.api_cep.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.api_cep.entity.Endereco;

/**
 * Repository para operações no banco de dados de Endereço
 * Todas as queries SQL estão explícitas para visualização
 */
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    
    /**
     * Busca um endereço específico pelo CEP
     * Query SQL: SELECT * FROM tb_enderecos WHERE cep = ?
     */
    @Query("SELECT e FROM Endereco e WHERE e.cep = :cep")
    Optional<Endereco> findByCep(@Param("cep") String cep);
    
    /**
     * Busca endereços que contenham o logradouro informado (busca parcial)
     * Query SQL: SELECT * FROM tb_enderecos WHERE LOWER(logradouro) LIKE LOWER(CONCAT('%', ?, '%'))
     */
    @Query("SELECT e FROM Endereco e WHERE LOWER(e.logradouro) LIKE LOWER(CONCAT('%', :logradouro, '%'))")
    Page<Endereco> findByLogradouroContainingIgnoreCase(@Param("logradouro") String logradouro, Pageable pageable);
    
    /**
     * Busca todos os endereços de uma cidade específica
     * Query SQL: SELECT * FROM tb_enderecos WHERE LOWER(cidade) = LOWER(?)
     */
    @Query("SELECT e FROM Endereco e WHERE LOWER(e.cidade) = LOWER(:cidade)")
    Page<Endereco> findByCidadeIgnoreCase(@Param("cidade") String cidade, Pageable pageable);
    
    /**
     * Busca endereços por UF (estado)
     * Query SQL: SELECT * FROM tb_enderecos WHERE UPPER(uf) = UPPER(?)
     */
    @Query("SELECT e FROM Endereco e WHERE UPPER(e.uf) = UPPER(:uf)")
    Page<Endereco> findByUf(@Param("uf") String uf, Pageable pageable);
    
    /**
     * Busca endereços por bairro e cidade
     * Query SQL: SELECT * FROM tb_enderecos WHERE LOWER(bairro) = LOWER(?) AND LOWER(cidade) = LOWER(?)
     */
    @Query("SELECT e FROM Endereco e WHERE LOWER(e.bairro) = LOWER(:bairro) AND LOWER(e.cidade) = LOWER(:cidade)")
    Page<Endereco> findByBairroAndCidade(@Param("bairro") String bairro, @Param("cidade") String cidade, Pageable pageable);
    
    /**
     * Verifica se um CEP já existe no banco
     * Query SQL: SELECT COUNT(*) > 0 FROM tb_enderecos WHERE cep = ?
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Endereco e WHERE e.cep = :cep")
    boolean existsByCep(@Param("cep") String cep);
    
    /**
     * Verifica se um CEP existe excluindo um ID específico (útil para atualizações)
     * Query SQL: SELECT COUNT(*) > 0 FROM tb_enderecos WHERE cep = ? AND id != ?
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Endereco e WHERE e.cep = :cep AND e.id != :id")
    boolean existsByCepAndIdNot(@Param("cep") String cep, @Param("id") Long id);
    
    /**
     * Conta total de endereços por cidade
     * Query SQL: SELECT COUNT(*) FROM tb_enderecos WHERE LOWER(cidade) = LOWER(?)
     */
    @Query("SELECT COUNT(e) FROM Endereco e WHERE LOWER(e.cidade) = LOWER(:cidade)")
    long countByCidade(@Param("cidade") String cidade);
}