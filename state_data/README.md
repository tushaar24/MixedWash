# State Management System

## Overview

The State Management System provides a structured approach to maintain knowledge about application components, features, and architecture. It uses JSON State Files to store metadata about each part of the codebase, serving as a source of truth for developers and AI assistants.

## Purpose

1. **Project State Representation**: Maintain accurate and up-to-date information about the codebase structure
2. **Code Consistency**: Ensure new code follows established patterns and architecture
3. **Developer Guidance**: Provide clear rules and guidelines for each component
4. **Reduce Onboarding Time**: Help new developers understand the project structure quickly
5. **AI Assistant Integration**: Enable AI tools to provide accurate and contextual assistance

## State System Structure

```
state_data/
  ├── README.md                     # This file - documentation
  ├── rules.json                    # Rules for code generation and state management
  ├── state-schema.json             # JSON schema for validation
  ├── sourceset_*.json              # Source set states
  ├── core_*.json                   # Core module states
  └── feature_*.json                # Feature states
```

## File Naming Convention

All state files follow a strict naming convention:
- `sourceset_<sourceSetName>.json` for source set states
- `core_<coreName>.json` for core module states
- `feature_<featureName>.json` for feature states

## State File Structure

Each state file contains:
- Basic information (path, name, type, purpose)
- Last updated information (date and commit hash)
- Change log of historical modifications
- Architecture description
- Component details with paths to source files
- Dependencies and integration points
- Manipulation guidelines

Required fields are: `path`, `name`, `type`, `purpose`, `lastUpdated`, and `change_log`.

## Commands

1. **Create**: `state create [state_file_names...]`
   - Creates new state files for specified components
   - Extracts information directly from code

2. **Update**: `state update [state_file_names...]` 
   - Updates specified state files based on code changes
   - If no arguments are provided, updates all state files
   - Adds entries to the change_log
   - Updates commit hash and date

3. **Defragment**: `state defragment-changes [state_file_names...]`
   - Consolidates change_log entries older than the 10 most recent
   - Creates summary descriptions for consolidated ranges

## Change Log Management

The `change_log` tracks historical changes:
- New entry added on each update with meaningful summary
- Includes commit hash and date
- Most recent changes appear at the top

## Best Practices

1. **Code-Based Information Only**: No assumptions or speculations
2. **Deterministic Updates**: Same code should result in same state
3. **Focus on Architecture**: Emphasize patterns and relationships
4. **Regular Updates**: Keep state in sync with code changes
5. **Path Accuracy**: Ensure paths reflect actual component locations
6. **Component Paths**: Include relative paths to component files
7. **Meaningful Change Logs**: Write descriptive summaries for changes