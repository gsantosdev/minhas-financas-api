package com.gsantos.minhasfinancas.model.repository;

import com.gsantos.minhasfinancas.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
