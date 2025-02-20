package com.madhav.payloadgeneration.DtoS.Promotion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class PromotionResponseDto {
    private PromotionDetails promotion;
    private Object productsAdded = null;
    private List<ProductAmendedDto> productsAmended;
    private Object fundingsAmended = null;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PromotionDetails {
        private String countryCode;
        private String promotionId;
        private String promotionStatus;
        private Object notificationActions = null;
        private boolean resetFundingStatus = false;
        private boolean resetFunding = false;
        private List<FieldUpdatedDto> fieldsUpdated;
        private String userUuid = "trn:tesco:uid:uuid:998008ee-ea17-435c-a403-12df70dbf780";
        private String userType = "SERVICE";
        private String roleName = "SERVICE";
    }
}
