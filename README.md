# ProxyFox Command

A simple, yet extendable command parsing library for Kotlin

## Note for maintainers:

Any changes in regard to parsing that may propagate to the end user is to be treated as a bump in the minor version
rather than the patch version.

This is to say:

- If string parsing is changed, minor must be incremented (i.e. `1.3` -> `1.4`.)
- If purely only the logic is changed and the end semantics has not changed as a result, patch may be incremented.