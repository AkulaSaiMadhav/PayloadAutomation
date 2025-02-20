package com.madhav.payloadgeneration.DtoS.Promotion;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

    @Data
    @AllArgsConstructor
    public class ProductAmendedDto {
        private String productZoneUuid;
        private List<FieldUpdatedDto> fieldsUpdated;
        private Object suppliersFundingDetails = null;

    }
