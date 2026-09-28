package com.industria.controller;

import com.industria.dto.FuncionarioDTO;
import com.industria.service.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @DeleteMapping("/nome/{nome}")
    public ResponseEntity<String> removerPorNome(@PathVariable String nome) {
        service.removerPorNome(nome);
        return ResponseEntity.ok("Funcionário '" + nome + "' removido com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/aumento")
    public ResponseEntity<String> aplicarAumento(
            @RequestParam(defaultValue = "10") BigDecimal percentual) {
        service.aplicarAumento(percentual);
        return ResponseEntity.ok("Aumento de " + percentual + "% aplicado com sucesso!");
    }

    @GetMapping("/por-funcao")
    public ResponseEntity<Map<String, List<FuncionarioDTO>>> listarPorFuncao() {
        return ResponseEntity.ok(service.agruparPorFuncao());
    }

    @GetMapping("/aniversariantes")
    public ResponseEntity<List<FuncionarioDTO>> aniversariantes() {
        return ResponseEntity.ok(service.aniversariantesNosMeses(10, 12));
    }

    @GetMapping("/maior-idade")
    public ResponseEntity<Map<String, Object>> funcionarioMaisVelho() {
        return ResponseEntity.ok(service.funcionarioMaisVelho());
    }

    @GetMapping("/ordem-alfabetica")
    public ResponseEntity<List<FuncionarioDTO>> listarEmOrdemAlfabetica() {
        return ResponseEntity.ok(service.listarEmOrdemAlfabetica());
    }

    @GetMapping("/total-salarios")
    public ResponseEntity<Map<String, String>> totalSalarios() {
        return ResponseEntity.ok(Map.of("total", service.totalSalarios()));
    }

    // não sabia se era para ser inteiro ou não, ex: 2.66R$
    @GetMapping("/salarios-minimos")
    public ResponseEntity<Map<String, String>> salariosEmSalariosMinimos() {
        return ResponseEntity.ok(service.salariosEmSalariosMinimos());
    }
}
