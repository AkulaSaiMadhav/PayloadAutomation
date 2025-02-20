package com.madhav.payloadgeneration.controllers;

import com.madhav.payloadgeneration.service.v1.rpmdata.RpmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
@Tag(description = "to fetch the details of RPM ID", name="RPM ID FETCHER")
public class RpmController {

    private RpmService rpmService;

    public RpmController(RpmService rpmService) {
        this.rpmService = rpmService;
    }

    @PostMapping("/fetch-data")
    public ResponseEntity<List<String>> getData(HttpServletRequest request,@RequestBody List<String> rpmId) throws IOException {

        return rpmService.fetchData(request.getHeader("Authorization"),rpmId);

    }
}
