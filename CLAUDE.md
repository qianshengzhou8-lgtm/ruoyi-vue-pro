# CLAUDE.md

Behavioral guidelines to reduce common LLM coding mistakes. Merge with project-specific instructions as needed.

**Tradeoff:** These guidelines bias toward caution over speed. For trivial tasks, use judgment.

## 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

Before implementing:
- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

## 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

## 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing code:
- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:
- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

## 4. Goal-Driven Execution

**Define success criteria. Loop until verified.**

Transform tasks into verifiable goals:
- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"
- "Refactor X" → "Ensure tests pass before and after"

For multi-step tasks, state a brief plan:
```
1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]
```

Strong success criteria let you loop independently. Weak criteria ("make it work") require constant clarification.

---

**These guidelines are working if:** fewer unnecessary changes in diffs, fewer rewrites due to overcomplication, and clarifying questions come before implementation rather than after mistakes.

## 5. Multi-Window Collaboration (Shared State)

When working across multiple sessions, agents, or terminals on the same project, use `PROJECT_STATE.md` as the single source of truth.

**Golden Rule:** If the task spans multiple sessions or requires coordination, you must sync state.

### Protocol

1. **Read on entry.** Every session starts by reading `PROJECT_STATE.md`. Know what's in progress, what's blocked, what's claimed.

2. **Claim before acting.** Update the file with your intent before starting a task:
   ```
   | TaskID | Owner | Status | Timestamp |
   |--------|-------|--------|-----------|
   | T001   | win-1 | doing  | 07-15 14:30 |
   ```

3. **Update on completion.** Mark done, append result. If blocked, say why.

4. **Check before write.** If `git status` shows conflicting changes from another session, stop and ask.

5. **Reality over file.** If `PROJECT_STATE.md` contradicts the code, trust the code, correct the file, and notify the user.

### Multi-step shared plans

For multi-session work, append a checklist to `PROJECT_STATE.md` at the start:
```markdown
# Shared Plan
- [ ] Task 1: [Description] (Session A)
- [ ] Task 2: [Description] (Session B)
```

### Init

Create `PROJECT_STATE.md` in the project root:
```markdown
# Project State
# Last Updated: YYYY-MM-DD

- [ ] 待定任务 | 未指派 | 准备就绪
```

### Trigger

"Read PROJECT_STATE.md, find the current progress, and pick up the next task assigned to this session."
