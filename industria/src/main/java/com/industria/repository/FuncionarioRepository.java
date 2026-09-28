package com.industria.repository;

import com.industria.model.Funcionario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioRepository extends MongoRepository<Funcionario, String> {

    void deleteByNome(String nome);

    List<Funcionario> findAllByOrderByNomeAsc();
}
