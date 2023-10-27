package com.phonecompany.billing;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    // read csv file
    //extract telNum
    //find the most called one
    //read the data formats - time
    //create method for counting each call
    // in the method don't count the most called num
    //return total for all csv file
    //tests???

    @Override
    public BigDecimal calculate(String phoneLog) {
        Map<String, Integer> telNumCount = new HashMap<>();

        try {
            CSVReader reader = new CSVReader(new FileReader(phoneLog));
            String[] currentLine;

            while ((currentLine = reader.readNext()) != null){
                String telNum = currentLine[0];

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


        return null;
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
