package com.phonecompany.billing;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    //read the data formats - time
    //create method for counting each call
    // in the method don't count the most called num
    //return total for all csv file
    //tests???

    @Override
    public BigDecimal calculate(String phoneLog) {
        Map<String, Integer> telNumCount = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;

        try {
            //loading data from the file
            CSVReader reader = new CSVReader(new FileReader(phoneLog));
            String[] currentLine;

            //adjusting time data format
            String csvDTF = "dd-MM-yyyy HH:mm:ss";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(csvDTF);


            while ((currentLine = reader.readNext()) != null){
                //breaking the line
                String telNum = currentLine[0];
                LocalDateTime startTime = LocalDateTime.parse(currentLine[1], formatter);
                LocalDateTime endTime = LocalDateTime.parse(currentLine[2], formatter);


                //saving telNums and their occurrences to map
                if (telNumCount.containsKey(telNum)){
                    int newCount = telNumCount.get(telNum) + 1;
                    telNumCount.put(telNum, newCount);
                } else {
                    telNumCount.put(telNum, 1);
                }
            }

            String mostCalledNum = findMostCalledNum(telNumCount);


        } catch (FileNotFoundException fnf){
            System.out.println("Can not find the file.");
        } catch (IOException | CsvValidationException ioe) {
            System.out.println("Can not read the file.");
        }


        return total;
    }

    public static String findMostCalledNum(Map<String, Integer> telNumCount){
        int maxCount = 0;
        String mostCalledNum = null;

        for (Map.Entry<String, Integer> pair : telNumCount.entrySet()) {
            String currentTelNum = pair.getKey();
            int count = pair.getValue();
            if (count > maxCount || (count == maxCount && currentTelNum.compareTo(mostCalledNum) > 0)){
                maxCount = count;
                mostCalledNum = currentTelNum;
            }
        }
        return mostCalledNum;
    }
}
