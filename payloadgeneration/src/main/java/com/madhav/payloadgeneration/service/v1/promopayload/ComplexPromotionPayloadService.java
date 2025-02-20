package com.madhav.payloadgeneration.service.v1.promopayload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.madhav.payloadgeneration.DtoS.Promotion.ProductAmendedDto;
import com.madhav.payloadgeneration.DtoS.Promotion.FieldUpdatedDto;
import com.madhav.payloadgeneration.DtoS.Promotion.PromotionInputDto;
import com.madhav.payloadgeneration.DtoS.Promotion.PromotionResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplexPromotionPayloadService {

    private final ObjectMapper objectMapper;

    public ComplexPromotionPayloadService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void generateComplexPayload(PromotionInputDto promotionInputDto, String countryCode, HttpServletResponse response) {
        try {
            PromotionResponseDto promotionResponseDto = createComplexPromotionPayload(promotionInputDto, countryCode);
            File tempFile = File.createTempFile("promotion_", ".json");
            tempFile.deleteOnExit();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                objectMapper.writeValue(writer, promotionResponseDto);
            }

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + promotionInputDto.getPromotionId() + ".json");

            try (OutputStream outputStream = response.getOutputStream()) {
                Files.copy(tempFile.toPath(), outputStream);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error generating or downloading payload", e);
        }
    }

    private PromotionResponseDto createComplexPromotionPayload(PromotionInputDto promotionInputDto, String countryCode) {
        if (promotionInputDto == null) {
            throw new IllegalArgumentException("PromotionInputDto cannot be null");
        }

        PromotionResponseDto promotionResponseDto = new PromotionResponseDto();
        PromotionResponseDto.PromotionDetails promotionDetails = new PromotionResponseDto.PromotionDetails();

        promotionDetails.setCountryCode(countryCode);
        promotionDetails.setPromotionId(promotionInputDto.getPromotionId());
        promotionDetails.setPromotionStatus(promotionInputDto.getPromotionStatus());

        promotionDetails.setFieldsUpdated(List.of(
                new FieldUpdatedDto("rpmStatus", promotionInputDto.getPromotionStatus()),
                new FieldUpdatedDto("promotionStatus", promotionInputDto.getPromotionStatus())
        ));

        promotionResponseDto.setPromotion(promotionDetails);
        promotionResponseDto.setProductsAdded(null);
        promotionResponseDto.setFundingsAmended(null);

        // Validate lists
        List<String> tpnbs = promotionInputDto.getTpnbs() != null ? promotionInputDto.getTpnbs() : List.of();
        List<Integer> zones = promotionInputDto.getZones() != null ? promotionInputDto.getZones() : List.of();

        // Generate productsAmended
        List<ProductAmendedDto> productsAmended = tpnbs.stream()
                .flatMap(tpnb -> zones.stream()
                        .map(zone -> createProductAmendedDto(tpnb, zone, promotionInputDto))
                ).collect(Collectors.toList());

        promotionResponseDto.setProductsAmended(productsAmended);
        return promotionResponseDto;
    }

    private ProductAmendedDto createProductAmendedDto(String tpnb, Integer zone, PromotionInputDto promotionInputDto) {
        return new ProductAmendedDto(
                tpnb + ":" + zone,
                List.of(
                        new FieldUpdatedDto("rpmStatus", promotionInputDto.getPromotionStatus()),
                        new FieldUpdatedDto("productStatus", promotionInputDto.getPromotionStatus()),
                        new FieldUpdatedDto("rpmPromotionId", List.of(promotionInputDto.getRpmId())),
                        new FieldUpdatedDto("enterpriseApiUUID", promotionInputDto.getEnterpriseId()),
                        new FieldUpdatedDto("rpmErrorMessageMap", null)
                ),
                null
        );
    }
}
