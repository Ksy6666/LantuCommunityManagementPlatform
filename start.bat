@echo off
chcp 65001 >nul
title Open_Lantu 开发服务器

echo ========================================
echo   Open_Lantu - 启动开发服务器
echo ========================================
echo.

cd /d "%~dp0\lantu_web"

echo 正在启动 Vite 开发服务器...
echo.
echo 服务器启动后，浏览器访问: http://localhost:5173
echo 按 Ctrl+C 即可停止服务器
echo.

npm run dev

echo.
echo 服务器已停止。
pause
