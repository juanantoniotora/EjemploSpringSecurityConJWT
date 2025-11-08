package com.juanantoniotora.appPruebaTecnica.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AsynchronousServiceImpl implements AsynchronousService {

    final Logger logger = LoggerFactory.getLogger(AsynchronousServiceImpl.class);

    @Override
    public void asyncSimulateProcess() {
        this.logger.info("INICIO [AsynchronousServiceServiceImpl.asyncSimulateProcess] - simulamos tareas asincronas.");

        // Aqui podrian hacerse tareas en background que no necesiten interacción con el usuario

        this.logger.info("FIN [AsynchronousServiceServiceImpl.asyncSimulateProcess] - simulamos tareas asincronas.");
    }
}
