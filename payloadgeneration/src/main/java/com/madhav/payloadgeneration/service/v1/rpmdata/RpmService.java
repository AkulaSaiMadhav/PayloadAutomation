package com.madhav.payloadgeneration.service.v1.rpmdata;

import com.madhav.payloadgeneration.service.HttpHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RpmService {

    @Autowired
    private HttpHeaderService httpHeaderService;

    @Autowired
    private FilterRpmData filterRpmData;

    @Value("${app.service.rpmUrl}")
    private String rpmUrl;

    //REstTemplate ni external API ni hit cheyyadaniki vaadatam..idi deprecate chesesaru...deeni badulu WebClient ni use chesukovacchu
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<List<String>> fetchData(String accessToken, List<String> rpmId) throws IOException {

        //REST Api ki kaavalsina headers anni indullo store chesukuntam
        //HttpServletRequest ani okati untadi...daani dwara token manam swagger lo icchinappudu code ki pass chestundi
        HttpHeaders headers = httpHeaderService.setHeaders("Bearer ", accessToken);

        //headers and body ni kalisi pampadaniki vaadatam
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //anni rpm ids ki api url ni amend chestunnam and adi oka list loki store chestunnam
        List<String> fullUrls = rpmId.stream()
                                    .map(id -> rpmUrl + id)
                                    .collect(Collectors.toList());

        //paina generate ayna urls hit chesi vaati responses annintini oka list loki store chestunnam
        List<String> responses = fullUrls.stream()
                .map(url -> {
                    try {
                        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                        return response.getBody();
                    } catch (Exception e) {
                        return "Error fetching data for URL: " + url + " - " + e.getMessage();
                    }
                }).collect(Collectors.toList());
        List<String> filteredData = filterRpmData.filteredRPM(responses);
        return ResponseEntity.ok(filteredData);
    }
}
