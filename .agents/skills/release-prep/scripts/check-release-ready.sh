#!/bin/bash
# Deterministic release-readiness check for one module.
# Usage: check-release-ready.sh <module> <version>
# Run from anywhere inside the repository.

set -u

MODULE="${1:-}"
VERSION="${2:-}"

if [ -z "$MODULE" ] || [ -z "$VERSION" ]; then
  echo "Usage: check-release-ready.sh <module> <version>"
  echo "Modules: lib, dialogs, dialogs-exp-config-addon, worlds, items"
  exit 1
fi

ROOT="$(git rev-parse --show-toplevel 2>/dev/null)"
if [ -z "$ROOT" ]; then
  echo "FAIL: not inside a git repository"
  exit 1
fi

ERRORS=0
fail() { echo "FAIL: $1"; ERRORS=$((ERRORS + 1)); }
pass() { echo "PASS: $1"; }

# 1. Module exists
MODULE_DIR="$ROOT/modules/$MODULE"
if [ -d "$MODULE_DIR" ]; then
  pass "module directory exists: modules/$MODULE"
else
  fail "unknown module '$MODULE' (no modules/$MODULE directory)"
fi

# 2. Version format: X.Y.Z with optional -alpha.N / -beta.N / -rc.N
if echo "$VERSION" | grep -Eq '^[0-9]+\.[0-9]+\.[0-9]+(-(alpha|beta|rc)\.[0-9]+)?$'; then
  pass "version format is valid: $VERSION"
else
  fail "invalid version '$VERSION' (expected X.Y.Z or X.Y.Z-alpha.N/-beta.N/-rc.N)"
fi

# 3. version.txt exists; report whether it already matches the target
VERSION_FILE="$MODULE_DIR/version.txt"
if [ -f "$VERSION_FILE" ]; then
  CURRENT="$(tr -d '[:space:]' < "$VERSION_FILE")"
  if [ "$CURRENT" = "$VERSION" ]; then
    pass "version.txt already set to $VERSION"
  else
    echo "INFO: version.txt is '$CURRENT'; bump to '$VERSION' in the release prep change"
  fi
else
  fail "missing $MODULE_DIR/version.txt"
fi

# 4. Tag must not already exist
TAG="kukso-minecraft-$MODULE-v$VERSION"
if git -C "$ROOT" rev-parse -q --verify "refs/tags/$TAG" >/dev/null; then
  fail "tag $TAG already exists"
else
  pass "tag $TAG is available"
fi

# 5. Changelog: release section present, or Unreleased entries pending the move
CHANGELOG="$ROOT/CHANGELOG.md"
if [ -f "$CHANGELOG" ]; then
  if grep -q "\[$TAG\]" "$CHANGELOG"; then
    pass "CHANGELOG.md has release section [$TAG]"
  elif grep -q '## \[Unreleased\]' "$CHANGELOG"; then
    echo "INFO: no [$TAG] section yet; move this module's entries out of Unreleased during release prep"
  else
    fail "CHANGELOG.md has neither an [Unreleased] section nor a [$TAG] section"
  fi
else
  fail "missing CHANGELOG.md"
fi

echo
if [ "$ERRORS" -eq 0 ]; then
  echo "READY: no blocking problems found for $MODULE v$VERSION"
else
  echo "NOT READY: $ERRORS blocking problem(s) found"
  exit 1
fi
