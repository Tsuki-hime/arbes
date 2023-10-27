package com.phonecompany.billing;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    //create method for counting each call
    // in the method don't count the most called num
    //return total for all csv file
    //tests???

    @Override
    public BigDecimal calculate(String phoneLog) {
        Map<String, CallDetails> telNumCount = new HashMap<>();
        List<CallDetails> forTheMoney = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        try {
            //loading data from the file
            CSVReader reader = new CSVReader(new FileReader(phoneLog));
            String[] currentLine;

            //adjusting time data format
            String csvDTF = "dd-MM-yyyy HH:mm:ss";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(csvDTF);


            while ((currentLine = reader.readNext()) != null) {
                //breaking the line
                String telNum = currentLine[0];
                LocalDateTime startTime = LocalDateTime.parse(currentLine[1], formatter);
                LocalDateTime endTime = LocalDateTime.parse(currentLine[2], formatter);

                //adding all callDetails to list to count price
                forTheMoney.add(new CallDetails(telNum, startTime, endTime));

                //saving telNums and their occurrences to map
                if (telNumCount.containsKey(telNum)) {
                    CallDetails cd = telNumCount.get(telNum);
                    cd.setOccurrence(cd.getOccurrence() + 1);
                } else {
                    CallDetails cd = new CallDetails(telNum, startTime, endTime, 1);
                    telNumCount.put(telNum, cd);
                }
            }

            String mostCalledNum = findMostCalledNum(telNumCount);

            //sums up all calls except for mostCalledNum
            for (CallDetails callDetails : forTheMoney) {
                if (!callDetails.getTelNum().equals(mostCalledNum)) {
                    BigDecimal priceOfCall = payUp(callDetails.getStart(), callDetails.getEnd());
                    total = total.add(priceOfCall);
                }
            }
        } catch (FileNotFoundException fnf) {
            System.out.println("Can not find the file.");
        } catch (IOException | CsvValidationException ioe) {
            System.out.println("Can not read the file.");
        }

        return total;
    }

    private BigDecimal payUp(LocalDateTime start, LocalDateTime end) {


        return null;
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
