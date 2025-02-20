package com.madhav.payloadgeneration.DtoS.Promotion;

import lombok.AllArgsConstructor;
import lombok.Data;
 @Data
    @AllArgsConstructor
    public class FieldUpdatedDto {
        private String fieldName;
        private Object fieldValue;
    }

