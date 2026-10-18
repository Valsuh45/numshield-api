package com.numshield.numshield_api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class BranchProtectionProbeTest {

    @Test
    void deliberateFailureProvesRequiredChecksBlockMerge() {
        fail("Temporary branch-protection verification probe");
    }
}
