package com.juanantoniotora.appPruebaTecnica;

import com.juanantoniotora.appPruebaTecnica.service.AsynchronousServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
public class AsynchronousServiceTest {
    @InjectMocks
    private AsynchronousServiceImpl asynchronousServiceImpl;

    @Mock
    private Logger logger;

    @BeforeEach
    void SetUp() throws NoSuchFieldException, IllegalAccessException {
        final Field loggerField = AsynchronousServiceImpl.class.getDeclaredField(("logger"));
        loggerField.setAccessible(true);
        loggerField.set(this.asynchronousServiceImpl, this.logger);
    }

    /**
     * Here start test for the Services
     */
    @Test
    void asyncSimulateProcessOkTest() {
        this.asynchronousServiceImpl.asyncSimulateProcess();

        Mockito.verify(logger).info("INICIO [AsynchronousServiceServiceImpl.asyncSimulateProcess] - simulamos tareas asincronas.");
        Mockito.verify(logger).info("FIN [AsynchronousServiceServiceImpl.asyncSimulateProcess] - simulamos tareas asincronas.");
    }
}
