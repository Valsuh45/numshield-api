# Contributing to NumShield

## Development environment

NumShield requires JDK 21. The Maven wrapper is the supported build entry point, so a separate Maven installation is not required. Docker is optional for validating the container image.

Maintainers should clone the canonical SSH remote:

```bash
git clone git@github.com:Valsuh45/numshield-api.git
cd numshield-api
git remote -v
```

The `origin` fetch and push URLs should both be `git@github.com:Valsuh45/numshield-api.git`.

## Branch and pull-request flow

- `develop` is the default integration branch. Start feature, fix, documentation, and maintenance branches from an up-to-date `develop`.
- Open pull requests into `develop`. Keep each pull request focused and include verification evidence.
- `main` is the release branch. Changes reach it through a release pull request from a `release/vX.Y.Z` branch; ordinary work does not target `main` directly.
- Do not force-push or delete `develop` or `main`. See [BRANCH_PROTECTION.md](BRANCH_PROTECTION.md) for enforced checks, the sole-maintainer review exception, and emergency recovery policy.

Example:

```bash
git switch develop
git pull --ff-only origin develop
git switch -c feat/short-description
./mvnw clean verify
git push -u origin feat/short-description
```

Before requesting review:

1. Add or update tests for behavioral changes.
2. Run `./mvnw clean verify` with JDK 21.
3. If packaging or runtime behavior changed, run `docker build -t numshield-api:local .`.
4. Update `openapi/openapi.yaml` when the HTTP contract changes.
5. Add a concise entry under `Unreleased` in `CHANGELOG.md` for user-visible changes.
6. Explain verification and compatibility impact in the pull-request description.

On Windows, use `mvnw.cmd clean verify` instead of `./mvnw clean verify`.

## Compatibility

The version and compatibility rules are defined in [RELEASING.md](RELEASING.md). In particular, a change that removes or renames a field, changes its meaning or type, removes an accepted input, or changes established HTTP behavior requires a new major API version and path.
