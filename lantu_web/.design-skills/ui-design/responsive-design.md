# Responsive Design
设计适应各种屏幕和输入方式的界面。

## 响应式策略
- **Fluid**：百分比宽度，范围内灵活
- **Adaptive**：特定断点不同布局
- **Mobile-first**：从小屏开始，向上增强
- **Content-first**：让内容需求驱动断点

## 常用断点
- Small：375-639px（手机）
- Medium：640-1023px（平板）
- Large：1024-1439px（笔记本）
- Extra large：1440px+（桌面）

## 响应式模式
- Column drop：小屏减少列数
- Reflow：水平元素堆叠为垂直
- Off-canvas：次要内容藏在切换按钮后
- Priority+：显示最重要的，超出部分收起

## 输入方式适配
- Touch：最小 44px 触控目标，手势支持
- Mouse：悬停状态，精确定位
- Keyboard：焦点指示器，逻辑 Tab 顺序
- Voice：清晰标签，逻辑结构

## 最佳实践
- 为内容设计，而非设备
- 在真实设备上测试
- 考虑横竖屏
- 考虑慢速连接
- 在每个断点用无障碍工具测试
