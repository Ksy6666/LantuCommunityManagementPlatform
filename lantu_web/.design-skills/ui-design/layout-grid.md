# Layout Grid
定义响应式布局网格系统。

## 网格结构
- **Columns**：4（手机）、8（平板）、12（桌面）
- **Gutters**：列间距（通常 16px、24px 或 32px）
- **Margins**：页面外边距（移动端 16px，桌面 24-48px）
- **Breakpoints**：布局适配点（如 375、768、1024、1440px）

## 网格类型
- **Column grid**：等列通用布局
- **Modular grid**：列+行组成模块
- **Baseline grid**：垂直节奏对齐（4px 或 8px）
- **Compound grid**：复杂布局的重叠网格

## 响应式行为
- Fluid：列按比例伸缩
- Fixed：最大宽度容器 + 居中内容
- Adaptive：每个断点不同布局
- Column dropping：小屏减少列数

## 常用模式
- Full-bleed：内容铺满视口
- Contained：最大宽度 + 边距
- Asymmetric：侧边栏 + 主内容
- Card grids：自动填充响应式卡片

## 最佳实践
- 使用一致的 gutters 和 margins
- 内容对齐网格，而非随意放置
- 在每个断点测试，而不仅是两端
- 记录网格规格供开发使用
- 允许有意打破网格以突出重点
