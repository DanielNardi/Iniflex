package com.Iniflex.Iniflex.Ultils;

import com.Iniflex.Iniflex.Model.Funcionario;
import com.Iniflex.Iniflex.Service.FuncionarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Component
public class Principal implements CommandLineRunner {

    private final FuncionarioService funcionarioService;

    public Principal(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @Override
    public void run(String... args) {
        if (!funcionarioService.listarTodos().isEmpty()) {
            System.out.println("Banco já populado. Pulando inserção inicial.");
            imprimirEndpoints();
            return;
        }

        List<Funcionario> listaInicial = Arrays.asList(
            new Funcionario("Maria",   LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"),
            new Funcionario("João",    LocalDate.of(1990,  5, 12), new BigDecimal("2284.38"), "Operador"),
            new Funcionario("Caio",    LocalDate.of(1961,  5,  2), new BigDecimal("9836.14"), "Coordenador"),
            new Funcionario("Miguel",  LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
            new Funcionario("Alice",   LocalDate.of(1995,  1,  5), new BigDecimal("2234.68"), "Recepcionista"),
            new Funcionario("Heitor",  LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"),
            new Funcionario("Arthur",  LocalDate.of(1993,  3, 31), new BigDecimal("4071.84"), "Contador"),
            new Funcionario("Laura",   LocalDate.of(1994,  7,  8), new BigDecimal("3017.45"), "Gerente"),
            new Funcionario("Heloísa", LocalDate.of(2003,  5, 24), new BigDecimal("1606.85"), "Eletricista"),
            new Funcionario("Helena",  LocalDate.of(1996,  9,  2), new BigDecimal("2799.93"), "Gerente")
        );
        funcionarioService.salvarLista(listaInicial);

        funcionarioService.removerPorNome("João");

        System.out.println("Dados carregados com sucesso!");
        imprimirEndpoints();
    }


}
