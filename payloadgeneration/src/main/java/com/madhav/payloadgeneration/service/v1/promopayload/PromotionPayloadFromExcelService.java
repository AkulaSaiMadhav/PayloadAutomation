package com.madhav.payloadgeneration.service.v1.promopayload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madhav.payloadgeneration.DtoS.Promotion.FieldUpdatedDto;
import com.madhav.payloadgeneration.DtoS.Promotion.ProductAmendedDto;
import com.madhav.payloadgeneration.DtoS.Promotion.PromotionHeaderDto;
import com.madhav.payloadgeneration.DtoS.Promotion.PromotionResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionPayloadFromExcelService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processExcelFile(InputStream excelFileInputStream,
                                 PromotionHeaderDto promotionHeaderDto,
                                 String countryCode,
                                 HttpServletResponse response) throws IOException {

        //1) --responseDto ki values assign chestunnam(payload create cheyyadaniki kaavalsina details)
        PromotionResponseDto promotionResponseDto = createPromotionHeaderAndPayload(excelFileInputStream, promotionHeaderDto, countryCode);
        //2) --oka temp file create chesi andullo data anta store chestunnam
        File tempFile = writeJsonToFile(promotionResponseDto);
        //3) --download chesey file properties anni indullo store chestunnam
        sendFileAsResponse(tempFile, promotionHeaderDto.getPromotionId(), response);
    }

    //step 1 lo di idi...payload anta ikkadey create avtundi
    private PromotionResponseDto createPromotionHeaderAndPayload(InputStream excelFileInputStream,
                                                                 PromotionHeaderDto promotionHeaderDto,
                                                                 String countryCode) throws IOException {
        PromotionResponseDto promotionResponseDto = new PromotionResponseDto();
        promotionResponseDto.setPromotion(createPromotionDetails(promotionHeaderDto, countryCode));
        promotionResponseDto.setProductsAdded(null);
        promotionResponseDto.setFundingsAmended(null);
        promotionResponseDto.setProductsAmended(parseExcelData(excelFileInputStream, promotionHeaderDto));

        return promotionResponseDto;
    }

    //step1 lo sub idi... ikkada payload header prepare chestunnam
    private PromotionResponseDto.PromotionDetails createPromotionDetails(PromotionHeaderDto promotionHeaderDto, String countryCode) {
        PromotionResponseDto.PromotionDetails details = new PromotionResponseDto.PromotionDetails();
        details.setCountryCode(countryCode);
        details.setPromotionId(promotionHeaderDto.getPromotionId());
        details.setPromotionStatus(promotionHeaderDto.getPromotionStatus());

        details.setFieldsUpdated(List.of(
                new FieldUpdatedDto("rpmStatus", promotionHeaderDto.getPromotionStatus()),
                new FieldUpdatedDto("promotionStatus", promotionHeaderDto.getPromotionStatus())
        ));
        return details;
    }

    //step1 lo sub idi -- ikkada excel nunchi details teesukuni product amendment payload prepare chestunnam
    //lopaala createProductAmendedDto untadi..deeni dwara payload lopala kavalsinavanni pettukuntuntunnam
    private List<ProductAmendedDto> parseExcelData(InputStream excelFileInputStream, PromotionHeaderDto promotionHeaderDto) throws IOException {
        List<ProductAmendedDto> productsAmended = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(excelFileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                productsAmended.add(createProductAmendedDto(
                        getCellValue(row.getCell(0)),
                        Integer.parseInt(getCellValue(row.getCell(1))),
                        getCellValue(row.getCell(2)),
                        getCellValue(row.getCell(3)),
                        promotionHeaderDto
                ));
            }
        }
        return productsAmended;
    }

    //paina dantlo di idi --- excel lo prati row ki idi execute avtundi
    private ProductAmendedDto createProductAmendedDto(String tpnb, int zone, String rpmId, String enterpriseId, PromotionHeaderDto promotionHeaderDto) {
        if (!tpnb.startsWith("0")) {
            tpnb = "0" + tpnb;
        }
        return new ProductAmendedDto(
                tpnb + ":" + zone,
                List.of(
                        new FieldUpdatedDto("rpmStatus", promotionHeaderDto.getPromotionStatus()),
                        new FieldUpdatedDto("productStatus", promotionHeaderDto.getPromotionStatus()),
                        new FieldUpdatedDto("rpmPromotionId", List.of(rpmId)),
                        new FieldUpdatedDto("enterpriseApiUUID", enterpriseId),
                        new FieldUpdatedDto("rpmErrorMessageMap", null)
                ),
                null
        );
    }

    //step2 idi - ikkada paina create ayyina payload anta oka temp file lo store chestunnam
    private File writeJsonToFile(PromotionResponseDto promotionResponseDto) throws IOException {
        File tempFile = File.createTempFile("promotion_", ".json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, promotionResponseDto);
        }
        return tempFile;
    }

    //step3 idi - download cheyyalsina file ki kavalsinavanni ikkada istunnam
    private void sendFileAsResponse(File file, String promotionId, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + promotionId + ".json");

        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            inputStream.transferTo(outputStream);
        }

        file.delete();
    }

    //excel lo every record lo data validate cheyyadam kosam idi vaadutunnam
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
