import { createApp } from 'vue'
import { createPinia } from 'pinia'
import naive from 'naive-ui'

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
import router from './router'
import './style.css'

const app = createApp(App)

// 注册 Element Plus 图标（兼容 Naive UI 使用）
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(createPinia())
app.use(router)
app.use(naive)
app.mount('#app')
