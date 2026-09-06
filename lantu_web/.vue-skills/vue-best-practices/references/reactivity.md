---
title: Reactivity Core Patterns (ref, reactive, shallowRef, computed, watch)
impact: MEDIUM
impactDescription: Clear reactivity choices keep state predictable and reduce unnecessary updates in Vue 3 apps
type: efficiency
tags: [vue3, reactivity, ref, reactive, shallowRef, computed, watch, watchEffect, external-state, best-practice]
---

# Reactivity Core Patterns (ref, reactive, shallowRef, computed, watch)

**Impact: MEDIUM** - Choose the right reactive primitive first, derive with `computed`, and use watchers only for side effects.

## Task List

- Declare reactive state correctly
  - Always use `shallowRef()` instead of `ref()` for primitive values
  - Choose the correct reactive declaration method for objects/arrays/map/set
- Follow best practices for `reactive`
  - Avoid destructuring from `reactive()` directly
  - Watch correctly for `reactive`
- Follow best practices for `computed`
  - Prefer `computed` over watcher-assigned derived refs
  - Keep filtered/sorted derivations out of templates
  - Use `computed` for reusable class/style logic
  - Keep computed getters pure (no side effects) and put side effects in watchers
- Follow best practices for watchers
  - Use `immediate: true` instead of duplicate initial calls
  - Clean up async effects for watchers

## Declare reactive state correctly

### Always use `shallowRef()` instead of `ref()` for primitive values

**Correct:**
```ts
import { shallowRef } from 'vue'
const count = shallowRef(0)
```

### Choose the correct declaration for objects/arrays/map/set

**Use `ref()`** when you often **replace** the entire value:
```ts
// Frequently reassigned state
const user = ref({ name: 'Alice', age: 30 })
user.value = { name: 'Bob', age: 25 } // ✅ triggers update
```

**Use `reactive()`** when you mainly **mutate properties**:
```ts
const state = reactive({ count: 0, user: { name: 'Alice' } })
state.count++ // ✅ reactive
state.user.name = 'Bob' // ✅ reactive
```

**Use `shallowRef()`** for opaque values (class instances, large data with immutable updates):
```ts
const user = shallowRef({ name: 'Alice', age: 30 })
user.value.age = 31 // ❌ not reactive
user.value = { name: 'Bob', age: 25 } // ✅ triggers update
```

## Best practices for `reactive`

### Avoid destructuring from `reactive()` directly
```ts
const state = reactive({ count: 0 })
const { count } = state // ❌ disconnected from reactivity
```

### Watch correctly for reactive
```ts
const state = reactive({ count: 0 })
// ❌ wrong: watch(state.count, ...)
watch(() => state.count, () => { /* ... */ }) // ✅ use getter
```

## Best practices for `computed`

### Prefer `computed` over watcher-assigned derived refs
```ts
// ✅ GOOD
const items = ref([{ price: 10 }, { price: 20 }])
const total = computed(() => items.value.reduce((sum, item) => sum + item.price, 0))
```

### Keep filtered/sorted derivations out of templates
```vue
<script setup>
const visibleItems = computed(() =>
  items.value.filter(item => item.active).sort((a, b) => a.name.localeCompare(b.name))
)
</script>
<template>
  <li v-for="item in visibleItems" :key="item.id">{{ item.name }}</li>
</template>
```

### Keep computed getters pure (no side effects)
Side effects belong in `watch()`, not `computed()`.

## Best practices for watchers

### Use `immediate: true` instead of duplicate initial calls
```ts
watch(userId, (id) => loadUser(id), { immediate: true })
```

### Clean up async effects
```ts
watch(query, async (q, _prev, onCleanup) => {
  const controller = new AbortController()
  onCleanup(() => controller.abort())
  const res = await fetch(`/api/search?q=${encodeURIComponent(q)}`, { signal: controller.signal })
  if (res.ok) results.value = await res.json()
})
```
