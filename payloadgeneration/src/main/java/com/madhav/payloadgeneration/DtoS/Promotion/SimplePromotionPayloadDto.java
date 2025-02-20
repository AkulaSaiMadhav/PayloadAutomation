package com.madhav.payloadgeneration.DtoS.Promotion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SimplePromotionPayloadDto {
        @Schema(description = "Unique Promotion ID", example = "trn:tesco:promotion:uuid:aaaaa")
        private String promotionId;

        @Schema(description = "Promotion Status", example = "Submitted")
        private String promotionStatus;

        @Schema(description = "Include printLabel in response if true", example = "true")
        private boolean printLabel;

    }

