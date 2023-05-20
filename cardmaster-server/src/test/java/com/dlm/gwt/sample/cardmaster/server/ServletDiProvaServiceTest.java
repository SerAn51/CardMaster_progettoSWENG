package com.dlm.gwt.sample.cardmaster.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServletDiProvaServiceTest {

    ServletDiProvaServiceImpl service = new ServletDiProvaServiceImpl();

    @BeforeEach
    public void cleanEnvironment() {

    }

    @DisplayName("Test testing is working")
    @Test
    public void testGetPrices() {
        service.metodoDiTest();
    }
}
