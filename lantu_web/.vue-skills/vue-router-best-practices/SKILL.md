---
name: vue-router-best-practices
description: "Vue Router 4 patterns, navigation guards, route params, and route-component lifecycle interactions."
version: 1.0.0
license: MIT
author: github.com/vuejs-ai
---

Vue Router best practices, common gotchas, and navigation patterns.

### Navigation Guards
- Navigating between same route with different params → See [reference/router-beforeenter-no-param-trigger.md](reference/router-beforeenter-no-param-trigger.md)
- Accessing component instance in beforeRouteEnter guard → See [reference/router-beforerouteenter-no-this.md](reference/router-beforerouteenter-no-this.md)
- Navigation guard making API calls without awaiting → See [reference/router-guard-async-await-pattern.md](reference/router-guard-async-await-pattern.md)
- Users trapped in infinite redirect loops → See [reference/router-navigation-guard-infinite-loop.md](reference/router-navigation-guard-infinite-loop.md)
- Navigation guard using deprecated next() function → See [reference/router-navigation-guard-next-deprecated.md](reference/router-navigation-guard-next-deprecated.md)

### Route Lifecycle
- Stale data when navigating between same route → See [reference/router-param-change-no-lifecycle.md](reference/router-param-change-no-lifecycle.md)
- Event listeners persisting after component unmounts → See [reference/router-simple-routing-cleanup.md](reference/router-simple-routing-cleanup.md)

### Setup
- Building production single-page application → See [reference/router-use-vue-router-for-production.md](reference/router-use-vue-router-for-production.md)
