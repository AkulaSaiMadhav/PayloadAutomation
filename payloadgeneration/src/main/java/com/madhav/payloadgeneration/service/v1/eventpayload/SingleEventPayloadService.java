package com.madhav.payloadgeneration.service.v1.eventpayload;

import com.madhav.payloadgeneration.DtoS.Event.EventInputDto;
import com.madhav.payloadgeneration.DtoS.Event.EventResponseDto;
import com.madhav.payloadgeneration.DtoS.Event.HierarchyDto;
import com.madhav.payloadgeneration.constants.EventConstants;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SingleEventPayloadService {

    public EventResponseDto generateEventPayload(EventInputDto eventInputDto, String countryCode) {

        EventResponseDto eventResponseDto = new EventResponseDto();
        EventResponseDto.Events events = new EventResponseDto.Events();

        events.setCountryCode(countryCode);
        events.setEndDateForActiveStatus(eventInputDto.getEndDate());
        events.setEndDateForClosedStatus(eventInputDto.getEndDate());
        events.setEndDateForSignoffStatus(eventInputDto.getEndDate());
        events.getEnterpriseAPICallEnabled();
        events.getEventClassificationCode();
        events.getEventClassificationDesc();
        events.setEventCompletionDate(eventInputDto.getEndDate());
        events.setEventDescription(eventInputDto.getEventName());
        events.setEventEndDate(eventInputDto.getEndDate());
        events.setEventName(eventInputDto.getEventName());
        events.setEventPhasingCalendar("");
        events.setEventStartDate(eventInputDto.getStartDate());
        events.getEventStatus();
        events.setEventThemeDesc(eventInputDto.getEventName());
        if(eventInputDto.getEventType().toLowerCase().contains("event")){
            events.setEventTypeDesc(EventConstants.THEMED_EVENTS);}
        else if (eventInputDto.getEventType().toLowerCase().contains("period")){
            events.setEventTypeDesc(EventConstants.PROMOTION_PERIOD);}
        else events.setEventTypeDesc("enter correct value");

        List<String> hierarchyList = eventInputDto.getHierarchyList() != null ?
                eventInputDto.getHierarchyList() : List.of();

        List<HierarchyDto> hierarchyDtoList = hierarchyList.stream()
                .map(categoryArea -> new HierarchyDto(categoryArea, "categoryArea"))
                .collect(Collectors.toList());

        events.setHierarchyDesc(hierarchyDtoList);
        events.getHierarchyType();
        events.setStartDateForOpenStatus(eventInputDto.getStartDate());
        eventResponseDto.setEvents(List.of(events));

        return eventResponseDto;
    }
}
