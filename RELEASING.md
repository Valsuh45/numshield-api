# Release process

NumShield follows Semantic Versioning (`MAJOR.MINOR.PATCH`) for the application and its public API contract.

## Compatibility policy

- Increment `MAJOR` for incompatible HTTP contract or documented behavior changes. A new major API uses a new path such as `/api/v2`; an existing released major path remains stable for its documented support period.
- Increment `MINOR` for backward-compatible capabilities, including new endpoints or optional response fields.
- Increment `PATCH` for backward-compatible fixes that do not add required client behavior.

Removing or renaming a field, changing a field's type or meaning, removing an accepted request format, or changing established status/error semantics is breaking. Security fixes may require an exception, but the release notes must state the compatibility impact and migration path.

## Version mapping

The planned release number has four representations:

| Surface | Pre-release value | v1 release value |
| --- | --- | --- |
| Maven `project.version` | `1.0.0-SNAPSHOT` | `1.0.0` |
| Maintained and generated OpenAPI `info.version` | `1.0.0` | `1.0.0` |
| Git tag | None | `v1.0.0` |
| GHCR image | `develop`, `develop-<sha>` | `1.0.0`, `1.0`, `latest` |

The Maven base version, after removing `-SNAPSHOT`, must equal both OpenAPI versions. Git release tags add the `v` prefix. Container tags omit it. Branch and short-SHA image tags are development artifacts and are not releases.

## Release branches

- `develop` is the default integration branch.
- Create `release/vX.Y.Z` from the accepted `develop` commit when the corresponding release gate is complete.
- Open the release pull request from `release/vX.Y.Z` into `main`.
- After release, merge the released `main` commit back into `develop`, then set the next planned Maven `-SNAPSHOT` version.

## Changelog and release notes

Every pull request with user-visible impact adds an entry to the `Unreleased` section of `CHANGELOG.md`. Use `Added`, `Changed`, `Fixed`, `Deprecated`, `Removed`, or `Security` headings as appropriate.

During release preparation:

1. Rename `Unreleased` to `[X.Y.Z] - YYYY-MM-DD` and add a new empty `Unreleased` section.
2. Use that version's changelog section as the GitHub release notes, supplemented with upgrade instructions and known risks when needed.
3. Link the changelog version to the Git comparison for the preceding release.

## Release checklist

The initial `v1.0.0` publication is intentionally deferred until issue #26, the production launch gate. When that gate is approved:

1. Confirm the intended commit is on `develop` and all required CI, security, contract, and release-acceptance checks pass.
2. Create `release/v1.0.0`, change the POM version from `1.0.0-SNAPSHOT` to `1.0.0`, and confirm the maintained and generated OpenAPI versions remain `1.0.0`.
3. Finalize `CHANGELOG.md` and open the release pull request into `main`.
4. Confirm CI produces the executable JAR with JDK 21 and the package-verification artifact is downloadable.
5. Merge the approved release pull request into `main`.
6. Create an annotated `v1.0.0` tag on that exact merge commit and push the tag.
7. Confirm the publish workflow creates the `1.0.0`, `1.0`, and `latest` images from that tagged commit and record the immutable digest.
8. Publish the GitHub release from the finalized changelog and attach or link the verified artifacts.
9. Merge the release commit back into `develop` and set the next planned snapshot version.

Never move or reuse a published release tag. Correct a released defect with a new patch version.
