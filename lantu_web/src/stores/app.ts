import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAppStore = defineStore('app', () => {
  const siteName = ref('Open_Lantu')
  const siteSlogan = ref('学习 · 协作 · 成长')

  return {
    siteName,
    siteSlogan,
  }
})
