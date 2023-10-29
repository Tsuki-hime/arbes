package com.phonecompany.billing;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    @Override
    public BigDecimal calculate(String phoneLog) {
        Map<String, Integer> telNumCount = new HashMap<>();
        List<CallDetails> forTheMoney = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        String csvDTF = "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(csvDTF);


        phoneLog.lines()
                .map(line -> line.split(","))
                .forEach(currentLine -> {

                    String telNum = currentLine[0];
                    LocalDateTime startTime = LocalDateTime.parse(currentLine[1], formatter);
                    LocalDateTime endTime = LocalDateTime.parse(currentLine[2], formatter);

                    forTheMoney.add(new CallDetails(telNum, startTime, endTime));

                    telNumCount.put(telNum, telNumCount.getOrDefault(telNum, 0) + 1);
                });


        String mostCalledNum = findMostCalledNum(telNumCount);

        for (CallDetails callDetails : forTheMoney) {
            if (!callDetails.getTelNum().equals(mostCalledNum)) {
                BigDecimal priceOfCall = getPrice(callDetails.getStart(), callDetails.getEnd());
                total = total.add(priceOfCall);
            }
        }

        return total;
    }

    private BigDecimal getPrice(LocalDateTime start, LocalDateTime end) {
        BigDecimal priceOfCall = BigDecimal.ZERO;
        //rates
        BigDecimal eightToFour = new BigDecimal("1.0");
        BigDecimal theRest = new BigDecimal("0.5");
        BigDecimal afterFive = new BigDecimal("0.2");

        LocalTime beginning = LocalTime.of(8, 0, 0);
        LocalTime ending = LocalTime.of(15, 59, 59);

        int minutesCounted = 0;

        while (start.isBefore(end)) {
            BigDecimal currentRate;

            if (start.toLocalTime().isAfter(beginning) && start.toLocalTime().isBefore(ending)) {
                currentRate = eightToFour;
            } else {
                currentRate = theRest;
            }

            if (minutesCounted >= 5) {
                BigDecimal rest = new BigDecimal(Duration.between(start.toLocalTime(), end.toLocalTime()).getSeconds() / 60);
                return priceOfCall.add(afterFive.multiply(rest.add(BigDecimal.ONE)));
            }

            priceOfCall = priceOfCall.add(currentRate);
            start = start.plusMinutes(1);
            minutesCounted++;
        }
        return priceOfCall;
    }

    public static String findMostCalledNum(Map<String, Integer> telNumCount) {
        int maxCount = 0;
        String mostCalledNum = null;

        for (Map.Entry<String, Integer> pair : telNumCount.entrySet()) {
            String currentTelNum = pair.getKey();
            int count = pair.getValue();
            if (count > maxCount || (count == maxCount && currentTelNum.compareTo(mostCalledNum) > 0)) {
                maxCount = count;
                mostCalledNum = currentTelNum;
            }
        }
        return mostCalledNum;
    }
}
