# Kubernetes (K8s) - Complete Beginner Guide

---

## What is Kubernetes?

Kubernetes (K8s) is a tool that **manages your Docker containers** across **multiple servers** automatically.

---

## Why Do We Need K8s?

**Problem with Docker alone:**

```
One Server
┌─────────────────────────┐
│  Container 1 (ehr-app)  │
│  Container 2 (mysql)    │
│  Container 3 (redis)    │
└─────────────────────────┘

What if:
  ❌ Server crashes? → ALL containers die
  ❌ 10,000 users? → One server can't handle
  ❌ Container crashes? → Manual restart needed
  ❌ Deploy new version? → Downtime
```

**K8s solves all of this:**

```
Kubernetes Cluster (multiple servers)
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│   Server 1   │  │   Server 2   │  │   Server 3   │
│  ehr-app (1) │  │  ehr-app (2) │  │  ehr-app (3) │
│  mysql       │  │  redis       │  │              │
└──────────────┘  └──────────────┘  └──────────────┘

  ✅ Server 1 crashes? → K8s moves containers to Server 2 & 3
  ✅ 10,000 users? → K8s adds more containers automatically
  ✅ Container crashes? → K8s restarts it automatically
  ✅ New version? → K8s deploys with zero downtime
```

---

## Docker vs Kubernetes

| Feature | Docker Compose | Kubernetes |
|---------|---------------|-----------|
| Runs on | **1 machine** | **Multiple machines** |
| Auto restart | Basic (restart: always) | Advanced (auto-heal) |
| Scaling | Manual | **Automatic** |
| Load balancing | No | **Yes** |
| Zero downtime deploy | No | **Yes** |
| Self healing | No | **Yes** |
| Use case | Development, small apps | **Production, large apps** |

---

## K8s Architecture

```
┌──────────────────────────────────────────────────────────┐
│                   KUBERNETES CLUSTER                      │
│                                                          │
│  ┌──────────────────────────────────────────────┐        │
│  │            MASTER NODE (Control Plane)        │        │
│  │                                               │        │
│  │  API Server ─ Scheduler ─ Controller Manager  │        │
│  │                    │                          │        │
│  │                  etcd (database)               │        │
│  └──────────────────┬───────────────────────────┘        │
│                     │ manages                             │
│         ┌───────────┼───────────┐                        │
│         ▼           ▼           ▼                        │
│  ┌────────────┐ ┌────────────┐ ┌────────────┐           │
│  │ Worker     │ │ Worker     │ │ Worker     │           │
│  │ Node 1     │ │ Node 2     │ │ Node 3     │           │
│  │            │ │            │ │            │           │
│  │ ┌────────┐ │ │ ┌────────┐ │ │ ┌────────┐ │           │
│  │ │  Pod   │ │ │ │  Pod   │ │ │ │  Pod   │ │           │
│  │ │ehr-app │ │ │ │ehr-app │ │ │ │ mysql  │ │           │
│  │ └────────┘ │ │ └────────┘ │ │ └────────┘ │           │
│  └────────────┘ └────────────┘ └────────────┘           │
└──────────────────────────────────────────────────────────┘
```

| Component | Purpose |
|-----------|---------|
| **Master Node** | Brain of the cluster. Makes decisions |
| **Worker Node** | Actual server where containers run |
| **Pod** | Smallest unit. Wraps one or more containers |
| **API Server** | Entry point. Receives all commands |
| **Scheduler** | Decides which node runs which pod |
| **Controller Manager** | Ensures desired state is maintained |
| **etcd** | Database storing cluster information |

---

## K8s Key Concepts

```
Docker:     Image → Container
Kubernetes: Image → Container → Pod → Deployment → Service
```

| Concept | What It Is | Docker Equivalent |
|---------|-----------|-------------------|
| **Pod** | Smallest unit, wraps container(s) | Container |
| **Deployment** | Manages multiple pods, scaling, updates | docker-compose service |
| **Service** | Network access to pods (load balancer) | Port mapping (-p) |
| **Namespace** | Group/isolate resources | No equivalent |
| **ConfigMap** | Store configuration | Environment variables |
| **Secret** | Store passwords | .env file |
| **Volume** | Persistent storage | Docker volume |
| **Ingress** | Route external traffic to services | Nginx reverse proxy |

---

## 1. Pod

The **smallest unit** in K8s. A pod wraps one or more containers.

```
┌────────────────────┐
│        Pod         │
│  ┌──────────────┐  │
│  │  Container   │  │
│  │  (ehr-app)   │  │
│  └──────────────┘  │
│  IP: 10.1.0.5      │
└────────────────────┘
```

**pod.yml:**

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: ehr-pod
spec:
  containers:
    - name: ehr-app
      image: sravani123/ehr-app:1.0
      ports:
        - containerPort: 8084
```

**Pod Commands:**

```bash
# Create pod
kubectl apply -f pod.yml

# List pods
kubectl get pods

# Pod details
kubectl describe pod ehr-pod

# Pod logs
kubectl logs ehr-pod

# Enter pod
kubectl exec -it ehr-pod -- bash

# Delete pod
kubectl delete pod ehr-pod
```

**Note:** We rarely create pods directly. We use **Deployments** instead.

---

## 2. Deployment

Manages **multiple copies (replicas)** of your pod.

```
┌─────────────────────────────────────────┐
│           Deployment (ehr-app)          │
│                                         │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐  │
│  │  Pod 1  │ │  Pod 2  │ │  Pod 3  │  │
│  │ ehr-app │ │ ehr-app │ │ ehr-app │  │
│  └─────────┘ └─────────┘ └─────────┘  │
│                                         │
│  replicas: 3 (always keeps 3 running)   │
└─────────────────────────────────────────┘
```

**deployment.yml:**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ehr-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: ehr-app
  template:
    metadata:
      labels:
        app: ehr-app
    spec:
      containers:
        - name: ehr-app
          image: sravani123/ehr-app:1.0
          ports:
            - containerPort: 8084
```

**Deployment Commands:**

```bash
# Create deployment
kubectl apply -f deployment.yml

# List deployments
kubectl get deployments

# List pods created by deployment
kubectl get pods

# Scale up (more pods)
kubectl scale deployment ehr-deployment --replicas=5

# Scale down
kubectl scale deployment ehr-deployment --replicas=2

# Delete deployment
kubectl delete deployment ehr-deployment
```

**Self Healing:**

```
Pod 2 crashes → K8s detects → Creates new Pod 2 automatically ✅

  ┌─────────┐ ┌─────────┐ ┌─────────┐
  │  Pod 1  │ │  Pod 2 ❌│ │  Pod 3  │
  │ running │ │ crashed  │ │ running │
  └─────────┘ └─────────┘ └─────────┘
                   │
                   ▼ K8s creates new one
              ┌─────────┐
              │  Pod 2 ✅│
              │ running  │
              └─────────┘
```

---

## 3. Service

Gives a **stable network endpoint** to access your pods.

Pods can die and restart with new IPs. Service provides a **fixed IP/name**.

```
Without Service:
  Pod IP: 10.1.0.5 → Pod dies → New Pod IP: 10.1.0.9 → ❌ Connection lost

With Service:
  Service IP: 10.0.0.50 → Always same → Routes to healthy pods ✅
```

**service.yml:**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: ehr-service
spec:
  type: NodePort
  selector:
    app: ehr-app
  ports:
    - port: 8084
      targetPort: 8084
      nodePort: 30084
```

```
┌──────────────────────────────────────────────┐
│              Service (ehr-service)            │
│              IP: 10.0.0.50:8084              │
│                     │                        │
│         ┌───────────┼───────────┐            │
│         ▼           ▼           ▼            │
│    ┌─────────┐ ┌─────────┐ ┌─────────┐     │
│    │  Pod 1  │ │  Pod 2  │ │  Pod 3  │     │
│    │ ehr-app │ │ ehr-app │ │ ehr-app │     │
│    └─────────┘ └─────────┘ └─────────┘     │
│                                              │
│    Load balances traffic across all pods     │
└──────────────────────────────────────────────┘
```

**Service Types:**

| Type | Access From | Use Case |
|------|-----------|----------|
| **ClusterIP** | Inside cluster only | Internal services (default) |
| **NodePort** | Outside using Node IP:Port | Development/testing |
| **LoadBalancer** | External load balancer | Production (cloud) |

**Service Commands:**

```bash
# Create service
kubectl apply -f service.yml

# List services
kubectl get services

# Access app
# NodePort: http://node-ip:30084
```

---

## 4. ConfigMap & Secret

**ConfigMap** - Store configuration (non-sensitive):

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: ehr-config
data:
  SERVER_PORT: "8084"
  SPRING_DATASOURCE_URL: "jdbc:mysql://mysql-service:3306/aborabordb"
```

**Secret** - Store passwords (encoded):

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: ehr-secret
type: Opaque
data:
  DB_PASSWORD: cm9vdDEyMw==
  DB_USERNAME: cm9vdA==
```

**Encode password to base64:**

```bash
echo -n "root123" | base64
# Output: cm9vdDEyMw==
```

**Create configmap & secret:**

```bash
kubectl apply -f configmap.yml
kubectl apply -f secret.yml
```

**Use in Deployment:**

```yaml
spec:
  containers:
    - name: ehr-app
      image: sravani123/ehr-app:1.0
      envFrom:
        - configMapRef:
            name: ehr-config
        - secretRef:
            name: ehr-secret
```

---

## 5. Namespace

Groups and isolates resources. Like **folders**.

```bash
# Create namespace
kubectl create namespace production
kubectl create namespace development

# Deploy to specific namespace
kubectl apply -f deployment.yml -n production

# List pods in namespace
kubectl get pods -n production
```

```
┌─────────────────────────────────────┐
│         Kubernetes Cluster          │
│                                     │
│  ┌─────────────┐ ┌──────────────┐  │
│  │ production  │ │ development  │  │
│  │             │ │              │  │
│  │ ehr-app x3  │ │ ehr-app x1   │  │
│  │ mysql x1    │ │ mysql x1     │  │
│  │ redis x1    │ │              │  │
│  └─────────────┘ └──────────────┘  │
│   (isolated)      (isolated)       │
└─────────────────────────────────────┘
```

---

## Docker Compose vs K8s Files Comparison

| Docker Compose | Kubernetes |
|---------------|-----------|
| `docker-compose.yml` | `deployment.yml` + `service.yml` |
| `image: mysql:8.0` | `image: mysql:8.0` |
| `ports: - "8084:8084"` | `containerPort: 8084` |
| `environment:` | `ConfigMap / Secret` |
| `volumes:` | `PersistentVolume` |
| `depends_on:` | No equivalent (handle in app) |
| `restart: always` | Built-in (auto-heal) |
| `docker-compose up` | `kubectl apply -f .` |

---

## kubectl Commands (Most Used)

```bash
# --- CLUSTER INFO ---
kubectl cluster-info
kubectl get nodes

# --- PODS ---
kubectl get pods
kubectl get pods -o wide
kubectl describe pod <pod-name>
kubectl logs <pod-name>
kubectl logs -f <pod-name>
kubectl exec -it <pod-name> -- bash
kubectl delete pod <pod-name>

# --- DEPLOYMENTS ---
kubectl get deployments
kubectl apply -f deployment.yml
kubectl scale deployment <name> --replicas=5
kubectl delete deployment <name>

# --- SERVICES ---
kubectl get services
kubectl apply -f service.yml
kubectl delete service <name>

# --- EVERYTHING ---
kubectl get all
kubectl get all -n <namespace>

# --- APPLY ALL FILES IN FOLDER ---
kubectl apply -f .

# --- DELETE ALL FROM FILE ---
kubectl delete -f deployment.yml
```

---

## How to Practice K8s Locally

| Tool | What It Is | Best For |
|------|-----------|----------|
| **Minikube** | Single node K8s on laptop | Learning |
| **Docker Desktop** | Built-in K8s (enable in settings) | Easy setup |
| **Kind** | K8s in Docker containers | CI/CD testing |

**Enable K8s in Docker Desktop:**

1. Open Docker Desktop
2. Settings → Kubernetes
3. Check **"Enable Kubernetes"**
4. Click Apply & Restart
5. Wait 5 minutes

**Verify:**

```bash
kubectl version
kubectl get nodes
```

---

## Complete Example: Deploy EHR App on K8s

**File 1: configmap.yml**

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: ehr-config
data:
  SERVER_PORT: "8084"
  SPRING_DATASOURCE_URL: "jdbc:mysql://147.79.71.46:3306/u516822144_smartAiHr"
```

**File 2: secret.yml**

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: ehr-secret
type: Opaque
data:
  SPRING_DATASOURCE_USERNAME: c3JhdmFuaQ==
  SPRING_DATASOURCE_PASSWORD: U21hcnRBaUhyQDEyMw==
```

**File 3: deployment.yml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ehr-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: ehr-app
  template:
    metadata:
      labels:
        app: ehr-app
    spec:
      containers:
        - name: ehr-app
          image: sravani123/ehr-app:1.0
          ports:
            - containerPort: 8084
          envFrom:
            - configMapRef:
                name: ehr-config
            - secretRef:
                name: ehr-secret
```

**File 4: service.yml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: ehr-service
spec:
  type: NodePort
  selector:
    app: ehr-app
  ports:
    - port: 8084
      targetPort: 8084
      nodePort: 30084
```

**Deploy Everything:**

```bash
kubectl apply -f configmap.yml
kubectl apply -f secret.yml
kubectl apply -f deployment.yml
kubectl apply -f service.yml

# Check status
kubectl get all

# Access app: http://localhost:30084
```

---

## K8s Complete Flow

```
Developer                Docker Hub              Kubernetes Cluster
    │                        │                         │
 docker build               │                         │
 docker push ──────────────►│                         │
    │                        │                         │
 kubectl apply ─────────────────────────────────────►│
    │                        │                         │
    │                        │◄── pulls image ─────────│
    │                        │                         │
    │                        │    Creates 3 pods       │
    │                        │    Starts containers    │
    │                        │    App is live! ✅      │
```

---

## Summary

```
Docker        → Runs containers on ONE machine
Docker Compose → Runs MULTIPLE containers on ONE machine
Kubernetes    → Runs containers on MULTIPLE machines
              → Auto scaling
              → Auto healing
              → Zero downtime deployment
              → Load balancing
```

---

## Complete Docker + K8s Learning Progress

| # | Topic | Status |
|---|-------|--------|
| 1 | Docker Basics | ✅ |
| 2 | Images | ✅ |
| 3 | Containers | ✅ |
| 4 | Port Mapping & Volumes | ✅ |
| 5 | Dockerfile | ✅ |
| 6 | Docker Compose | ✅ |
| 7 | Networking | ✅ |
| 8 | Production Best Practices | ✅ |
| 9 | CI/CD with Docker | ✅ |
| 10 | Docker Hub | ✅ |
| 11 | Kubernetes Basics | ✅ |