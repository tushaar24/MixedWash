# Exhaustive Requirements List for Mixed Wash State Management System

## Terminology and Naming

1. ✅ Use "state" instead of "context" to represent project state
2. ✅ Store all state files in `state_data` directory (renamed from `project_context`)
3. ✅ Use naming convention: `sourceset_<name>.json`, `core_<name>.json`, or `feature_<name>.json`
4. ✅ Names should match corresponding package or module names using lowercase with underscores

## File Structure and Schema

5. ✅ Implement JSON schema in `state-schema.json` for validation
6. ✅ Required fields: `path`, `name`, `type`, `purpose`, `lastUpdated`, `change_log`
7. ✅ Include `change_log` to track historical changes
8. ✅ Store only information derived directly from code (no assumptions)
9. ✅ Include detailed info for complex/novel implementations only
10. ✅ Keep state focused on specific component/feature
11. ✅ Include manipulation rules for maintaining components

## Version Control Integration

12. ✅ Track commit hashes in `lastUpdated.commit`
13. ✅ Record changes in `change_log` array
14. ✅ Format for change_log entries: commit_range, date, summary
15. ✅ Compare changes between commits for intelligent updates

## State Maintenance

20. ✅ Update file paths when component locations change
21. ✅ Update state during code changes
22. ✅ Maintain consistent state across changes
23. ✅ When adding entries to change_log, include commit hash and date
24. ✅ Preserve the 10 most recent change_log entries during defragmentation

## Integration with Firebender

25. ✅ `firebender.json` points to `state_data/rules.json` for rules
26. ✅ Read README and rules.json for every query
27. ✅ Maintain state across changes
28. ✅ Validate against schema
29. ✅ Extract rules from rules.json

## Deterministic Updates

30. ✅ Same code should result in same state
31. ✅ Analyze code changes between commits
32. ✅ Rules for identifying necessary updates
33. ✅ Derive information only from code
34. ✅ No assumptions or hallucinations

## Other Requirements

35. ✅ README explaining the state management system
36. ✅ Clear documentation for commands and usage
37. ✅ Explanation of file structures and purpose
38. ✅ Guidelines for maintaining state files