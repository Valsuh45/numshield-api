package com.numshield.numshield_api.operator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Versioned source data for published Cameroon mobile prefix allocations.
 */
public final class OperatorAllocationDataset {

    public static final String VERSION = "1";
    public static final LocalDate VERIFIED_ON = LocalDate.of(2026, 10, 18);

    public static final Source ITU_ART_2014 = new Source(
            "ITU-OB-1063-CM-2014",
            "Cameroon National Numbering Plan, ART communication of 6 October 2014",
            "https://www.itu.int/dms_pub/itu-t/opb/sp/T-SP-OB.1063-2014-OAS-PDF-E.pdf",
            LocalDate.of(2014, 10, 6),
            Instant.parse("2014-11-21T22:59:00Z"));

    public static final List<Source> SOURCES = List.of(ITU_ART_2014);

    public static final List<Allocation> ALLOCATIONS = List.of(
            new Allocation("66", TelecomOperator.NEXTTEL, ITU_ART_2014.id()),
            new Allocation("67", TelecomOperator.MTN, ITU_ART_2014.id()),
            new Allocation("69", TelecomOperator.ORANGE, ITU_ART_2014.id()));

    private OperatorAllocationDataset() {
    }

    public record Source(String id, String title, String url, LocalDate communicationDate, Instant effectiveFrom) {
    }

    public record Allocation(String prefix, TelecomOperator operator, String sourceId) {
    }
}
