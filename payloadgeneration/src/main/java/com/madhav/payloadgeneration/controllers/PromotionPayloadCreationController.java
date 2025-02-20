package com.madhav.payloadgeneration.controllers;

import com.madhav.payloadgeneration.DtoS.Promotion.PromotionHeaderDto;
import com.madhav.payloadgeneration.DtoS.Promotion.PromotionInputDto;
import com.madhav.payloadgeneration.DtoS.Promotion.PromotionResponseDto;
import com.madhav.payloadgeneration.DtoS.Promotion.SimplePromotionPayloadDto;
import com.madhav.payloadgeneration.service.v1.promopayload.ComplexPromotionPayloadService;
import com.madhav.payloadgeneration.service.v1.promopayload.PromotionPayloadFromExcelService;
import com.madhav.payloadgeneration.service.v1.promopayload.PromotionPayloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/v1/api")
@Tag(description = "Create a Promotion Payload", name = "PROMOTION PAYLOAD CREATOR")
public class PromotionPayloadCreationController {

    @Autowired
    private PromotionPayloadService simplePayloadService;
    @Autowired
    private ComplexPromotionPayloadService complexPromotionPayloadService;

    @Autowired
    private PromotionPayloadFromExcelService promotionPayloadFromExcelService;


    @Operation(summary = "Generate Simple Promotion Payload", description = "Generate a Simple JSON payload based on PromotionID and PromotionStatus")
    @PostMapping("/generatePayload")
    public PromotionResponseDto generatePayload(@RequestBody SimplePromotionPayloadDto simplePromotionPayloadDto, @RequestHeader String countryCode) {
        return simplePayloadService.generatePayload(simplePromotionPayloadDto, countryCode);
    }


    @Operation(summary = "Generate Complex Promotion Payload", description = "Generate a Complex JSON payload based on user provided data")
    @ApiResponse(responseCode = "200", description = "Successful Response")
    @ApiResponse(responseCode = "400", description = "Invalid Input")
    @PostMapping(value = "/generateComplexPayload")
    public ResponseEntity generateComplexPromoPayload(@RequestBody PromotionInputDto promotionInputDto
            , @RequestHeader String countryCode, HttpServletResponse response) {
        complexPromotionPayloadService.generateComplexPayload(promotionInputDto, countryCode, response);

        return ResponseEntity.ok("data written to file successfully");
    }

    @Operation(summary = "Generate payload from excel", description = "Accepts input from excel file and generate payload")
    @ApiResponse(responseCode = "200", description = "Successful Response")
    @ApiResponse(responseCode = "400", description = "Invalid Input")
    @PostMapping(value = "/generatePayloadFromExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PromotionHeaderDto> generatePayloadFromExcel(@RequestParam("file") MultipartFile file,
                                                           @ModelAttribute PromotionHeaderDto promotionHeaderDto,
                                                           @RequestHeader String countryCode,
                                                           HttpServletResponse response) throws IOException {

        promotionPayloadFromExcelService.processExcelFile(file.getInputStream(),promotionHeaderDto, countryCode,response);
        return ResponseEntity.created(null).build();
    }
}