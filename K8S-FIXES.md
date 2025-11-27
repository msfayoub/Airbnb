# Kubernetes Deployment Fixes

## Issues Fixed

### 1. **Database Schema Mismatch**
   - **Problem**: The ConfigMap in `mysql-deployment.yaml` had a different table structure than the actual `airbnbdb.sql`
   - **Fix**: Updated the ConfigMap to use the exact schema from `airbnbdb.sql` with correct column names (`mail`, `password`, etc.)

### 2. **MySQL Service DNS Resolution**
   - **Problem**: MySQL service was configured as headless (`clusterIP: None`), which can cause DNS resolution issues
   - **Fix**: Changed to regular ClusterIP service for reliable DNS resolution

### 3. **Missing Health Checks**
   - **Problem**: MySQL had no health probes, making it difficult to know when it's truly ready
   - **Fix**: Added liveness and readiness probes to MySQL using `mysqladmin ping`

### 4. **Insufficient Startup Time**
   - **Problem**: Application probes were checking too early, before MySQL was fully initialized
   - **Fix**: Increased initialDelaySeconds for app probes (90s for liveness, 60s for readiness) and added failure thresholds

## Deployment Instructions

### Quick Deploy
```powershell
cd c:\Users\Ayoub\s5\Java\build\Airbnb\BookingApp_JavaEE
.\deploy-k8s.ps1
```

### Deploy Options
```powershell
# Clean deploy (removes existing resources first)
.\deploy-k8s.ps1 -Clean

# Skip Docker build (use existing image)
.\deploy-k8s.ps1 -SkipBuild

# Both options
.\deploy-k8s.ps1 -Clean -SkipBuild
```

### Manual Deployment Steps

1. **Start Minikube**
   ```powershell
   minikube start
   ```

2. **Build Docker Image in Minikube**
   ```powershell
   # Point to Minikube's Docker daemon
   & minikube -p minikube docker-env --shell powershell | Invoke-Expression
   
   # Build image
   docker build -t airbnb-booking-app:latest .
   ```

3. **Deploy MySQL**
   ```powershell
   kubectl apply -f k8s/mysql-deployment.yaml
   
   # Wait for MySQL to be ready
   kubectl wait --for=condition=ready pod -l app=mysql --timeout=180s
   ```

4. **Deploy Application**
   ```powershell
   kubectl apply -f k8s/deployment.yaml
   kubectl apply -f k8s/service.yaml
   
   # Wait for app to be ready
   kubectl wait --for=condition=ready pod -l app=airbnb-app --timeout=180s
   ```

5. **Access the Application**
   ```powershell
   # Get Minikube IP
   $minikubeIP = minikube ip
   
   # Open in browser
   Start-Process "http://${minikubeIP}:30080/home"
   ```

## Troubleshooting

### Run Diagnostics
```powershell
.\diagnose-k8s.ps1
```

### Check Pod Status
```powershell
# List all pods
kubectl get pods

# Describe a specific pod
kubectl describe pod <pod-name>
```

### View Logs
```powershell
# MySQL logs
kubectl logs -l app=mysql -f

# Application logs
kubectl logs -l app=airbnb-app -f
```

### Test Database Connection
```powershell
# Get MySQL pod name
$mysqlPod = kubectl get pods -l app=mysql -o jsonpath='{.items[0].metadata.name}'

# Connect to MySQL
kubectl exec -it $mysqlPod -- mysql -uairbnb -pairbnb123 airbnbdb

# Check tables
kubectl exec -it $mysqlPod -- mysql -uairbnb -pairbnb123 airbnbdb -e "SHOW TABLES;"

# Verify data
kubectl exec -it $mysqlPod -- mysql -uairbnb -pairbnb123 airbnbdb -e "SELECT * FROM user;"
```

### Check Network Connectivity
```powershell
# Get app pod name
$appPod = kubectl get pods -l app=airbnb-app -o jsonpath='{.items[0].metadata.name}'

# Test MySQL connection from app pod
kubectl exec $appPod -- sh -c "nc -zv mysql 3306"
```

### Port Forward (Alternative Access)
```powershell
# Forward port to localhost
kubectl port-forward service/airbnb-app 8080:8080

# Access at http://localhost:8080/home
```

## Common Issues and Solutions

### Issue: Pods not starting
**Solution**: Check if the Docker image exists in Minikube
```powershell
& minikube -p minikube docker-env --shell powershell | Invoke-Expression
docker images | Select-String airbnb
```

### Issue: MySQL not ready
**Solution**: Check MySQL logs and ensure PVC is bound
```powershell
kubectl logs -l app=mysql
kubectl get pvc
```

### Issue: Application can't connect to database
**Solution**: 
1. Verify MySQL service is running: `kubectl get svc mysql`
2. Check DNS resolution from app pod
3. Review application logs for connection errors

### Issue: ImagePullBackOff error
**Solution**: Use `imagePullPolicy: Never` and ensure image is built in Minikube's Docker daemon
```powershell
& minikube -p minikube docker-env --shell powershell | Invoke-Expression
docker build -t airbnb-booking-app:latest .
```

## Cleanup

```powershell
# Delete all Kubernetes resources
kubectl delete -f k8s/

# Stop Minikube
minikube stop

# Delete Minikube cluster (complete cleanup)
minikube delete
```

## Key Changes Made

### `k8s/mysql-deployment.yaml`
- ✅ Updated ConfigMap with correct database schema
- ✅ Added liveness probe (30s initial delay)
- ✅ Added readiness probe (10s initial delay)
- ✅ Changed service from headless to ClusterIP

### `k8s/deployment.yaml`
- ✅ Increased liveness probe initial delay to 90s
- ✅ Increased readiness probe initial delay to 60s
- ✅ Added failure thresholds and timeouts
- ✅ Added probe timeout settings

## Verification Steps

After deployment, verify everything is working:

1. **Check all pods are running**
   ```powershell
   kubectl get pods
   ```
   Expected: All pods in "Running" state with "1/1" or "2/2" ready

2. **Check services**
   ```powershell
   kubectl get svc
   ```
   Expected: Both `mysql` and `airbnb-app` services listed

3. **Verify database**
   ```powershell
   $mysqlPod = kubectl get pods -l app=mysql -o jsonpath='{.items[0].metadata.name}'
   kubectl exec $mysqlPod -- mysql -uairbnb -pairbnb123 airbnbdb -e "SELECT * FROM user;"
   ```
   Expected: See the admin user record

4. **Access application**
   ```powershell
   $minikubeIP = minikube ip
   Start-Process "http://${minikubeIP}:30080/home"
   ```
   Expected: Application homepage loads successfully
