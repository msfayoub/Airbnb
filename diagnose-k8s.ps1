#!/usr/bin/env pwsh
# Kubernetes Diagnostics Script for Airbnb Booking Application

$ErrorActionPreference = "Continue"

function Write-Section {
    param($Title)
    Write-Host "`n" + "=" * 70 -ForegroundColor Cyan
    Write-Host $Title -ForegroundColor Cyan
    Write-Host "=" * 70 -ForegroundColor Cyan
}

Write-Host "Airbnb Kubernetes Diagnostics" -ForegroundColor Green
Write-Host "Started at: $(Get-Date)" -ForegroundColor Gray

# 1. Minikube Status
Write-Section "1. Minikube Status"
minikube status

# 2. Cluster Info
Write-Section "2. Kubernetes Cluster Info"
kubectl cluster-info

# 3. All Resources
Write-Section "3. All Kubernetes Resources"
kubectl get all

# 4. MySQL Resources
Write-Section "4. MySQL Resources"
Write-Host "`nMySQL Deployment:" -ForegroundColor Yellow
kubectl get deployment mysql

Write-Host "`nMySQL Pods:" -ForegroundColor Yellow
kubectl get pods -l app=mysql

Write-Host "`nMySQL Service:" -ForegroundColor Yellow
kubectl get svc mysql

Write-Host "`nMySQL PVC:" -ForegroundColor Yellow
kubectl get pvc mysql-pvc

# 5. Application Resources
Write-Section "5. Application Resources"
Write-Host "`nApplication Deployment:" -ForegroundColor Yellow
kubectl get deployment airbnb-app

Write-Host "`nApplication Pods:" -ForegroundColor Yellow
kubectl get pods -l app=airbnb-app

Write-Host "`nApplication Service:" -ForegroundColor Yellow
kubectl get svc airbnb-app

# 6. Pod Details
Write-Section "6. Pod Details"

Write-Host "`nMySQL Pod Description:" -ForegroundColor Yellow
$mysqlPod = kubectl get pods -l app=mysql -o jsonpath='{.items[0].metadata.name}' 2>$null
if ($mysqlPod) {
    kubectl describe pod $mysqlPod | Select-String -Pattern "Status:|State:|Ready:|Restart|Events:" -Context 0,2
} else {
    Write-Host "No MySQL pod found" -ForegroundColor Red
}

Write-Host "`nApplication Pod Description:" -ForegroundColor Yellow
$appPod = kubectl get pods -l app=airbnb-app -o jsonpath='{.items[0].metadata.name}' 2>$null
if ($appPod) {
    kubectl describe pod $appPod | Select-String -Pattern "Status:|State:|Ready:|Restart|Events:" -Context 0,2
} else {
    Write-Host "No application pod found" -ForegroundColor Red
}

# 7. Pod Logs
Write-Section "7. Pod Logs (Last 30 Lines)"

Write-Host "`nMySQL Logs:" -ForegroundColor Yellow
if ($mysqlPod) {
    kubectl logs $mysqlPod --tail=30
} else {
    Write-Host "No MySQL pod found" -ForegroundColor Red
}

Write-Host "`nApplication Logs:" -ForegroundColor Yellow
if ($appPod) {
    kubectl logs $appPod --tail=30
} else {
    Write-Host "No application pod found" -ForegroundColor Red
}

# 8. ConfigMap and Secrets
Write-Section "8. ConfigMaps"
kubectl get configmap mysql-initdb-config -o yaml 2>$null

# 9. Endpoints
Write-Section "9. Service Endpoints"
Write-Host "`nMySQL Endpoints:" -ForegroundColor Yellow
kubectl get endpoints mysql

Write-Host "`nApplication Endpoints:" -ForegroundColor Yellow
kubectl get endpoints airbnb-app

# 10. Network Test
Write-Section "10. Network Connectivity Test"
if ($appPod) {
    Write-Host "`nTesting MySQL connectivity from app pod..." -ForegroundColor Yellow
    kubectl exec $appPod -- sh -c "nc -zv mysql 3306 2>&1 || echo 'netcat not available, trying telnet...' && timeout 2 telnet mysql 3306 2>&1 || echo 'Connection test failed or tools not available'" 2>$null
}

# 11. Image Status
Write-Section "11. Docker Images in Minikube"
& minikube -p minikube docker-env --shell powershell | Invoke-Expression
docker images | Select-String -Pattern "airbnb|REPOSITORY"

# 12. Resource Usage
Write-Section "12. Resource Usage"
kubectl top pods 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "Metrics server not available. Enable with: minikube addons enable metrics-server" -ForegroundColor Yellow
}

# 13. Events
Write-Section "13. Recent Events"
kubectl get events --sort-by='.lastTimestamp' | Select-Object -Last 20

# Summary
Write-Section "Summary"
Write-Host "Minikube IP: $(minikube ip)" -ForegroundColor Cyan
Write-Host "Application URL: http://$(minikube ip):30080/home" -ForegroundColor Cyan
Write-Host "`nTo view live logs:" -ForegroundColor Yellow
Write-Host "  MySQL:       kubectl logs -l app=mysql -f" -ForegroundColor White
Write-Host "  Application: kubectl logs -l app=airbnb-app -f" -ForegroundColor White
Write-Host "`nTo access MySQL:" -ForegroundColor Yellow
Write-Host "  kubectl exec -it $mysqlPod -- mysql -uairbnb -pairbnb123 airbnbdb" -ForegroundColor White

Write-Host "`nDiagnostics completed at: $(Get-Date)" -ForegroundColor Gray
