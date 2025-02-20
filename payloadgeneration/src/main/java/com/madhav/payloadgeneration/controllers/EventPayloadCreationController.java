package com.madhav.payloadgeneration.controllers;

import com.madhav.payloadgeneration.DtoS.Event.EventInputDto;
import com.madhav.payloadgeneration.DtoS.Event.EventResponseDto;
import com.madhav.payloadgeneration.service.v1.eventpayload.BulkEventPayloadService;
import com.madhav.payloadgeneration.service.v1.eventpayload.SingleEventPayloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/api")
@Tag(description = "Create event related payload",name="EVENT PAYLOAD CREATOR")
public class EventPayloadCreationController {

    @Autowired
    private SingleEventPayloadService singleEventPayloadService;

    @Autowired
    private BulkEventPayloadService bulkEventPayloadService;

    @Operation(summary = "Generate payload for single event",description = "Creates event payload from user input")
    @ApiResponse(responseCode = "200", description = "Successful Response")
    @ApiResponse(responseCode = "400", description = "Invalid Input")
    @PostMapping(value = "/singleEventPayload")
    public EventResponseDto eventCreationPayload(@RequestBody EventInputDto eventInputDto, @RequestHeader String countryCode){

        return singleEventPayloadService.generateEventPayload(eventInputDto,countryCode);
    }

    @Operation(summary = "Generate bulk event payload",description = "Creates event payload from excel")
    @ApiResponse(responseCode = "200", description = "Successful Response")
    @ApiResponse(responseCode = "400", description = "Invalid Input")
    @PostMapping(value = "/EventPayloadFromExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void eventCreationPayloadFromExcel(@RequestParam("file") MultipartFile file,
                                                          @RequestHeader String countryCode,
                                                          HttpServletResponse response) throws IOException {

        bulkEventPayloadService.generateEventPayloadFromExcel(file.getInputStream(),countryCode, response);
    }
}
