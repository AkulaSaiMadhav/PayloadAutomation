package com.madhav.payloadgeneration.DtoS.Event;

import lombok.Data;

import java.util.List;

@Data
public class EventResponseDto {
    private List<Events> events;
    @Data
    public static class Events {
        private String countryCode;
        private int endDateForActiveStatus;
        private int endDateForClosedStatus;
        private int endDateForSignoffStatus;
        private Object enterpriseAPICallEnabled = true;
        private String eventClassificationCode = "C1";
        private String eventClassificationDesc = "PROMOTION";
        private int eventCompletionDate;
        private String eventDescription;
        private int eventEndDate;
        private String eventName;
        private String eventPhasingCalendar;
        private int eventStartDate;
        private String eventStatus = "new";
        private String eventThemeDesc;
        private String eventTypeDesc;
        private List<HierarchyDto> hierarchyDesc;
        private String hierarchyType = "buyer";
        private int startDateForOpenStatus;
    }
}
