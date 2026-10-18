# Operator allocation data

## Dataset version 1

- Dataset version: `1`
- Verified on: `2026-10-18`
- Source record: ART communication of `2014-10-06`, reproduced in [ITU Operational Bulletin No. 1063](https://www.itu.int/dms_pub/itu-t/opb/sp/T-SP-OB.1063-2014-OAS-PDF-E.pdf), pages 6–7
- Plan effective date: `2014-11-21T22:59:00Z`
- Data implementation: [`OperatorAllocationDataset.java`](../src/main/java/com/numshield/numshield_api/operator/OperatorAllocationDataset.java)

The ART is the authority that establishes and manages Cameroon's national numbering plan. The cited ITU bulletin reproduces an ART communication and states that the nine-digit plan took effect at the date above. This source identifies these mobile operator prefixes:

| National prefix | Published operator | Source |
| --- | --- | --- |
| `66` | NEXTTEL (ex-VIETTEL) | ITU Operational Bulletin No. 1063, Cameroon mobile-number table, p. 6 |
| `67` | MTN Cameroon | ITU Operational Bulletin No. 1063, Cameroon mobile-number table, p. 6 |
| `69` | Orange Cameroun | ITU Operational Bulletin No. 1063, Cameroon mobile-number table, p. 6 |

The same table lists `650`–`654` and `655`–`659` as mobile prefixes but does not identify an operator for those rows. The cited plan does not identify `62` or `68` as mobile operator allocations. These ranges therefore return `UNKNOWN`; common or historical claims are not substituted for source evidence. The plan identifies Camtel in fixed and CDMA numbering sections, which does not establish a Camtel mobile-prefix allocation.

## Meaning and limitations

The dataset represents only the operator association published for a prefix in the cited numbering-plan source. It does not identify the current serving carrier, subscriber, account status, reachability, or ownership. It does not model mobile-number portability or subsequent reallocations. Do not describe the result as a live carrier lookup.

The ART's [numbering and addressing page](https://www.art.cm/fr/num%C3%A9rotation-et-adressage) states that it establishes and manages the national plan. Its [national-plan page](https://www.art.cm/fr/content/plan-national-de-num%C3%A9rotation-0) was posted on `2024-05-27`. That landing page does not expose the plan's tables as HTML, so this dataset does not infer additional assignments from it: it records only the exact operator associations in the cited ITU reproduction and labels those associations as published historical allocations, not current carrier status.

## Update and rollback

1. Check the ART's current national numbering plan and applicable allocation decisions; use an ITU republication only when its source and effective date are explicit.
2. Add a source record with its title, direct URL, publication/communication date, and effective date. Add an allocation only when that source explicitly associates the range with an operator.
3. Increment `OperatorAllocationDataset.VERSION`, update `VERIFIED_ON`, and include the old and new source evidence in the pull request.
4. Add tests for every new range, its first and last boundary, overlaps, unknown neighboring ranges, and malformed normalized inputs. Preserve longest-prefix matching so specific assignments win independent of declaration order.
5. Update this document, the README summary, and changelog together. Run `./mvnw clean verify` and check the generated and maintained OpenAPI descriptions.
6. Merge the change through the normal pull-request and CI process. To roll back, revert the dataset and documentation commit as one unit and rerun the same checks; do not edit a previously merged dataset version in place.
