package com.madhav.payloadgeneration.service.v1.eventpayload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madhav.payloadgeneration.DtoS.Event.EventResponseDto;
import com.madhav.payloadgeneration.DtoS.Event.HierarchyDto;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BulkEventPayloadService {

    @Autowired
    private SingleEventPayloadService singleEventPayloadService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void generateEventPayloadFromExcel(InputStream inputStream, String countryCode, HttpServletResponse response) throws IOException {
        //for payload
        EventResponseDto eventResponseDto = createPayload(inputStream, countryCode);

        File tempFile = writeJsonToFile(eventResponseDto);
        //3) --download chesey file properties anni indullo store chestunnam
        sendFileAsResponse(tempFile, response);
    }


    private EventResponseDto createPayload(InputStream inputStream, String countryCode) throws IOException {
        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setEvents(parseExcelData(inputStream));

        return eventResponseDto;
    }

    private List<EventResponseDto.Events> parseExcelData(InputStream inputStream) throws IOException {
        List<EventResponseDto.Events> eventResponse = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();  // Skip the header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                EventResponseDto.Events event = new EventResponseDto.Events();

                LocalDate eventEndDate = row.getCell(6).getLocalDateTimeCellValue().toLocalDate();
                String formattedEndDate = eventEndDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                event.setEndDateForActiveStatus(Integer.parseInt(formattedEndDate));

                LocalDate eventStartDate = row.getCell(5).getLocalDateTimeCellValue().toLocalDate();
                String formattedStartDate = eventStartDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                event.setEndDateForActiveStatus(Integer.parseInt(formattedStartDate));

                // Read the columns from the row
                event.setCountryCode(row.getCell(0).getStringCellValue().trim());
                event.setEndDateForActiveStatus(Integer.parseInt(formattedEndDate));
                event.setEndDateForClosedStatus(Integer.parseInt(formattedEndDate));
                event.setEndDateForSignoffStatus(Integer.parseInt(formattedEndDate));
                event.getEnterpriseAPICallEnabled();
                event.getEventClassificationCode();
                event.getEventClassificationDesc();
                event.setEventCompletionDate(Integer.parseInt(formattedEndDate));
                event.setEventDescription(row.getCell(7).getStringCellValue().trim());
                event.setEventEndDate(Integer.parseInt(formattedEndDate));
                event.setEventName(row.getCell(4).getStringCellValue().trim());
                event.setEventPhasingCalendar(row.getCell(10).getStringCellValue().trim());
                event.setEventStartDate(Integer.parseInt(formattedStartDate));
                event.getEventStatus();
                event.setEventThemeDesc(row.getCell(3).getStringCellValue().trim());
                event.setEventTypeDesc(row.getCell(2).getStringCellValue().trim());

                // Split hierarchy values and add to list
                String hierarchyValues = row.getCell(9).getStringCellValue().trim();
                List<String> hierarchyList = Arrays.asList(hierarchyValues.split("\\|"));

                List<HierarchyDto> hierarchyDtoList = hierarchyList.stream()
                        .map(categoryArea -> new HierarchyDto(categoryArea, "categoryArea"))
                        .collect(Collectors.toList());
                event.setHierarchyDesc(hierarchyDtoList);

                // Phasing calendar and comments (if any)
                event.getHierarchyType();
                event.setStartDateForOpenStatus(Integer.parseInt(formattedStartDate));
                // Add the event to the events list
                eventResponse.add(event);
            }
        }

        // Set the events list in the response DTO
        return eventResponse;
    }

    private File writeJsonToFile(EventResponseDto eventResponseDto) throws IOException {
        File tempFile = File.createTempFile("promotion_", ".json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, eventResponseDto);
        }
        return tempFile;
    }

    private void sendFileAsResponse(File file, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + LocalDateTime.now() + ".json");

        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            inputStream.transferTo(outputStream);
        }

        file.delete();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getDateCellValue().toString()
                    : String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
