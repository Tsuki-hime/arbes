package com.phonecompany.billing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BillingApplicationTests {

    private TelephoneBillCalculatorImpl calculator;

    @BeforeEach
    void setUp() {
        calculator = new TelephoneBillCalculatorImpl();
    }

    @Test
    void shortCallsIn8To4() throws Exception {
        String filepath2 = "otherTestFile.csv";

        BigDecimal expected = new BigDecimal("3.0");
        BigDecimal actual = calculator.calculate(filepath2);

        assertEquals(expected, actual);
    }

    @Test
    void shortCallsOutside8To4() throws Exception {
        String filepath = "shortOut.csv";

        BigDecimal expected = new BigDecimal("1.5");
        BigDecimal actual = calculator.calculate(filepath);

        assertEquals(expected, actual);
    }

    @Test
    void longCallsIn8To4() throws Exception {
        String filepath2 = "otherTestFileLong.csv";

        BigDecimal expected = new BigDecimal("5.2");
        BigDecimal actual = calculator.calculate(filepath2);

        assertEquals(expected, actual);
    }

    @Test
    void longCallsOutside8To4() throws Exception {
        String filepath2 = "longOut.csv";

        BigDecimal expected = new BigDecimal("2.7");
        BigDecimal actual = calculator.calculate(filepath2);

        assertEquals(expected, actual);
    }

    @Test
    void longCallCrossing5MinInt() throws Exception {
        String filepath = "crossIntervalCalls.csv";

        BigDecimal expected = new BigDecimal("5.2");
        BigDecimal actual = calculator.calculate(filepath);

        assertEquals(expected, actual);
    }


    @Test
    void contextLoads() {
    }

}
