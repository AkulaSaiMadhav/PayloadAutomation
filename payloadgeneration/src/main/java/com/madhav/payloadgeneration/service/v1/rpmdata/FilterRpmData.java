package com.madhav.payloadgeneration.service.v1.rpmdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FilterRpmData {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RpmFileWriter rpmFileWriter;

    public List<String> filteredRPM(List<String> responseList) throws JsonProcessingException {
        List<String> formattedData = new ArrayList<>();
        for (String response : responseList) {
            // Parse the string into a JSON array
            ArrayNode jsonArray = (ArrayNode) objectMapper.readTree(response);

            for (JsonNode node : jsonArray) {
                //manaki kaavalsina fields ni ikkada
                String id = node.has("id") ? node.get("id").asText() : "N/A";
                String parentId = node.has("parentId") ? node.get("parentId").asText() : "N/A";
                String state = node.has("state") ? node.get("state").asText() : "N/A";
                String startDate = node.has("startDateTime") ? node.get("startDateTime").asText() : "N/A";
                String endDate = node.has("endDateTime") ? node.get("endDateTime").asText() : "N/A";

                String rpmId = "N/A";
                JsonNode transitionalNode = node.get("transitional");
                if (transitionalNode != null && transitionalNode.isObject()) { // Check if it's an object
                    if (transitionalNode.has("referenceNumber")) {
                        rpmId = transitionalNode.get("referenceNumber").asText();
                    }
                }

                String price = "N/A";
                if (node.has("rewardRules") && node.get("rewardRules").isArray() && node.get("rewardRules").size() > 0) {
                    JsonNode firstRule = node.get("rewardRules").get(0);
                    if (firstRule.has("price")) {
                        price = firstRule.get("price").asText();
                    }
                }

                Set<String> tpnbsSet = new HashSet<>();
                extractTpnbs(node, tpnbsSet);
                System.out.println("TPNBS List: " + tpnbsSet);

                JsonNode locationClustersNode = node.path("locationClusters");
                List<String> locationClustersList = new ArrayList<>();
                if (locationClustersNode.isArray()) {
                    for (JsonNode clusterNode : locationClustersNode) {
                        locationClustersList.add(clusterNode.asText());
                    }
                }

                String formattedRecord = String.format(" ID: %s ParentId: %s State: %s Price: %s RpmId: %s StartDate: %s EndDate: %s Locations: %s Tpnbs: %s", id, parentId, state, price, rpmId, startDate,endDate,locationClustersList, tpnbsSet);
                formattedData.add(formattedRecord);
            }
        }
        // Write filtered data to file
        System.out.println("formattedData is : " + formattedData);
        rpmFileWriter.writeResponseToFile(formattedData);
        return formattedData;
    }
    private void extractTpnbs(JsonNode node, Set<String> tpnbsSet) {
        if (node.isObject()) {
            // If the node is an object, iterate through its fields
            node.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                JsonNode childNode = entry.getValue();
                if ("tpnbs".equals(fieldName) && childNode.isArray()) {
                    // If the field is "tpnbs" and is an array, add its elements to the list
                    childNode.forEach(tpnbsElement -> tpnbsSet.add(tpnbsElement.asText()));
                } else {
                    // Recursively process child nodes
                    extractTpnbs(childNode, tpnbsSet);
                }
            });
        } else if (node.isArray()) {
            // If the node is an array, process each element
            node.forEach(element -> extractTpnbs(element, tpnbsSet));
        }
    }
}