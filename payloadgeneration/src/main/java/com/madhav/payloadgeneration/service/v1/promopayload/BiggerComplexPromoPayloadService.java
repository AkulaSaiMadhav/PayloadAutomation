package com.madhav.payloadgeneration.service.v1.promopayload;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madhav.payloadgeneration.DtoS.Promotion.PromotionInputDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BiggerComplexPromoPayloadService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CompletableFuture<String> generateLargeComplexPayload(PromotionInputDto inputDto, String countryCode) throws IOException {
        try {
            StringWriter stringWriter = new StringWriter();
            JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);

            jsonGenerator.writeStartObject();

            // Write promotion object
            jsonGenerator.writeObjectFieldStart("promotion");
            jsonGenerator.writeStringField("countryCode", countryCode);
            jsonGenerator.writeStringField("promotionId", inputDto.getPromotionId());
            jsonGenerator.writeStringField("promotionStatus", inputDto.getPromotionStatus());
            jsonGenerator.writeNullField("notificationActions");
            jsonGenerator.writeBooleanField("resetFundingStatus", false);
            jsonGenerator.writeBooleanField("resetFunding", false);

            // Write fieldsUpdated array
            jsonGenerator.writeArrayFieldStart("fieldsUpdated");
            writeField(jsonGenerator, "rpmStatus", "Uploaded");
            writeField(jsonGenerator, "promotionStatus", "Uploaded");
            jsonGenerator.writeEndArray();

            jsonGenerator.writeStringField("userUuid", "trn:tesco:uid:uuid:998008ee-ea17-435c-a403-12df70dbf780");
            jsonGenerator.writeStringField("userType", "SERVICE");
            jsonGenerator.writeStringField("roleName", "SERVICE");
            jsonGenerator.writeEndObject();

            jsonGenerator.writeNullField("productsAdded");

            // Process TPNBs in chunks
            jsonGenerator.writeArrayFieldStart("productsAmended");
            for (List<String> chunk : partitionList(inputDto.getTpnbs(), 500)) {
                for (String tpnb : chunk) {
                    for (Integer zone : inputDto.getZones()) {
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeStringField("productZoneUuid", tpnb + ":" + zone);

                        jsonGenerator.writeArrayFieldStart("fieldsUpdated");
                        writeField(jsonGenerator, "rpmStatus", inputDto.getPromotionStatus());
                        writeField(jsonGenerator, "productStatus", inputDto.getPromotionStatus());
                        writeField(jsonGenerator, "rpmPromotionId", inputDto.getRpmId());
                        writeField(jsonGenerator, "enterpriseApiUUID", inputDto.getEnterpriseId());
                        writeField(jsonGenerator, "rpmErrorMessageMap", null);
                        jsonGenerator.writeEndArray();

                        jsonGenerator.writeNullField("suppliersFundingDetails");
                        jsonGenerator.writeEndObject();
                    }
                }
            }
            jsonGenerator.writeEndArray();

            jsonGenerator.writeNullField("fundingsAmended");
            jsonGenerator.writeEndObject();
            jsonGenerator.close();

            return CompletableFuture.completedFuture(stringWriter.toString());

        } catch (IOException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private void writeField(JsonGenerator jsonGenerator, String fieldName, Object fieldValue) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fieldName", fieldName);
        if (fieldValue instanceof String) {
            jsonGenerator.writeStringField("fieldValue", (String) fieldValue);
        } else {
            jsonGenerator.writeNullField("fieldValue");
        }
        jsonGenerator.writeEndObject();
    }

    private List<List<String>> partitionList(List<String> list, int batchSize) {
        return list.stream()
                .collect(Collectors.groupingBy(i -> list.indexOf(i) / batchSize))
                .values().stream().toList();
    }
}
