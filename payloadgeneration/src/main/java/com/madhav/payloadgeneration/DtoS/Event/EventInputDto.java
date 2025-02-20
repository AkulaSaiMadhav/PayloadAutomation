package com.madhav.payloadgeneration.DtoS.Event;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class EventInputDto {

    @Schema(description = "enter event start date in yyyymmdd format" , example = "yyyymmdd")
    private int startDate;

    @Schema(description = "enter end start date in yyyymmdd format" , example = "yyyymmdd")
    private int endDate;

    @Schema(description = "enter event name")
    private String eventName;

    @Schema(description = "enter Type")
    private String eventType;

    private List<String> hierarchyList;

    private String phasingCalendar;
}
