package com.numshield.numshield_api.operator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collections;

public final class TelecomOperatorDetector {

    private static final String CAMEROON_COUNTRY_CODE = "+237";

    private static final Map<String, TelecomOperator> PREFIXES = createPrefixMap();

    private TelecomOperatorDetector() {
    }

    public static TelecomOperator detect(String normalizedNumber) {
        if (normalizedNumber == null || !normalizedNumber.matches("^\\+2376\\d{8}$")) {
            throw new IllegalArgumentException("Operator detection requires a normalized Cameroon mobile number");
        }

        String nationalNumber = normalizedNumber.substring(CAMEROON_COUNTRY_CODE.length());
        return PREFIXES.entrySet().stream()
                .filter(entry -> nationalNumber.startsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(TelecomOperator.UNKNOWN);
    }

    private static Map<String, TelecomOperator> createPrefixMap() {
        Map<String, TelecomOperator> prefixes = new LinkedHashMap<>();
        prefixes.put("650", TelecomOperator.MTN);
        prefixes.put("651", TelecomOperator.MTN);
        prefixes.put("652", TelecomOperator.MTN);
        prefixes.put("653", TelecomOperator.MTN);
        prefixes.put("654", TelecomOperator.MTN);
        prefixes.put("655", TelecomOperator.ORANGE);
        prefixes.put("656", TelecomOperator.ORANGE);
        prefixes.put("657", TelecomOperator.ORANGE);
        prefixes.put("658", TelecomOperator.ORANGE);
        prefixes.put("659", TelecomOperator.ORANGE);
        prefixes.put("62", TelecomOperator.CAMTEL);
        prefixes.put("66", TelecomOperator.NEXTTEL);
        prefixes.put("67", TelecomOperator.MTN);
        prefixes.put("69", TelecomOperator.ORANGE);
        return Collections.unmodifiableMap(prefixes);
    }
}
