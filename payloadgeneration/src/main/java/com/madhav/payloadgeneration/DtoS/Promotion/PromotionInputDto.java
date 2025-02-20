package com.madhav.payloadgeneration.DtoS.Promotion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
    @Schema(description = "Request DTO for generating complex promotion payload")
    public class PromotionInputDto {

        @Schema(description = "Unique Promotion ID", example = "trn:tesco:promotion:uuid:12345")
        private String promotionId;

        @Schema(description = "Promotion Status", example = "Uploaded/Withdrawn")
        private String promotionStatus;

        @Schema(description = "RPM Promotion ID", example = "91081648")
        private String rpmId;

        @Schema(description = "Enterprise API UUID", example = "307a7407-6ce9-4777-b043-debfb233bfb8")
        private String enterpriseId;

        @Schema(description = "List of TPNBs", example = "[\"012345678\", \"098765432\"]")
        private List<String> tpnbs;

        @Schema(description = "List of Zones", example = "[5, 15, 25]")
        private List<Integer> zones;
    }
