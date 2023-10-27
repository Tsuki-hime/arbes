package com.phonecompany.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class BillingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingApplication.class, args);
        TelephoneBillCalculatorImpl calculator = new TelephoneBillCalculatorImpl();

        String filepath = "forTheTest.csv";
        String filepath2 = "otherTestFile.csv";

        BigDecimal total = calculator.calculate(filepath2);
        System.out.println("Total amount: " + total);
    }

}
