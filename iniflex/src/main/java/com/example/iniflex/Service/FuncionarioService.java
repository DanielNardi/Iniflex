package com.Iniflex.Iniflex.Service;

import com.Iniflex.Iniflex.Model.Funcionario;
import com.Iniflex.Iniflex.Repository.FuncionarioRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    private final FuncionarioRepositorio funcionarioRepositorio;

    public FuncionarioService(FuncionarioRepositorio funcionarioRepositorio) {
        this.funcionarioRepositorio = funcionarioRepositorio;
    }

    // Salvar lista de funcionários que coloquei no "Principal"
    @Transactional
    public List<Funcionario> salvarLista(List<Funcionario> funcionarios) {
        return funcionarioRepositorio.saveAll(funcionarios);
    }

    @Transactional
    public void removerPorNome(String nome) {
        Funcionario funcionario = funcionarioRepositorio.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Funcionário '" + nome + "' não encontrado."));
        funcionarioRepositorio.delete(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepositorio.findAll();
    }

    //Aplicar aumento de 10%
    @Transactional
    public List<Funcionario> aplicarAumento() {
        List<Funcionario> todos = funcionarioRepositorio.findAll();
        todos.forEach(f -> {
            BigDecimal novoSalario = f.getSalario()
                    .multiply(new BigDecimal("1.10"))
                    .setScale(2, RoundingMode.HALF_UP);
            f.setSalario(novoSalario);
        });
        return funcionarioRepositorio.saveAll(todos);
    }

    public Map<String, List<Funcionario>> agruparPorFuncao() {
        return funcionarioRepositorio.buscarOrdenadosPorFuncao()
                .stream()
                .collect(Collectors.groupingBy(
                        Funcionario::getFuncao,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    public List<Funcionario> buscarAniversariantesMeses10e12() {
        return funcionarioRepositorio.buscarAniversariantesMeses10e12();
    }

    public Map<String, Object> buscarMaisVelho() {
        Funcionario f = funcionarioRepositorio.buscarMaisVelho()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nenhum funcionário cadastrado."));
        int idade = Period.between(f.getDataNascimento(), LocalDate.now()).getYears();
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("nome", f.getNome());
        resultado.put("idade", idade);
        return resultado;
    }

    public List<Funcionario> buscarOrdemAlfabetica() {
        return funcionarioRepositorio.buscarOrdemAlfabetica();
    }

    public Map<String, Object> buscarTotalSalarios() {
        BigDecimal total = funcionarioRepositorio.buscarTotalSalarios();
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("totalSalarios", total != null ? total : BigDecimal.ZERO);
        return resultado;
    }

    public List<Map<String, Object>> buscarSalariosMinimos() {
        return funcionarioRepositorio.buscarOrdemAlfabetica().stream()
                .map(f -> {
                    BigDecimal qtd = f.getSalario()
                            .divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("nome", f.getNome());
                    item.put("funcao", f.getFuncao());
                    item.put("salario", f.getSalario());
                    item.put("salariosMinimos", qtd);
                    return item;
                })
                .collect(Collectors.toList());
    }
}
