# Branch protection policy

The `develop` and `main` branches are protected through GitHub branch protection. The policy applies to repository administrators as well as contributors.

## Enforced settings

Both branches require:

- changes to arrive through a pull request;
- the branch to be current with its base before merge;
- successful `Build & Test (Java 21)` and `Package Verification` checks from GitHub Actions;
- all review conversations to be resolved; and
- force-pushes and branch deletion to remain disabled.

Direct pushes are not part of the normal contribution or release process. GitHub must report every required check as successful for the pull request's current commit before it can be merged.

## Sole-maintainer review exception

`Valsuh45` is currently the repository's only direct collaborator. Requiring one approving review would make every pull request unmergeable, so pull requests are required with zero mandatory approvals. Self-review does not count as independent approval.

When another trusted maintainer with review permission is added, increase the required approving review count for both protected branches from zero to one. Until then, the maintainer must wait for required CI, resolve every conversation, and record the rationale and verification evidence in each pull request.

## Normal branch flow

1. Branch from the current `develop` commit.
2. Open a pull request into `develop` and wait for the required checks.
3. Merge only after checks succeed and conversations are resolved.
4. At an approved release gate, create `release/vX.Y.Z` from `develop` and open a pull request into `main`.
5. Apply the same required checks to the release pull request; tag the accepted `main` commit only after merge.

The detailed versioning and release checklist are in [RELEASING.md](RELEASING.md).

## Emergency bypass

Protection includes administrators and has no routine bypass. If GitHub protection itself prevents an urgent security or recovery change and the pull-request path cannot be used, the repository owner may temporarily change only the blocking setting:

1. Open an issue or incident record describing the urgency, affected branch, intended commit, and why the normal pull-request path is unavailable.
2. Capture the current protection settings for both branches.
3. Temporarily relax only the setting that blocks recovery; do not enable force-pushes or branch deletion.
4. Apply the smallest reviewed change possible and run the Java 21 build and package checks.
5. Restore and verify the complete protection configuration before any other repository work.
6. Record the commit, verification results, duration of the exception, and follow-up actions in the incident record.

An emergency exception does not authorize rewriting history, deleting protected branches, or bypassing release approval.

## Verification

Protection can be audited with:

```bash
gh api repos/Valsuh45/numshield-api/branches/develop/protection
gh api repos/Valsuh45/numshield-api/branches/main/protection
```

A protection test is complete only when a pull request with a deliberately failing test reports a failed required check and GitHub reports the pull request as blocked. Remove the probe, rerun CI successfully, and retain the failed run URL as evidence.

On 2026-10-18, pull request [#28](https://github.com/Valsuh45/numshield-api/pull/28) included a temporary failing JUnit probe at commit `bf2c09f`. The required [Build & Test job failed](https://github.com/Valsuh45/numshield-api/actions/runs/39139511666/job/117710845688), Package Verification was skipped, and GitHub reported the ready pull request as `BLOCKED`. The probe was then removed so the final change could pass both required checks.
