#!/usr/bin/env pwsh
# Kubernetes Deployment Script for Airbnb Booking Application

param(
    [switch]$Clean,
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

function Write-Step {
    param($Message)
    Write-Host "`n===> $Message" -ForegroundColor Cyan
}

function Write-Success {
    param($Message)
    Write-Host "✓ $Message" -ForegroundColor Green
}

function Write-Error-Custom {
    param($Message)
    Write-Host "✗ $Message" -ForegroundColor Red
}

# Clean up existing resources if requested
if ($Clean) {
    Write-Step "Cleaning up existing Kubernetes resources..."
    kubectl delete -f k8s/ --ignore-not-found=true
    Start-Sleep -Seconds 5
    Write-Success "Cleanup completed"
}

# Check if Minikube is running
Write-Step "Checking Minikube status..."
$minikubeStatus = minikube status 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Minikube is not running. Starting Minikube..." -ForegroundColor Yellow
    minikube start
    if ($LASTEXITCODE -ne 0) {
        Write-Error-Custom "Failed to start Minikube"
        exit 1
    }
}
Write-Success "Minikube is running"

# Build and load Docker image
if (-not $SkipBuild) {
    Write-Step "Building Docker image in Minikube context..."
    
    # Point Docker CLI to Minikube's Docker daemon
    & minikube -p minikube docker-env --shell powershell | Invoke-Expression
    
    Write-Host "Building image: airbnb-booking-app:latest"
    docker build -t airbnb-booking-app:latest .
    
    if ($LASTEXITCODE -ne 0) {
        Write-Error-Custom "Failed to build Docker image"
        exit 1
    }
    Write-Success "Docker image built successfully"
} else {
    Write-Host "Skipping Docker build (using existing image)" -ForegroundColor Yellow
}

# Deploy MySQL
Write-Step "Deploying MySQL database..."
kubectl apply -f k8s/mysql-deployment.yaml

if ($LASTEXITCODE -ne 0) {
    Write-Error-Custom "Failed to deploy MySQL"
    exit 1
}

Write-Host "Waiting for MySQL to be ready..."
$timeout = 180
$elapsed = 0
while ($elapsed -lt $timeout) {
    $ready = kubectl get pods -l app=mysql -o jsonpath='{.items[0].status.conditions[?(@.type=="Ready")].status}' 2>$null
    if ($ready -eq "True") {
        Write-Success "MySQL is ready"
        break
    }
    Start-Sleep -Seconds 5
    $elapsed += 5
    Write-Host "." -NoNewline
}

if ($elapsed -ge $timeout) {
    Write-Error-Custom "MySQL failed to become ready within $timeout seconds"
    Write-Host "`nMySQL Pod Status:"
    kubectl get pods -l app=mysql
    Write-Host "`nMySQL Pod Logs:"
    kubectl logs -l app=mysql --tail=50
    exit 1
}

# Give MySQL extra time to fully initialize the database
Write-Host "Waiting for MySQL database initialization..."
Start-Sleep -Seconds 10

# Deploy Application
Write-Step "Deploying Airbnb application..."
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

if ($LASTEXITCODE -ne 0) {
    Write-Error-Custom "Failed to deploy application"
    exit 1
}

Write-Host "Waiting for application pods to be ready..."
$timeout = 180
$elapsed = 0
while ($elapsed -lt $timeout) {
    $readyPods = kubectl get pods -l app=airbnb-app -o jsonpath='{.items[*].status.conditions[?(@.type=="Ready")].status}' 2>$null
    $readyCount = ($readyPods -split "True").Count - 1
    
    if ($readyCount -ge 2) {
        Write-Success "Application pods are ready ($readyCount/2)"
        break
    }
    Start-Sleep -Seconds 5
    $elapsed += 5
    Write-Host "." -NoNewline
}

if ($elapsed -ge $timeout) {
    Write-Error-Custom "Application failed to become ready within $timeout seconds"
    Write-Host "`nApplication Pod Status:"
    kubectl get pods -l app=airbnb-app
    Write-Host "`nApplication Pod Logs:"
    $podName = kubectl get pods -l app=airbnb-app -o jsonpath='{.items[0].metadata.name}'
    if ($podName) {
        kubectl logs $podName --tail=50
    }
    exit 1
}

# Deploy Ingress (optional)
if (Test-Path "k8s/ingress.yaml") {
    Write-Step "Deploying Ingress..."
    
    # Check if ingress addon is enabled
    $ingressEnabled = minikube addons list | Select-String "ingress.*enabled"
    if (-not $ingressEnabled) {
        Write-Host "Enabling Ingress addon..."
        minikube addons enable ingress
    }
    
    kubectl apply -f k8s/ingress.yaml
    Write-Success "Ingress deployed"
}

# Display deployment info
Write-Host ""
Write-Host ("=" * 60) -ForegroundColor Green
Write-Host "Deployment Completed Successfully!" -ForegroundColor Green
Write-Host ("=" * 60) -ForegroundColor Green

Write-Host "`nDeployed Resources:" -ForegroundColor Yellow
kubectl get all -l app=airbnb-app
kubectl get all -l app=mysql

Write-Host "`nAccess the application:" -ForegroundColor Yellow
$minikubeIP = minikube ip
Write-Host "  NodePort: http://${minikubeIP}:30080/home" -ForegroundColor Cyan

if (Test-Path "k8s/ingress.yaml") {
    Write-Host "  Ingress:  http://airbnb.local/home (add to hosts file)" -ForegroundColor Cyan
}

Write-Host "`nPort Forward Option:" -ForegroundColor Yellow
Write-Host "  kubectl port-forward service/airbnb-app 8080:8080" -ForegroundColor White
Write-Host "  Then access: http://localhost:8080/home" -ForegroundColor Cyan

Write-Host "`nUseful Commands:" -ForegroundColor Yellow
Write-Host "  View pods:          kubectl get pods" -ForegroundColor White
Write-Host "  View services:      kubectl get svc" -ForegroundColor White
Write-Host "  View app logs:      kubectl logs -l app=airbnb-app -f" -ForegroundColor White
Write-Host "  View MySQL logs:    kubectl logs -l app=mysql -f" -ForegroundColor White
Write-Host "  Delete all:         kubectl delete -f k8s/" -ForegroundColor White

Write-Host ("=" * 60) -ForegroundColor Green
