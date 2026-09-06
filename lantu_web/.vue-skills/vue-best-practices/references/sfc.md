---
title: Single-File Component Structure, Styling, and Template Patterns
impact: MEDIUM
impactDescription: Consistent SFC structure and styling choices improve maintainability
type: best-practice
tags: [vue3, sfc, scoped-css, styles, template, v-html, v-for, computed, v-if, v-show]
---

# Single-File Component Structure, Styling, and Template Patterns

## Task List

- Use `.vue` SFCs instead of separate `.js`/`.ts` and `.css` files for components
- Colocate template, script, and styles in the same SFC by default
- Use PascalCase for component names in templates and filenames
- Prefer component-scoped styles
- Prefer class selectors (not element selectors) in scoped CSS for performance
- Access DOM/component refs with `useTemplateRef()` in Vue 3.5+
- Use camelCase keys in `:style` bindings
- Use `v-for` and `v-if` correctly
- Never use `v-html` with untrusted/user-provided content
- Choose `v-if` vs `v-show` based on toggle frequency

## Colocate template, script, and styles

```vue
<!-- components/UserCard.vue -->
<script setup>
import { computed } from 'vue'
const props = defineProps({ user: { type: Object, required: true } })
const displayName = computed(() => `${props.user.firstName} ${props.user.lastName}`)
</script>

<template>
  <div class="user-card">
    <h3 class="name">{{ displayName }}</h3>
  </div>
</template>

<style scoped>
.user-card { padding: 1rem; }
.name { margin: 0; }
</style>
```

## Use PascalCase for component names
```vue
<script setup>
import UserProfile from './UserProfile.vue'  // ✅
</script>
<template>
  <UserProfile :user="currentUser" />  <!-- ✅ -->
</template>
```

## Best practices for `<style>` block

### Prefer component-scoped styles
- Use `<style scoped>` for component styles
- Keep global CSS in a dedicated file (e.g. `src/assets/main.css`)
- Use `:deep()` sparingly

### Use class selectors in scoped CSS (not element selectors)
```vue
<!-- GOOD: use class selectors for performance -->
<template>
  <article class="article">
    <h1 class="article-title">{{ title }}</h1>
  </article>
</template>
<style scoped>
.article { max-width: 800px; }
.article-title { font-size: 2rem; }
</style>
```

## Access DOM refs with `useTemplateRef()` (Vue 3.5+)
```vue
<script setup lang="ts">
import { onMounted, useTemplateRef } from 'vue'
const inputRef = useTemplateRef<HTMLInputElement>('input')
onMounted(() => { inputRef.value?.focus() })
</script>
<template>
  <input ref="input" />
</template>
```

## Use camelCase in `:style` bindings
```vue
<div :style="{ fontSize: fontSize + 'px', backgroundColor: bg }">
```

## Use `v-for` and `v-if` correctly

- Always provide a stable `:key` (primitive keys, never objects)
- **Never** use `v-if` and `v-for` on the same element — filter with computed instead

## Never render untrusted HTML with `v-html`
Use `{{ }}` for user content, or sanitize with DOMPurify before using `v-html`.

## Choose `v-if` vs `v-show`
- `v-show`: frequent toggles (keeps in DOM, toggles display)
- `v-if`: rare conditions (lazy render)
