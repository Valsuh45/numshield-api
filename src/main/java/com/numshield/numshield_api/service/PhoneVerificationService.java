package com.numshield.numshield_api.service;

import com.numshield.numshield_api.dto.VerificationResponse;
import com.numshield.numshield_api.operator.TelecomOperator;
import com.numshield.numshield_api.operator.TelecomOperatorDetector;
import com.numshield.numshield_api.util.CameroonPhoneNumberNormalizer;
import com.numshield.numshield_api.util.CameroonPhoneNumberValidator;
import org.springframework.stereotype.Service;

@Service
public class PhoneVerificationService {

    public VerificationResponse verify(String phoneNumber) {
        String normalized = CameroonPhoneNumberNormalizer.normalize(phoneNumber);
        CameroonPhoneNumberValidator.validate(normalized);
        TelecomOperator operator = TelecomOperatorDetector.detect(normalized);

        return new VerificationResponse(phoneNumber, normalized, true, operator, "237", "CM");
    }
}
