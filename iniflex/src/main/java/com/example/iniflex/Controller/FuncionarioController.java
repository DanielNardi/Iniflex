package com.Iniflex.Iniflex.Controller;

import com.Iniflex.Iniflex.Model.Funcionario;
import com.Iniflex.Iniflex.Service.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    // Recebe lista de funcionários e salva no banco
    @PostMapping
    public ResponseEntity<List<Funcionario>> inserirFuncionarios(
            @RequestBody List<Funcionario> funcionarios) {
        return ResponseEntity.ok(funcionarioService.salvarLista(funcionarios));
    }

    // Remove funcionário pelo nome
    @DeleteMapping("/{nome}")
    public ResponseEntity<String> removerFuncionario(@PathVariable String nome) {
        funcionarioService.removerPorNome(nome);
        return ResponseEntity.ok("Funcionário '" + nome + "' removido com sucesso.");
    }

    // Lista todos os funcionários
    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        return ResponseEntity.ok(funcionarioService.listarTodos());
    }

    // Aplica aumento de 10% em todos os salários
    @PatchMapping("/aumento")
    public ResponseEntity<List<Funcionario>> aplicarAumento() {
        return ResponseEntity.ok(funcionarioService.aplicarAumento());
    }

    // Retorna funcionários agrupados por função (Map<funcao, lista>)
    @GetMapping("/por-funcao")
    public ResponseEntity<Map<String, List<Funcionario>>> porFuncao() {
        return ResponseEntity.ok(funcionarioService.agruparPorFuncao());
    }

    // 
    // Funcionários que fazem aniversário nos meses de outubro e dezembro
    @GetMapping("/aniversariantes")
    public ResponseEntity<List<Funcionario>> aniversariantes() {
        return ResponseEntity.ok(funcionarioService.buscarAniversariantesMeses10e12());
    }

    // Funcionário com maior idade: { "nome": "...", "idade": 65 }
    @GetMapping("/mais-velho")
    public ResponseEntity<Map<String, Object>> maisVelho() {
        return ResponseEntity.ok(funcionarioService.buscarMaisVelho());
    }

    // Listar todos em ordem alfabética
    @GetMapping("/ordem-alfabetica")
    public ResponseEntity<List<Funcionario>> ordemAlfabetica() {
        return ResponseEntity.ok(funcionarioService.buscarOrdemAlfabetica());
    }

    // Total da folha: { "totalSalarios": $valor }
    @GetMapping("/total-salarios")
    public ResponseEntity<Map<String, Object>> totalSalarios() {
        return ResponseEntity.ok(funcionarioService.buscarTotalSalarios());
    }

    // Quantos salários mínimos cada funcionário ganha
    @GetMapping("/salarios-minimos")
    public ResponseEntity<List<Map<String, Object>>> salariosMinimos() {
        return ResponseEntity.ok(funcionarioService.buscarSalariosMinimos());
    }
}
