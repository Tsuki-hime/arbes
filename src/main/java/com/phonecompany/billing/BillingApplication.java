package com.phonecompany.billing;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class BillingApplication {

    public static void main(String[] args) {
        TelephoneBillCalculatorImpl calculator = new TelephoneBillCalculatorImpl();

        String filepath = "forTheTest.csv";
        try {
            BigDecimal total = calculator.calculate(Files.readString(Path.of(filepath)));
            System.out.println("Total amount: " + total);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
