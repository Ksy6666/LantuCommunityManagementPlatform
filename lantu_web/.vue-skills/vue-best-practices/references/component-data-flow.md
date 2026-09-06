---
title: Component Data Flow Best Practices
impact: HIGH
impactDescription: Clear data flow between components prevents state bugs, stale UI, and brittle coupling
type: best-practice
tags: [vue3, props, emits, v-model, provide-inject, data-flow, typescript]
---

# Component Data Flow Best Practices

**Impact: HIGH** - Props down, events up. Blurring these boundaries leads to stale state, hidden coupling, and hard-to-debug UI.

## Task List

- Treat props as read-only inputs
- Use props/emit for component communication; reserve refs for imperative actions
- Emit events instead of mutating parent state directly
- Use `defineModel` for v-model in modern Vue (3.4+)
- Handle v-model modifiers deliberately in child components
- Use symbols for provide/inject keys to avoid props drilling (over ~3 layers)
- Keep mutations in the provider or expose explicit actions
- In TypeScript projects, prefer type-based `defineProps`, `defineEmits`, and `InjectionKey`

## Props: One-Way Data Down

Props are inputs. Do not mutate them in the child.

```ts
// ❌ BAD: mutating props
const props = defineProps({ count: Number })
props.count++

// ✅ GOOD: emit event to parent
const emit = defineEmits(['increment'])
```

## Prefer props/emit over component refs
```vue
<!-- ✅ GOOD: use events instead of refs for imperative access -->
<UserForm @submit="handleSubmit" />
```

## Emits: Explicit Events Up

Events do not bubble. Re-emit explicitly across component boundaries.

```vue
<!-- Child.vue -->
<script setup>
const emit = defineEmits(['saved'])
function onGrandchildSaved(payload) { emit('saved', payload) }
</script>
<template>
  <Grandchild @saved="onGrandchildSaved" />
</template>
```

Use kebab-case in templates, camelCase in script:
```vue
<script setup>
const emit = defineEmits(['updateUser'])
</script>
<template>
  <ProfileForm @update-user="emit('updateUser', $event)" />
</template>
```

## `v-model`: Predictable Two-Way Bindings

Use `defineModel` (Vue 3.4+):
```vue
<script setup>
const model = defineModel({ type: String })
</script>
<template>
  <input v-model="model" />
</template>
```

## Provide/Inject: Shared Context Without Prop Drilling

Keep mutations centralized in the provider:
```ts
// Provider
const theme = reactive({ dark: false })
const toggleTheme = () => { theme.dark = !theme.dark }
provide(themeKey, readonly(theme))
provide(themeActionsKey, { toggleTheme })

// Consumer
const theme = inject(themeKey)
const { toggleTheme } = inject(themeActionsKey)
```

Use symbols for keys:
```ts
export const themeKey: InjectionKey<Settings> = Symbol('theme')
```

## Use TypeScript Contracts

```vue
<script setup lang="ts">
interface Props { userId: string }
interface Emits { save: [payload: { id: string; draft: boolean }] }
const props = defineProps<Props>()
const emit = defineEmits<Emits>()
</script>
```
