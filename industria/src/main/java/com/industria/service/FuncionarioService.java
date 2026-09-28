package com.industria.service;

import com.industria.dto.FuncionarioDTO;
import com.industria.model.Funcionario;
import com.industria.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final NumberFormat NUMBER_FORMAT;
    static {
        NUMBER_FORMAT = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        NUMBER_FORMAT.setMinimumFractionDigits(2);
        NUMBER_FORMAT.setMaximumFractionDigits(2);
    }

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }


    public void inicializar() {
        repository.deleteAll();
        repository.saveAll(Arrays.asList(
                new Funcionario("Maria",   LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"),  "Operador"),
                new Funcionario("João",    LocalDate.of(1990,  5, 12), new BigDecimal("2284.38"),  "Operador"),
                new Funcionario("Caio",    LocalDate.of(1961,  5,  2), new BigDecimal("9836.14"),  "Coordenador"),
                new Funcionario("Miguel",  LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
                new Funcionario("Alice",   LocalDate.of(1995,  1,  5), new BigDecimal("2234.68"),  "Recepcionista"),
                new Funcionario("Heitor",  LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"),  "Operador"),
                new Funcionario("Arthur",  LocalDate.of(1993,  3, 31), new BigDecimal("4071.84"),  "Contador"),
                new Funcionario("Laura",   LocalDate.of(1994,  7,  8), new BigDecimal("3017.45"),  "Gerente"),
                new Funcionario("Heloísa", LocalDate.of(2003,  5, 24), new BigDecimal("1606.85"),  "Eletricista"),
                new Funcionario("Helena",  LocalDate.of(1996,  9,  2), new BigDecimal("2799.93"),  "Gerente")
        ));
    }

    public void removerPorNome(String nome) {
        repository.deleteByNome(nome);
    }

    public List<FuncionarioDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void aplicarAumento(BigDecimal percentual) {
        BigDecimal fator = BigDecimal.ONE.add(
                percentual.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
        );
        List<Funcionario> funcionarios = repository.findAll();
        funcionarios.forEach(f ->
                f.setSalario(f.getSalario().multiply(fator).setScale(2, RoundingMode.HALF_UP))
        );
        repository.saveAll(funcionarios);
    }

    public Map<String, List<FuncionarioDTO>> agruparPorFuncao() {
        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Funcionario::getFuncao,
                        Collectors.mapping(this::toDTO, Collectors.toList())
                ));
    }

    public List<FuncionarioDTO> aniversariantesNosMeses(int... meses) {
        Set<Integer> mesesSet = new HashSet<>();
        for (int mes : meses) mesesSet.add(mes);
        return repository.findAll()
                .stream()
                .filter(f -> mesesSet.contains(f.getDataNascimento().getMonthValue()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Object> funcionarioMaisVelho() {
        Funcionario f = repository.findAll()
                .stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .orElseThrow(() -> new RuntimeException("Nenhum funcionário encontrado."));
        int idade = Period.between(f.getDataNascimento(), LocalDate.now()).getYears();
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("nome", f.getNome());
        resultado.put("idade", idade);
        return resultado;
    }

    public List<FuncionarioDTO> listarEmOrdemAlfabetica() {
        return repository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public String totalSalarios() {
        BigDecimal total = repository.findAll()
                .stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return NUMBER_FORMAT.format(total);
    }

    public Map<String, String> salariosEmSalariosMinimos() {
        Map<String, String> resultado = new LinkedHashMap<>();
        repository.findAll().forEach(f -> {
            BigDecimal qtd = f.getSalario()
                    .divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            resultado.put(f.getNome(), NUMBER_FORMAT.format(qtd));
        });
        return resultado;
    }

    private FuncionarioDTO toDTO(Funcionario f) {
        return new FuncionarioDTO(
                f.getNome(),
                f.getDataNascimento().format(DATE_FORMATTER),
                NUMBER_FORMAT.format(f.getSalario()),
                f.getFuncao()
        );
    }
}
