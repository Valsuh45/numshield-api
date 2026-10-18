package com.numshield.numshield_api.operator;

import java.util.Comparator;
import java.util.List;

public final class TelecomOperatorDetector {

    private static final String CAMEROON_COUNTRY_CODE = "+237";

    private TelecomOperatorDetector() {
    }

    public static TelecomOperator detect(String normalizedNumber) {
        if (normalizedNumber == null || !normalizedNumber.matches("^\\+2376\\d{8}$")) {
            throw new IllegalArgumentException("Operator detection requires a normalized Cameroon mobile number");
        }

        String nationalNumber = normalizedNumber.substring(CAMEROON_COUNTRY_CODE.length());
        return matchPrefix(nationalNumber, OperatorAllocationDataset.ALLOCATIONS);
    }

    static TelecomOperator matchPrefix(String nationalNumber, List<OperatorAllocationDataset.Allocation> allocations) {
        return allocations.stream()
                .sorted(Comparator.comparingInt((OperatorAllocationDataset.Allocation allocation) -> allocation.prefix().length())
                        .reversed())
                .filter(allocation -> nationalNumber.startsWith(allocation.prefix()))
                .map(OperatorAllocationDataset.Allocation::operator)
                .findFirst()
                .orElse(TelecomOperator.UNKNOWN);
    }
}
