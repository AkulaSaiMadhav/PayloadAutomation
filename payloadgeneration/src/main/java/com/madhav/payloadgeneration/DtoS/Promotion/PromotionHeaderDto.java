package com.madhav.payloadgeneration.DtoS.Promotion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request DTO for generating complex promotion payload")
public class PromotionHeaderDto {

        @Schema(description = "Unique Promotion ID", example = "trn:tesco:promotion:uuid:12345")
        private String promotionId;

        @Schema(description = "Promotion Status", example = "Uploaded/Withdrawn")
        private String promotionStatus;
}
