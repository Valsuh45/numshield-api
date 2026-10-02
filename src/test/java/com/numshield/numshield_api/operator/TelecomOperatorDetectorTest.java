package com.numshield.numshield_api.operator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TelecomOperatorDetectorTest {

    @ParameterizedTest
    @CsvSource({
            "+237650123456, MTN",
            "+237651123456, MTN",
            "+237652123456, MTN",
            "+237653123456, MTN",
            "+237654123456, MTN",
            "+237670123456, MTN",
            "+237655123456, ORANGE",
            "+237656123456, ORANGE",
            "+237657123456, ORANGE",
            "+237658123456, ORANGE",
            "+237659123456, ORANGE",
            "+237690123456, ORANGE",
            "+237660123456, NEXTTEL",
            "+237620123456, CAMTEL",
            "+237680123456, UNKNOWN"
    })
    void shouldDetectOperator(String number, TelecomOperator expected) {
        assertEquals(expected, TelecomOperatorDetector.detect(number));
    }
}
