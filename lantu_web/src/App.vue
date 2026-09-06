<script setup lang="ts">
import { zhCN, dateZhCN } from 'naive-ui'
import { useScrollReveal } from './composables/useScrollReveal'
import { useAutoLogout } from './composables/useAutoLogout'
import AppLayout from './layouts/AppLayout.vue'

useScrollReveal()
useAutoLogout()
</script>

<template>
  <n-config-provider :locale="zhCN" :date-locale="dateZhCN">
    <AppLayout />
  </n-config-provider>
</template>
