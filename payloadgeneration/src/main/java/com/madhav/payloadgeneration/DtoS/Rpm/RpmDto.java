package com.madhav.payloadgeneration.DtoS.Rpm;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Data
@NoArgsConstructor(force = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RpmDto {

   //@JsonProperty(value = "enterpriseID")
   private final String id;

   private final String parentId;

   private final String startDateTime;

   private final String endDateTime;

   private final String name;

   private final String shortDescription;

   private final List<String> locationClusters;

   private final String currency;

   private final Country country;

   private final Condition condition;

   private final List<RewardRule> rewardRules;

   private final String phase;

   private final String state;

   private final int maxApplications;

   private final String advertiseDateTime;

   private final Transitional transitional;

   private final String promoType;

   private final Meta meta;

   private final boolean createdBySupplier;

   @Getter
   @RequiredArgsConstructor
   @ToString
   @NoArgsConstructor(force = true)
   public static class Country {
      private final String countryCode;
   }

   @Getter
   @RequiredArgsConstructor
   @ToString
   @NoArgsConstructor(force = true)
   public static class Condition {
      private final String id;
      private final String type;
      private final List<SubCondition> subConditions;
   }

   @Getter
   @RequiredArgsConstructor
   @ToString
   @NoArgsConstructor(force = true)
   public static class SubCondition {
      private final String id;
      private final String type;
      private final String scheme;
      private final boolean suppressMissedRewardsForAnonymous;
      private final boolean identifiedWithScheme;
      private final List<String> tpnbs; // Added tpnbs
      private final String productGroup;
      private final String requiredQuantityMin;
      private final String requiredQuantityMax;
      private final boolean cheapest;
      private final String sellingUOM;
   }

   @Getter
   @RequiredArgsConstructor
   @ToString
   @NoArgsConstructor(force = true)
   public static class RewardRule {
      private final String type;
      private final ApplyTo applyTo;
      private final boolean addToTillRewards;
      private final double price;
   }

   @Getter
   @RequiredArgsConstructor
   @ToString
   @NoArgsConstructor(force = true)
   public static class ApplyTo {
      private final List<String> conditions;
      private final boolean cheapest;
      private final String maxQuantity;
   }

   @Getter
   @RequiredArgsConstructor
   @ToString
   @NoArgsConstructor(force = true)
   public static class Transitional {
      private final String referenceNumber;
      private final int extId;
      private final boolean usesFeatureSpace;
      private final boolean posIndicator;
      private final boolean printLabel;
   }

   @Getter
   @RequiredArgsConstructor
   @ToString
   @NoArgsConstructor(force = true)
   public static class Meta {
      private final List<String> subClasses;
   }
}
