package com.industria;

import com.industria.service.FuncionarioService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final FuncionarioService service;

    public DataInitializer(FuncionarioService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
        service.inicializar();
    }
}
