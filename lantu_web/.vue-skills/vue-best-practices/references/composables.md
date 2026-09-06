---
title: Composable Organization Patterns
impact: MEDIUM
impactDescription: Well-structured composables improve maintainability, reusability, and update performance
type: best-practice
tags: [vue3, composables, composition-api, code-organization, api-design, readonly, utilities]
---

# Composable Organization Patterns

**Impact: MEDIUM** - Treat composables as reusable, stateful building blocks.

## Task List

- Compose complex behavior from small, focused composables
- Use options objects for composables with multiple optional parameters
- Return readonly state when updates must flow through explicit actions
- Keep pure utility functions as plain utilities, not composables
- Organize composable and component code by feature concern

## Compose Composables from Smaller Primitives

```js
// composables/useEventListener.js
export function useEventListener(target, event, callback) {
  onMounted(() => toValue(target).addEventListener(event, callback))
  onUnmounted(() => toValue(target).removeEventListener(event, callback))
}

// composables/useMouse.js
export function useMouse() {
  const x = ref(0)
  const y = ref(0)
  useEventListener(window, 'mousemove', (e) => { x.value = e.pageX; y.value = e.pageY })
  return { x, y }
}
```

## Use Options Object Pattern

```ts
interface UseCounterOptions {
  initial?: number
  min?: number
  max?: number
  step?: number
}
export function useCounter(options: UseCounterOptions = {}) {
  const { initial = 0, min = -Infinity, max = Infinity, step = 1 } = options
  // ...
}
```

## Return Readonly State with Explicit Actions

```js
export function useCart() {
  const _items = ref([])
  const total = computed(() => _items.value.reduce(/* ... */))

  function addItem(product, quantity = 1) {
    const existing = _items.value.find(item => item.id === product.id)
    if (existing) { existing.quantity += quantity; return }
    _items.value.push({ ...product, quantity })
  }

  return { items: readonly(_items), total, addItem }
}
```

## Keep Utilities as Utilities (not composables)

```js
// utils/formatters.js - plain functions, not composables
export function formatDate(date) { return new Intl.DateTimeFormat('en-US').format(date) }
export function formatCurrency(amount) { return new Intl.NumberFormat(...).format(amount) }
```

## Organize by Feature Concern

```vue
<script setup>
import { useItems } from '@/composables/useItems'
import { useSearch } from '@/composables/useSearch'
import { useSelectionModal } from '@/composables/useSelectionModal'

const { items, loading, fetchItems } = useItems()
const { query, visibleItems } = useSearch(items)
const { selectedItem, isModalOpen, selectItem, closeModal } = useSelectionModal()
</script>
```
