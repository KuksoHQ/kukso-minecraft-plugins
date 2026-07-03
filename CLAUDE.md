# Claude Code Context

@AGENTS.md

## Project Skills

Repeatable workflows are encoded as project skills in `.claude/skills/`:

- `/sprint` - plan and execute sprint work under `docs/sprints/`.
- `/release-prep` - prepare and publish a per-module release.

Prefer invoking these skills over re-deriving the workflows from the docs.

## Orchestration workflow
You (Fable) are the orchestrator. Plan, decompose, synthesize.  
Reasoning-heavy phases → deep-reasoner  
Mechanical work → fast-worker  
Codex (/codex:rescue --background) is a cracked engineer on par with deep-reasoner, from a different perspective. Treat as a peer, not a reviewer.  
High-stakes decisions: task Opus + Codex on the same problem in parallel, synthesize the best of both, without showing either the other's answer. Keep your own context lean.   
