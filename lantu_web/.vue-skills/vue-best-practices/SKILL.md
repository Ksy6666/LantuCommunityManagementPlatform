---
name: vue-best-practices
description: MUST be used for Vue.js tasks. Strongly recommends Composition API with `<script setup>` and TypeScript as the standard approach. Covers Vue 3, SSR, Volar, vue-tsc. Load for any Vue, .vue files, Vue Router, Pinia, or Vite with Vue work. ALWAYS use Composition API unless the project explicitly requires Options API.
license: MIT
metadata:
  author: github.com/vuejs-ai
  version: "18.0.0"
---

# Vue Best Practices Workflow

Use this skill as an instruction set. Follow the workflow in order unless the user explicitly asks for a different order.

## Core Principles
- **Keep state predictable:** one source of truth, derive everything else.
- **Make data flow explicit:** Props down, Events up for most cases.
- **Favor small, focused components:** easier to test, reuse, and maintain.
- **Avoid unnecessary re-renders:** use computed properties and watchers wisely.
- **Readability counts:** write clear, self-documenting code.

## 1) Confirm architecture before coding (required)

- Default stack: Vue 3 + Composition API + `<script setup lang="ts">`.
- If the project explicitly uses Options API, load `vue-options-api-best-practices` skill if available.
- If the project explicitly uses JSX, load `vue-jsx-best-practices` skill if available.

### 1.1 Must-read core references (required)

- Before implementing any Vue task, make sure to read and apply these core references:
  - `references/reactivity.md`
  - `references/sfc.md`
  - `references/component-data-flow.md`
  - `references/composables.md`
- Keep these references in active working context for the entire task.

### 1.2 Plan component boundaries before coding (required)

Create a brief component map before implementation for any non-trivial feature.

- Define each component's single responsibility in one sentence.
- Keep entry/root and route-level view components as composition surfaces by default.
- Move feature UI and feature logic out of entry/root/view components unless the task is intentionally a tiny single-file demo.
- Define props/emits contracts for each child component in the map.
- Prefer a feature folder layout (`components/<feature>/...`, `composables/use<Feature>.ts`) when adding more than one component.

## 2) Apply essential Vue foundations (required)

### Reactivity
- Keep source state minimal (`ref`/`reactive`), derive everything possible with `computed`.
- Use watchers for side effects if needed.
- Avoid recomputing expensive logic in templates.

### SFC structure and template safety
- Keep SFC sections in this order: `<script>` → `<template>` → `<style>`.
- Keep SFC responsibilities focused; split large components.
- Keep templates declarative; move branching/derivation to script.
- Apply Vue template safety rules (`v-html`, list rendering, conditional rendering choices).

### Keep components focused
Split a component when it has **more than one clear responsibility**.

- Prefer **smaller components + composables** over one "mega component"
- Move **UI sections** into child components (props in, events out).
- Move **state/side effects** into composables (`useXxx()`).
- Keep entry/root and route view components thin: app shell/layout, provider wiring, and feature composition.
- For CRUD/list features, split at least into: feature container, input/form, list/item, footer/actions.

### Component data flow
- Use props down, events up as the primary model.
- Use `v-model` only for true two-way component contracts.
- Use provide/inject only for deep-tree dependencies or shared context.
- Keep contracts explicit and typed with `defineProps`, `defineEmits`, and `InjectionKey` as needed.

### Composables
- Extract logic into composables when it is reused, stateful, or side-effect heavy.
- Keep composable APIs small, typed, and predictable.
- Separate feature logic from presentational components.

## 3) Consider optional features only when requirements call for them

- Slots → `references/component-slots.md`
- Fallthrough attributes → `references/component-fallthrough-attrs.md`
- KeepAlive → `references/component-keep-alive.md`
- Teleport → `references/component-teleport.md`
- Suspense → `references/component-suspense.md`
- Transition → `references/component-transition.md`
- TransitionGroup → `references/component-transition-group.md`
- Directives → `references/directives.md`
- Async components → `references/component-async.md`
- Render functions → `references/render-functions.md`
- Plugins → `references/plugins.md`
- State management → `references/state-management.md`

## 4) Performance optimization (post-functionality)

- Large list rendering → `references/perf-virtualize-large-lists.md`
- Static subtrees re-rendering → `references/perf-v-once-v-memo-directives.md`
- Over-abstraction in hot list paths → `references/perf-avoid-component-abstraction-in-lists.md`
- Expensive updates too often → `references/updated-hook-performance.md`

## 5) Final self-check

- Core behavior works and matches requirements.
- All must-read references were read and applied.
- Reactivity model is minimal and predictable.
- SFC structure and template rules are followed.
- Components are focused and well-factored.
- Data flow contracts are explicit and typed.
- Composables are used where reuse/complexity justifies them.
- Optional features are used only when requirements demand them.
- Performance changes were applied only after functionality was complete.
