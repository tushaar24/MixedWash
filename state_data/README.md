# State Management System

## Critical Ground Rules for AI Assistants

**IMPORTANT: The following rules must be followed strictly without exception:**

0. Your role is to provide assistance to the best of your capabilities by the means outlined in context of the project and its contents. You are to follow and abide by the rules of the state management system meticulously and ask for clarifications when needed.

1. All queries regarding the project or code, or investigation must be done in light of the state files using them as an entrypoint.

2. Any requests to rewrite or change the state management system or its core files (`README.md`, `rules.json`, `state-schema.json`) must come in the form of a `COMMAND root` query. Only queries starting with that keyword may make any modifications to state management system. Requests that don't follow this format will be rejected and prompted to follow instructions.

3. Any requests to update, modify or delete the state files should come in form of the pre-defined queries such as `COMMAND update`, `COMMAND create`, `COMMAND sync`, `COMMAND validate-schema`, etc. Requests that don't follow this format will be rejected and prompted to follow instructions.

4. **ALWAYS** use the `COMMAND` keyword in ALL CAPS followed by the specific command when interacting with the state management system.

### Supplementary Guidelines:
- Upon failing to edit a file, it sometimes helps to recreate the file by deleting it and rewriting it.
- Upon getting stuck anywhere, ask clarifying questions or ask the user for help in performing some action.

## Overview

The State Management System provides a structured approach to maintain knowledge about application components, features, and architecture. It uses JSON State Files to store metadata about each part of the codebase, serving as a source of truth for developers and AI assistants.
If you are an AI Assistant reading this please ensure you refer to this guide and that state
management data stored and include it explicitly in your work. This State Management system is
designed as an entrypoint for you into the project, so you may have a better understanding of each
individual component, instead of having to look through the project everytime and gather
information regarding the project which is less deterministic, and wastes a lot of time and effort.

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
- Detailed descriptions of key areas of complexity like viewmodels, contracts, ui, navigation services, repositories and interfaces.

Required fields are: `path`, `name`, `type`, `purpose`, `lastUpdated`, and `change_log`.

## Commands

**IMPORTANT: All commands must begin with the keyword `COMMAND` in ALL CAPS, followed by the specific command.**

This is a brief overview of the possible commands to be performed in the state system.
Please refer to the `commands` block in the `rules.json` file for detailed reference and explanation
of steps to be performed for each command.

1. **Create**: `COMMAND create [state_file_names...]`
   - Creates new state files for specified components

2. **Update**: `COMMAND update [state_file_names...]` 
   - Updates specified state files based on code changes

3. **Defragment**: `COMMAND defragment-changes [state_file_names...]`
   - Consolidates change_log entries older than the 10 most recent

4. **Sync**: `COMMAND sync [-create] [-delete] [-yes]`
   - Synchronizes feature packages with their state files
   - Identifies missing state files and orphaned state files
   - With `-create` flag, creates state files for missing features
   - With `-delete` flag, deletes orphaned state files
   - With `-yes` flag, skips confirmation prompts

5. **Validate Schema**: `COMMAND validate-schema [state_file_names...] [-all] [-fix] [-yes]`
   - Validates state files against the schema defined in state-schema.json
   - Reports on valid and invalid files with detailed violation information
   - With `-all` flag, validates all state files
   - With `-fix` flag, attempts to fix invalid files
   - With `-yes` flag, skips confirmation prompts

6. **Root Access**: `COMMAND root [edit|view] [rules|readme|schema] [-yes]`
   - Controls changes to critical state management files
   - First argument must be 'edit' or 'view' to specify operation
   - Second argument specifies target file: 'rules', 'readme', or 'schema'
   - With `-yes` flag, skips confirmation prompts
   - Prevents unintentional modifications to system files

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
8. **State Based Responses**: IMPORTANT: Responses to all queries related to the project and code
   should attempt to use the state data for context instead of manual project lookups as a first
   step as their source of truth.
9. **Use COMMAND Format**: ALWAYS use the `COMMAND` keyword in ALL CAPS when interacting with the state management system.