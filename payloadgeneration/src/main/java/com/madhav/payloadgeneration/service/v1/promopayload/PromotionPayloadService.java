package com.madhav.payloadgeneration.service.v1.promopayload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madhav.payloadgeneration.DtoS.Promotion.FieldUpdatedDto;
import com.madhav.payloadgeneration.DtoS.Promotion.PromotionResponseDto;
import com.madhav.payloadgeneration.DtoS.Promotion.SimplePromotionPayloadDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionPayloadService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PromotionResponseDto generatePayload(SimplePromotionPayloadDto simplePromotionPayloadDto, String countryCode) {

        PromotionResponseDto promotionResponseDto = new PromotionResponseDto();
        PromotionResponseDto.PromotionDetails promotionDetails = new PromotionResponseDto.PromotionDetails();
        promotionDetails.setCountryCode(countryCode);
        promotionDetails.setPromotionId(simplePromotionPayloadDto.getPromotionId());
        promotionDetails.setPromotionStatus(simplePromotionPayloadDto.getPromotionStatus());

        List<FieldUpdatedDto> fieldsUpdated = new ArrayList<>();
        fieldsUpdated.add(new FieldUpdatedDto("rpmStatus", simplePromotionPayloadDto.getPromotionStatus()));
        fieldsUpdated.add(new FieldUpdatedDto("promotionStatus", simplePromotionPayloadDto.getPromotionStatus()));
        if (simplePromotionPayloadDto.isPrintLabel()) {
            fieldsUpdated.add(new FieldUpdatedDto("printLabel", true));
        }
        promotionDetails.setFieldsUpdated(fieldsUpdated);

        promotionResponseDto.setPromotion(promotionDetails);
        promotionResponseDto.setProductsAdded(null);
        promotionResponseDto.setProductsAmended(null);
        promotionResponseDto.setFundingsAmended(null);
        return promotionResponseDto;
    }
}

