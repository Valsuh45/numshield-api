# Security policy

## Supported versions

NumShield has not published `v1.0.0`. Security fixes currently target the latest commit on `develop`; there is no supported production release yet. This policy will list supported release lines when the first production version is published.

## Reporting a vulnerability

Use GitHub's private vulnerability reporting from the repository Security tab. Include the affected endpoint or component, reproduction steps, impact, and any suggested mitigation. Do not disclose suspected vulnerabilities, credentials, tokens, or submitted phone numbers in a public issue.

Use a public issue only for non-sensitive hardening suggestions that do not reveal an exploitable weakness. The repository owner, `Valsuh45`, owns security triage, remediation decisions, and disclosure coordination.

## Severity and response policy

GitHub advisory severity and CVSS are the starting point; exploitability, exposure, and data impact can raise or lower priority. These are triage targets for a solo-maintained free service, not an availability guarantee:

| Severity | Initial triage target | Remediation policy |
| --- | --- | --- |
| Critical | 1 calendar day | Stop a release or disable affected functionality; mitigate within 3 days where practical. |
| High | 3 calendar days | Block release and target remediation within 14 days. |
| Medium | 14 calendar days | Plan remediation within 30 days or record a time-bounded risk acceptance. |
| Low | 30 calendar days | Address in normal maintenance or record why no change is needed. |

Every accepted alert must have an owner, disposition, and supporting evidence. A risk acceptance records the affected version, rationale, compensating controls, approver, and review or expiry date. Critical and high alerts cannot be silently dismissed.

## Automated controls

As of 2026-10-18:

- Dependabot vulnerability alerts and security updates are enabled.
- `.github/dependabot.yml` checks Maven, GitHub Actions, and Docker dependencies every week.
- GitHub secret scanning and push protection are enabled.
- Private vulnerability reporting is enabled.
- CodeQL default setup scans Java/Kotlin and GitHub Actions with the default query suite. The initial setup run is recorded at <https://github.com/Valsuh45/numshield-api/actions/runs/39139820023>.
- Default GitHub Actions token permissions are read-only. Workflows declare `contents: read`; only the image-publishing job adds `packages: write`.

GitHub reported secret-scanning non-provider patterns and validity checks as disabled after an enablement request, so this repository does not rely on those optional features. Re-evaluate them when the account or repository capabilities change.

Dependabot and CodeQL findings are triaged under the severity policy above. Secret-scanning alerts require immediate credential revocation or rotation before code cleanup; deleting a leaked secret from the current branch is not sufficient.
