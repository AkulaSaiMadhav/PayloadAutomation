package com.madhav.payloadgeneration.service.v1.rpmdata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class RpmFileWriter {

    @Value("${app.service.textfilepath}")
    private String textFilePath;

    @Value("${app.service.csvfilepath}")
    private String csvFilePath;

    public void writeResponseToFile(List<String> formattedData) {
        try (
                BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFilePath));
                BufferedWriter textWriter = new BufferedWriter(new FileWriter(textFilePath));
        ) {
            //excel lo headers ni ikkada declare chesukovali
            csvWriter.write("ID,ParentId,State,Price,RpmId,StartDate,EndDate,Location,Tpnb");
            csvWriter.newLine();
            csvWriter.flush();

            for(String record : formattedData){
            // Write to Text File (formatted output)
                textWriter.write(record);
                textWriter.newLine();

                String id = extractValue(record, "ID: ");
                String parentId = extractValue(record, "ParentId: ");
                String state = extractValue(record, "State: ");
                String price = extractValue(record, "Price: ");
                String rpmId = extractValue(record, "RpmId: ");
                String startDate = extractValue(record, "StartDate: ");
                String endDate = extractValue(record, "EndDate: ");
                String locations = extractList(record, "Locations: ");
                String tpnb = extractList(record, "Tpnbs: ");

                // **Writing formatted values to CSV**
                csvWriter.write(String.join(",", id, parentId, state, price, rpmId, startDate, endDate, locations, tpnb));
                csvWriter.newLine();
                csvWriter.flush();
            }

            System.out.println("Filtered data written to text file: " + textFilePath);
            System.out.println("Filtered data written to CSV file: " + csvFilePath);

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String extractList(String record, String key) {
        String[] parts = record.split(key);
        if (parts.length > 1) {
            return parts[1].replace("[", "").replace("]", "").trim(); // Remove brackets
        }
        return "";
    }

    private String extractValue(String record, String key) {
        String[] parts = record.split(key);
        if (parts.length > 1) {
            return parts[1].split(" ")[0].trim(); // Get first value after key
        }
        return null;
    }
}