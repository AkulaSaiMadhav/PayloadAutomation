package com.madhav.payloadgeneration.DtoS.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HierarchyDto {
    private String hierarchyCode;
    private String hierarchyLevel = "categoryArea";
}
