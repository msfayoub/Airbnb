#!/usr/bin/env pwsh
# Deploy Prometheus & Grafana Monitoring Stack

$ErrorActionPreference = "Stop"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Deploying Monitoring Stack" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# Enable metrics-server addon
Write-Host "Enabling metrics-server addon..." -ForegroundColor Yellow
minikube addons enable metrics-server
if ($LASTEXITCODE -ne 0) {
    Write-Host "Warning: Could not enable metrics-server" -ForegroundColor Yellow
}
Start-Sleep -Seconds 5

# Create monitoring namespace
Write-Host "`nCreating monitoring namespace..." -ForegroundColor Yellow
kubectl apply -f k8s/monitoring-namespace.yaml

# Deploy Prometheus
Write-Host "`nDeploying Prometheus..." -ForegroundColor Yellow
kubectl apply -f k8s/prometheus-rbac.yaml
kubectl apply -f k8s/prometheus-config.yaml
kubectl apply -f k8s/prometheus-deployment.yaml

Write-Host "Waiting for Prometheus to be ready..." -ForegroundColor Gray
Start-Sleep -Seconds 10
kubectl wait --for=condition=ready pod -l app=prometheus -n monitoring --timeout=120s

# Deploy Grafana
Write-Host "`nDeploying Grafana..." -ForegroundColor Yellow
kubectl apply -f k8s/grafana-deployment.yaml

Write-Host "Waiting for Grafana to be ready..." -ForegroundColor Gray
Start-Sleep -Seconds 10
kubectl wait --for=condition=ready pod -l app=grafana -n monitoring --timeout=120s

# Display status
Write-Host "`n========================================" -ForegroundColor Green
Write-Host "Monitoring Stack Deployed Successfully!" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Green

Write-Host "Deployed Resources:" -ForegroundColor Yellow
kubectl get all -n monitoring

Write-Host "`nAccess URLs:" -ForegroundColor Cyan
$minikubeIP = minikube ip

Write-Host "`nPrometheus:" -ForegroundColor Yellow
Write-Host "  NodePort: http://${minikubeIP}:30090" -ForegroundColor White
Write-Host "  Or run:   minikube service prometheus -n monitoring --url" -ForegroundColor Gray

Write-Host "`nGrafana:" -ForegroundColor Yellow
Write-Host "  NodePort: http://${minikubeIP}:30030" -ForegroundColor White
Write-Host "  Or run:   minikube service grafana -n monitoring --url" -ForegroundColor Gray
Write-Host "  Login:    admin / admin123" -ForegroundColor Cyan

Write-Host "`nDashboard:" -ForegroundColor Yellow
Write-Host "  The 'Airbnb Application Monitoring' dashboard is pre-configured" -ForegroundColor White

Write-Host "`nUseful Commands:" -ForegroundColor Yellow
Write-Host "  View logs:     kubectl logs -l app=prometheus -n monitoring" -ForegroundColor White
Write-Host "  View metrics:  kubectl top pods" -ForegroundColor White
Write-Host "  Port-forward:  kubectl port-forward -n monitoring svc/grafana 3000:3000" -ForegroundColor White

Write-Host "`n========================================`n" -ForegroundColor Green
