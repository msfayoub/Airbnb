#!/usr/bin/env pwsh
# Quick Access Script for Airbnb Services

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Starting Airbnb Service Tunnels" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "This will open 3 tunnels. Keep this window open!" -ForegroundColor Yellow
Write-Host ""

# Start minikube tunnel for ingress in background
Write-Host "1. Starting Minikube Tunnel for Ingress..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Minikube Tunnel Running - Keep this window open!' -ForegroundColor Cyan; minikube tunnel"

Start-Sleep -Seconds 3

# Start Grafana service tunnel
Write-Host "2. Starting Grafana Tunnel..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Grafana Tunnel - Keep this window open!' -ForegroundColor Cyan; minikube service grafana -n monitoring"

Start-Sleep -Seconds 2

# Start Airbnb app service tunnel  
Write-Host "3. Starting Airbnb App Tunnel..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Airbnb App Tunnel - Keep this window open!' -ForegroundColor Cyan; minikube service airbnb-app"

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "All Tunnels Started!" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Green

Write-Host "Access URLs:" -ForegroundColor Yellow
Write-Host "  Airbnb App (Ingress): http://airbnb.local/home" -ForegroundColor Cyan
Write-Host "  Airbnb App (NodePort): Check Airbnb App tunnel window" -ForegroundColor Cyan
Write-Host "  Grafana:               Check Grafana tunnel window" -ForegroundColor Cyan
Write-Host ""
Write-Host "Login Credentials:" -ForegroundColor Yellow
Write-Host "  Airbnb: admin@admin.com / admin" -ForegroundColor White
Write-Host "  Grafana: admin / admin123" -ForegroundColor White
Write-Host ""
Write-Host "Note: Keep all PowerShell windows open to maintain tunnels!" -ForegroundColor Red
Write-Host "Press Ctrl+C in each window to stop the tunnel" -ForegroundColor Gray
Write-Host ""
