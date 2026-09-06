---
name: animal-island-ui-style
description: >
  使用 animal-island-ui 设计风格创建 UI 界面或组件（兼容 Vue/React 项目）。
  动物森友会风格：温暖大地色系 + 大圆角 pill 形 + 游戏按键立体感 + 柔和动效。
---

# Animal Island UI 设计风格指南 (Vue 适配版)

## 概述

设计语言核心：**温暖大地色系 + 大圆角 pill 形 + 游戏按键立体感 + 柔和动效 + 有机不规则形状**。

---

## 1. Design Tokens

### 色彩系统

```
主色（薄荷青绿）
  primary:        #19c8b9
  primary-hover:  #3dd4c6
  primary-active: #11a89b
  primary-bg:     #e6f9f6

文字（温暖棕色系）
  text:           #794f27  (header/sidebar)
  text-body:      #725d42  (正文)
  text-secondary: #9f927d  (次级)
  text-muted:     #8a7b66  (浅棕)
  text-disabled:  #c4b89e  (禁用)

边框
  border:         #9f927d
  border-light:   #c4b89e
  border-hover:   #a89878

背景（奶油米白）
  bg:             #f8f8f0  (主背景)
  bg-content:     rgb(247, 243, 223) (卡片/内容区)
  bg-secondary:   #f0e8d8
  bg-disabled:    #f0ece2

状态色
  success:        #6fba2c
  success-active: #5a9e1e
  warning:        #f5c31c
  warning-active: #dba90e
  error:          #e05a5a
  error-active:   #c94444

游戏特殊色
  focus-yellow:       #ffcc00   (输入框焦点)
  focus-yellow-dark:  #e0b800   (焦点阴影)
  shadow-btn:         #bdaea0   (按钮 3D 阴影)
  shadow-input:       #d4c9b4   (输入框 3D 阴影)
  shadow-switch-on:   #5a9e1e   (Switch 开启阴影)
```

### 字体

```css
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;500;600;700;800;900&family=Noto+Sans+SC:wght@400;500;700&family=Zen+Maru+Gothic:wght@400;500;700&display=swap');

font-family: Nunito, 'Noto Sans SC', 'Zen Maru Gothic',
  -apple-system, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
```

字重：正文 500 / 按钮标题 600-700 / 数字强调 900 / placeholder 400

### 间距 / 圆角

```
间距: xs=4px  sm=8px  md=12px  lg=16px  xl=24px  xxl=32px
圆角: sm=12px  base=18px  lg=24px  pill=50px（按钮/输入框）
边框: 默认 2px solid
```

### 3D 阴影（核心特征）

```css
/* 按钮 */
box-shadow: 0 5px 0 0 #bdaea0;   /* 默认 */
box-shadow: 0 6px 0 0 #bdaea0;   /* hover */
box-shadow: 0 1px 0 0 #bdaea0;   /* active (按下) */

/* 卡片 */
box-shadow: 0 4px 10px rgba(107, 92, 67, 0.42);  /* 默认阴影 */
box-shadow: 0 8px 24px rgba(61, 52, 40, 0.14);   /* 较大阴影 */
```

### 动效

```css
transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);

/* Hover 上浮 */
transform: translateY(-1px);  /* 按钮/输入框 */
transform: translateY(-4px);  /* 卡片 */

/* Active 下压 */
transform: translateY(2px);   /* 按钮按下 */
```

---

## 2. 设计铁律

1. **颜色**：大地棕色系文字 + 薄荷青绿主色 + 奶油米白背景，禁止纯黑/冷灰
2. **圆角**：最小 12px；按钮/输入框必须 50px pill 形
3. **立体感**：所有可点击元素加底部厚阴影，hover 上浮，active 下压（游戏按键手感）
4. **字体**：Nunito 圆体（Google Fonts），按钮/标题 weight 600+，不使用细体
5. **动效**：过渡 0.25s，缓动 cubic-bezier(0.4, 0, 0.2, 1)
6. **焦点**：输入框用黄色 #ffcc00，按钮用青绿 #19c8b9，绝不用蓝色
7. **禁止**：直角矩形交互元素、纯黑文字 #000、冷蓝色调、扁平无阴影设计

---

## 3. 页面/容器样式

### Card（卡片容器）
```css
background: rgb(247, 243, 223);
border-radius: 20px;
padding: 16px 24px;
color: #725d42;
font-weight: 500;
box-shadow: 0 4px 10px rgba(107, 92, 67, 0.42);
transition: all 0.3s ease;
&:hover { transform: translateY(-4px); }
```

### 页面背景
```css
background: #f8f8f0;
color: #725d42;
```

### Info Row（信息行）
```css
display: flex;
justify-content: space-between;
align-items: center;
padding: 14px 0;
border-bottom: 2px solid rgba(159, 146, 125, 0.2);
color: #725d42;
```

### Button pill 风格
```css
border-radius: 50px;
font-weight: 600;
letter-spacing: 0.02em;
line-height: 1;
box-shadow: 0 5px 0 0 #bdaea0;
&:hover { transform: translateY(-1px); box-shadow: 0 6px 0 0 #bdaea0; }
&:active { transform: translateY(2px); box-shadow: 0 1px 0 0 #bdaea0; }
```

---

## 4. CSS Variables 模板（不依赖组件库时使用）

```css
:root {
  --animal-font: Nunito, 'Noto Sans SC', 'Zen Maru Gothic', -apple-system, 'PingFang SC', sans-serif;
  --animal-primary: #19c8b9;
  --animal-primary-hover: #3dd4c6;
  --animal-primary-active: #11a89b;
  --animal-text: #794f27;
  --animal-text-body: #725d42;
  --animal-text-secondary: #9f927d;
  --animal-text-muted: #8a7b66;
  --animal-text-disabled: #c4b89e;
  --animal-bg: #f8f8f0;
  --animal-bg-content: rgb(247, 243, 223);
  --animal-border: #c4b89e;
  --animal-border-hover: #a89878;
  --animal-radius-sm: 12px;
  --animal-radius: 18px;
  --animal-radius-lg: 24px;
  --animal-radius-pill: 50px;
  --animal-shadow-btn: #bdaea0;
  --animal-shadow-input: #d4c9b4;
  --animal-focus-yellow: #ffcc00;
  --animal-success: #6fba2c;
  --animal-warning: #f5c31c;
  --animal-error: #e05a5a;
  --animal-ease: cubic-bezier(0.4, 0, 0.2, 1);
}
```

---

## 5. NookPhone 调色板 (Card 配色参考)

| 名称 | 背景色 | 文字色 |
|---|---|---|
| default | rgb(247,243,223) | #725d42 |
| app-pink | #f8a6b2 | #fff |
| purple | #b77dee | #fff |
| app-blue | #889df0 | #fff |
| app-yellow | #f7cd67 | #725d42 |
| app-orange | #e59266 | #fff |
| app-teal | #82d5bb | #fff |
| app-green | #8ac68a | #fff |
| app-red | #fc736d | #fff |
| lime-green | #d1da49 | #3d5a1a |
| yellow-green | #ecdf52 | #725d42 |
| brown | #9a835a | #fff |
| warm-peach-pink | #e18c6f | #fff |
