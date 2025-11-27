# Prometheus & Grafana - Monitoring Setup

## Overview

This monitoring setup includes:
- **Prometheus**: Metrics collection and storage
- **Grafana**: Visualization and dashboards
- **Pre-configured Dashboard**: CPU, Memory, Availability, Network I/O

## Quick Deploy

```powershell
cd c:\Users\Ayoub\s5\Java\build\Airbnb\BookingApp_JavaEE
.\deploy-monitoring.ps1
```

## Manual Deployment

### 1. Enable Metrics Server
```powershell
minikube addons enable metrics-server
```

### 2. Deploy Monitoring Stack
```powershell
# Create namespace
kubectl apply -f k8s/monitoring-namespace.yaml

# Deploy Prometheus
kubectl apply -f k8s/prometheus-rbac.yaml
kubectl apply -f k8s/prometheus-config.yaml
kubectl apply -f k8s/prometheus-deployment.yaml

# Deploy Grafana
kubectl apply -f k8s/grafana-deployment.yaml
```

### 3. Verify Deployment
```powershell
kubectl get pods -n monitoring
kubectl get svc -n monitoring
```

## Access

### Prometheus

**Option 1: NodePort (Docker driver requires tunnel)**
```powershell
minikube service prometheus -n monitoring --url
# Access the URL provided
```

**Option 2: Port Forward**
```powershell
kubectl port-forward -n monitoring svc/prometheus 9090:9090
# Access: http://localhost:9090
```

### Grafana

**Option 1: NodePort (Docker driver requires tunnel)**
```powershell
minikube service grafana -n monitoring --url
# Access the URL provided
```

**Option 2: Port Forward**
```powershell
kubectl port-forward -n monitoring svc/grafana 3000:3000
# Access: http://localhost:3000
```

**Default Credentials:**
- Username: `admin`
- Password: `admin123`

## Pre-configured Dashboard

The deployment includes an "Airbnb Application Monitoring" dashboard with:

### Metrics Displayed

1. **CPU Usage per Pod**
   - Real-time CPU consumption
   - Per-pod breakdown
   - 5-minute rate average

2. **Memory Usage per Pod**
   - Working set memory
   - Per-pod breakdown
   - Live updates

3. **Pod Availability**
   - Number of running airbnb-app pods
   - Color-coded status (red/orange/green)
   - Target: 2 pods running

4. **MySQL Pod Status**
   - MySQL database availability
   - Color-coded (red/green)

5. **Pod Restarts**
   - Restart count in last hour
   - Early warning indicator

6. **Network I/O**
   - Receive/transmit bytes per second
   - Per-pod network activity

7. **Disk Usage**
   - Container filesystem usage
   - Airbnb and MySQL pods

### Dashboard Features

- **Auto-refresh**: Every 5 seconds
- **Time range**: Configurable
- **Interactive**: Click legends to show/hide series
- **Exportable**: Can be downloaded as JSON

## Using Grafana

### Access the Dashboard

1. Open Grafana (see Access section above)
2. Login with admin/admin123
3. Click "Dashboards" → "Airbnb Application Monitoring"

### Create Additional Dashboards

1. Click "+" → "Dashboard"
2. Add Panel
3. Use PromQL queries (examples below)

### Useful PromQL Queries

**CPU Usage:**
```promql
sum(rate(container_cpu_usage_seconds_total{namespace="default",pod=~"airbnb-app.*"}[5m])) by (pod)
```

**Memory Usage:**
```promql
sum(container_memory_working_set_bytes{namespace="default",pod=~"airbnb-app.*"}) by (pod)
```

**Pod Count:**
```promql
count(kube_pod_status_phase{namespace="default",pod=~"airbnb-app.*",phase="Running"})
```

**HTTP Request Rate (if available):**
```promql
rate(http_requests_total[5m])
```

## Monitoring Best Practices

### Set Up Alerts

1. In Grafana, go to Alerting → Alert rules
2. Create alerts for:
   - High CPU usage (> 80%)
   - High memory usage (> 512MB)
   - Pod restarts (> 3 in 1 hour)
   - Service unavailability

### Example Alert Configuration

**High CPU Alert:**
- Query: `avg(rate(container_cpu_usage_seconds_total{pod=~"airbnb-app.*"}[5m])) > 0.8`
- Condition: Above 0.8 for 5 minutes
- Action: Send notification

## Troubleshooting

### Metrics Not Showing

**Check metrics-server:**
```powershell
kubectl top pods
```

**Check Prometheus targets:**
1. Open Prometheus UI
2. Go to Status → Targets
3. Verify targets are "UP"

**Check Prometheus logs:**
```powershell
kubectl logs -n monitoring -l app=prometheus
```

### Grafana Dashboard Empty

**Verify data source:**
1. Grafana → Configuration → Data sources
2. Click "Prometheus"
3. Click "Test" - should show "Data source is working"

**Check time range:**
- Ensure time range includes current data
- Try "Last 5 minutes"

### Cannot Access Services

**Using Docker driver?**
```powershell
# Use service tunnel
minikube service grafana -n monitoring --url
minikube service prometheus -n monitoring --url
```

## Cleanup

```powershell
# Delete monitoring stack
kubectl delete namespace monitoring

# Disable metrics-server
minikube addons disable metrics-server
```

## Advanced Configuration

### Custom Scrape Configs

Edit `k8s/prometheus-config.yaml` to add custom scrape targets.

### Custom Dashboards

1. Export dashboard JSON from Grafana
2. Add to ConfigMap in `k8s/grafana-deployment.yaml`
3. Redeploy

### Persistent Storage

For production, add PersistentVolumeClaims:

```yaml
volumes:
- name: prometheus-storage
  persistentVolumeClaim:
    claimName: prometheus-pvc
```

## Metrics Collection

### Application Metrics

To expose custom metrics from your Java application:

1. Add Micrometer dependency
2. Configure actuator endpoints
3. Add annotations to pods:
   ```yaml
   annotations:
     prometheus.io/scrape: "true"
     prometheus.io/port: "8080"
     prometheus.io/path: "/actuator/prometheus"
   ```

### MySQL Metrics

Add MySQL exporter for database-specific metrics:
```powershell
kubectl apply -f https://raw.githubusercontent.com/prometheus/mysqld_exporter/main/mysqld-exporter.yaml
```

## Resources

- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
- [PromQL Cheat Sheet](https://promlabs.com/promql-cheat-sheet/)
- [Kubernetes Monitoring Guide](https://kubernetes.io/docs/tasks/debug-application-cluster/resource-metrics-pipeline/)
