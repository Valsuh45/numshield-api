package com.numshield.numshield_api.operator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TelecomOperatorDetectorTest {

    @ParameterizedTest
    @CsvSource({
            "+237650123456, UNKNOWN",
            "+237654123456, UNKNOWN",
            "+237655123456, UNKNOWN",
            "+237659123456, UNKNOWN",
            "+237660123456, NEXTTEL",
            "+237669123456, NEXTTEL",
            "+237670123456, MTN",
            "+237679123456, MTN",
            "+237680123456, UNKNOWN",
            "+237689123456, UNKNOWN",
            "+237690123456, ORANGE",
            "+237699123456, ORANGE",
            "+237620123456, UNKNOWN",
            "+237629123456, UNKNOWN",
    })
    void detectsDocumentedRangesAndLeavesUndocumentedRangesUnknown(String number, TelecomOperator expected) {
        assertEquals(expected, TelecomOperatorDetector.detect(number));
    }

    @Test
    void everyConfiguredAllocationHasProvenanceAndMatchesItsRange() {
        assertFalse(OperatorAllocationDataset.VERSION.isBlank());
        for (OperatorAllocationDataset.Allocation allocation : OperatorAllocationDataset.ALLOCATIONS) {
            int suffixLength = 9 - allocation.prefix().length();
            String firstNumber = allocation.prefix() + "0".repeat(suffixLength);
            String lastNumber = allocation.prefix() + "9".repeat(suffixLength);
            assertEquals(allocation.operator(), TelecomOperatorDetector.detect("+237" + firstNumber));
            assertEquals(allocation.operator(), TelecomOperatorDetector.detect("+237" + lastNumber));
            assertEquals(1, OperatorAllocationDataset.SOURCES.stream()
                    .filter(source -> source.id().equals(allocation.sourceId())).count());
        }
    }

    @Test
    void longestPrefixWinsRegardlessOfDeclarationOrder() {
        var broadToSpecific = new ArrayList<>(java.util.List.of(
                new OperatorAllocationDataset.Allocation("6", TelecomOperator.MTN, OperatorAllocationDataset.ITU_ART_2014.id()),
                new OperatorAllocationDataset.Allocation("66", TelecomOperator.NEXTTEL, OperatorAllocationDataset.ITU_ART_2014.id()),
                new OperatorAllocationDataset.Allocation("660", TelecomOperator.ORANGE, OperatorAllocationDataset.ITU_ART_2014.id())));

        assertEquals(TelecomOperator.ORANGE, TelecomOperatorDetector.matchPrefix("660123456", broadToSpecific));
        Collections.reverse(broadToSpecific);
        assertEquals(TelecomOperator.ORANGE, TelecomOperatorDetector.matchPrefix("660123456", broadToSpecific));
    }

    @Test
    void rejectsMalformedNormalizedInput() {
        assertThrows(IllegalArgumentException.class, () -> TelecomOperatorDetector.detect(null));
        assertThrows(IllegalArgumentException.class, () -> TelecomOperatorDetector.detect("690123456"));
        assertThrows(IllegalArgumentException.class, () -> TelecomOperatorDetector.detect("+237590123456"));
    }
}
