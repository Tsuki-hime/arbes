package com.phonecompany.billing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    @Override
    public BigDecimal calculate(String phoneLog) {
        Map<String, CallDetails> telNumCount = new HashMap<>();
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

                    //saving telNums and their occurrences to map
                    if (telNumCount.containsKey(telNum)) {
                        CallDetails cd = telNumCount.get(telNum);
                        cd.setOccurrence(cd.getOccurrence() + 1);
                    } else {
                        CallDetails cd = new CallDetails(telNum, startTime, endTime, 1);
                        telNumCount.put(telNum, cd);
                    }
                });


        String mostCalledNum = findMostCalledNum(telNumCount);

        //sums up all calls except for mostCalledNum
        for (CallDetails callDetails : forTheMoney) {
            if (!callDetails.getTelNum().equals(mostCalledNum)) {
                BigDecimal priceOfCall = payUp(callDetails.getStart(), callDetails.getEnd());
                total = total.add(priceOfCall);
            }
        }

        return total;
    }

    private BigDecimal payUp(LocalDateTime start, LocalDateTime end) {
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
                currentRate = afterFive;
            }

            priceOfCall = priceOfCall.add(currentRate);
            start = start.plusMinutes(1);
            minutesCounted++;
        }
        return priceOfCall;
    }

    public static String findMostCalledNum(Map<String, CallDetails> telNumCount) {
        int maxCount = 0;
        String mostCalledNum = null;

        for (Map.Entry<String, CallDetails> pair : telNumCount.entrySet()) {
            String currentTelNum = pair.getKey();
            int count = pair.getValue().getOccurrence();
            if (count > maxCount || (count == maxCount && currentTelNum.compareTo(mostCalledNum) > 0)) {
                maxCount = count;
                mostCalledNum = currentTelNum;
            }
        }
        return mostCalledNum;
    }
}
