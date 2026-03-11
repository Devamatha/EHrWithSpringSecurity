# Kubernetes Complete Notes — Beginner to Advanced

---

# SESSION 1: Kubernetes Architecture

## What is Kubernetes (K8s)?
- Open-source container orchestration platform by Google
- Automates deployment, scaling, and management of containerized apps
- Docker runs containers, K8s manages them at scale

## Architecture — The Big Picture

```
                         ┌─────────────────────────────────────────────┐
                         │              K8s CLUSTER                     │
                         │                                             │
   kubectl/API ────────► │  ┌───────────────────────────────────────┐  │
   (You/Developer)       │  │         CONTROL PLANE (Master)        │  │
                         │  │                                       │  │
                         │  │  ┌───────────┐    ┌──────────────┐   │  │
                         │  │  │API Server │    │    etcd       │   │  │
                         │  │  │(Front Door│    │  (Database)   │   │  │
                         │  │  │ Gateway)  │    │  Key-Value    │   │  │
                         │  │  └─────┬─────┘    └──────────────┘   │  │
                         │  │        │                              │  │
                         │  │  ┌─────┴─────┐    ┌──────────────┐   │  │
                         │  │  │Scheduler  │    │ Controller   │   │  │
                         │  │  │(Assigns   │    │ Manager      │   │  │
                         │  │  │pods to    │    │(Maintains    │   │  │
                         │  │  │ nodes)    │    │ desired state│   │  │
                         │  │  └───────────┘    └──────────────┘   │  │
                         │  └──────────────────┬────────────────────┘  │
                         │                     │                       │
                         │          Communicates with Workers          │
                         │                     │                       │
                         │  ┌──────────────────┼────────────────────┐  │
                         │  │                  │                    │  │
                         │  ▼                  ▼                    ▼  │
                         │ ┌──────────┐  ┌──────────┐  ┌──────────┐  │
                         │ │ WORKER   │  │ WORKER   │  │ WORKER   │  │
                         │ │ NODE 1   │  │ NODE 2   │  │ NODE 3   │  │
                         │ │          │  │          │  │          │  │
                         │ │┌────────┐│  │┌────────┐│  │┌────────┐│  │
                         │ ││Kubelet ││  ││Kubelet ││  ││Kubelet ││  │
                         │ │└────────┘│  │└────────┘│  │└────────┘│  │
                         │ │┌────────┐│  │┌────────┐│  │┌────────┐│  │
                         │ ││Kube-   ││  ││Kube-   ││  ││Kube-   ││  │
                         │ ││proxy   ││  ││proxy   ││  ││proxy   ││  │
                         │ │└────────┘│  │└────────┘│  │└────────┘│  │
                         │ │┌────────┐│  │┌────────┐│  │┌────────┐│  │
                         │ ││Container│  ││Container│  ││Container│  │
                         │ ││Runtime ││  ││Runtime ││  ││Runtime ││  │
                         │ │└────────┘│  │└────────┘│  │└────────┘│  │
                         │ │          │  │          │  │          │  │
                         │ │ ┌──┐┌──┐ │  │ ┌──┐┌──┐│  │ ┌──┐     │  │
                         │ │ │P1││P2│ │  │ │P3││P4││  │ │P5│     │  │
                         │ │ └──┘└──┘ │  │ └──┘└──┘│  │ └──┘     │  │
                         │ └──────────┘  └──────────┘  └──────────┘  │
                         └─────────────────────────────────────────────┘
                                        P = Pod
```

## Control Plane Components — The Brain

### 1. API Server — The Front Door
- Every command goes through API Server ONLY
- kubectl, Kubelet, Scheduler, Controller — ALL talk through API Server
- Nothing talks directly to etcd, Scheduler, or Controller
- Real-time analogy: Hospital **Reception Desk** — everyone reports here first

### 2. etcd — The Database
- Stores ENTIRE cluster state (pods, deployments, services, secrets, RBAC — everything)
- Key-value store (not SQL)
- Only API Server talks to etcd directly
- If etcd is lost → entire cluster state is GONE (that's why backup is critical)
- Usually runs 3 or 5 instances (odd number for consensus)
- Real-time analogy: Hospital **Record Room** — all records stored here

### 3. Scheduler — The Assigner
- Decides WHICH node a new pod runs on
- Considers: CPU/Memory available, node selectors, affinity, taints/tolerations
- Only ASSIGNS pod to node, doesn't run it (Kubelet runs it)
- If no node has space → pod stays **Pending** → Karpenter/CA adds node
- Real-time analogy: Hospital **Bed Allocation Manager**

### 4. Controller Manager — The Maintainer
- Runs multiple controllers: ReplicaSet, Deployment, Node, Job, ServiceAccount, Endpoint controllers
- Each controller watches its resources and maintains desired state
- Reconciliation Loop: Desired State vs Actual State → Act to match
  - Example: Desired = 3 pods, Actual = 2 pods → Create 1 more!
- Real-time analogy: Hospital **Department Supervisors**

## Worker Node Components — The Workers

### 5. Kubelet — The Node Agent
- Runs on EVERY worker node
- Receives orders from API Server → starts/stops pods
- Reports pod health back to API Server
- Runs health probes (liveness, readiness, startup)
- Collects metrics for Metrics Server
- Real-time analogy: **Nurse on a ward** — monitors patients, reports to doctor

### 6. Kube-proxy — The Network Manager
- Runs on EVERY worker node
- Maintains network rules (iptables/IPVS)
- Enables Service → Pod load balancing
- Handles ClusterIP, NodePort, LoadBalancer routing
- Real-time analogy: Hospital **Receptionist** directing patients to available doctors

### 7. Container Runtime — The Engine
- Actually pulls images and runs containers
- Popular runtimes: containerd (most common), CRI-O (OpenShift)
- Docker removed in K8s 1.24, but Docker images still work
- Real-time analogy: **Operating room equipment** — keeps containers alive

## Complete Flow — kubectl apply -f deployment.yaml

```
Step 1:  kubectl sends YAML ──────────────► API Server
Step 2:  API Server validates YAML
Step 3:  API Server stores in ─────────────► etcd
Step 4:  Controller Manager detects ◄──────── "New Deployment, need 3 pods"
Step 5:  Scheduler assigns ────────────────► Pod 1→Node1, Pod 2→Node2, Pod 3→Node1
Step 6:  Kubelet receives orders ◄──────────  "Run these pods"
Step 7:  Container Runtime pulls image & starts containers
Step 8:  Kube-proxy sets up networking (Service → Pod load balancing)
Step 9:  Kubelet reports back ─────────────► "Pods running! ✅"
Step 10: etcd updated ◄───────────────────── "3/3 pods running"
```

## Control Plane vs Worker Node — Summary

```
┌─────────────────────────────┬──────────────────────────────┐
│      CONTROL PLANE          │       WORKER NODE            │
│      (The Brain)            │       (The Hands)            │
├─────────────────────────────┼──────────────────────────────┤
│  API Server                 │  Kubelet                     │
│  → Receives all commands    │  → Runs pods on this node    │
│                             │                              │
│  etcd                       │  Kube-proxy                  │
│  → Stores cluster state     │  → Handles networking        │
│                             │                              │
│  Scheduler                  │  Container Runtime           │
│  → Assigns pods to nodes    │  → Actually runs containers  │
│                             │                              │
│  Controller Manager         │  Pods                        │
│  → Ensures desired state    │  → Your actual applications  │
│                             │                              │
│  Makes DECISIONS            │  Does the WORK               │
└─────────────────────────────┴──────────────────────────────┘
```

## Key Concepts
- **Cluster** = Control Plane + Worker Nodes
- **Node** = A machine (VM/physical) that runs pods
- **Pod** = Smallest unit in K8s, wraps one or more containers
- Control Plane makes decisions, Worker Nodes execute them

---

# SESSION 2: Local Environment Setup

## Tools
- **Minikube** — Single-node K8s cluster for local development
- **kubectl** — CLI tool to interact with K8s cluster

## Essential Commands
```bash
minikube start                    # Start local cluster
minikube stop                     # Stop cluster
minikube status                   # Check status
kubectl cluster-info              # Cluster details
kubectl config get-contexts       # List all cluster contexts
kubectl config use-context minikube  # Switch to minikube context
```

## Important
- Always check context before running commands to avoid running on production cluster
- `kubectl config use-context minikube` — ALWAYS verify!

---

# SESSION 3: Pods Deep Dive

## What is a Pod?
- Smallest deployable unit in K8s
- Wraps one or more containers
- Containers in a pod share network (localhost) and storage
- Pods are ephemeral — they can die and be replaced

## Pod YAML Structure
```yaml
apiVersion: v1              # API version (v1 for core resources)
kind: Pod                   # Resource type
metadata:                   # Identity
  name: nginx-pod
  labels:
    app: nginx
spec:                       # Desired state
  containers:
    - name: nginx-container
      image: nginx:latest
      ports:
        - containerPort: 80
      env:
        - name: APP_NAME
          value: "EHR-App"
      resources:
        requests:
          memory: "64Mi"
          cpu: "100m"
        limits:
          memory: "128Mi"
          cpu: "250m"
```

## Pod Commands
```bash
kubectl apply -f pod.yaml          # Create/update from YAML
kubectl get pods                   # List pods
kubectl get pods -o wide           # List with more details (IP, Node)
kubectl describe pod <name>        # Detailed pod info + events
kubectl logs <name>                # View pod logs
kubectl logs <name> -f             # Follow logs real-time
kubectl logs <name> --previous     # Logs from crashed pod
kubectl logs <name> -c <container> # Logs from specific container
kubectl exec -it <name> -- /bin/sh # Shell into pod
kubectl delete pod <name>          # Delete pod
kubectl delete -f pod.yaml         # Delete from YAML
```

## Resource Units
- CPU: `1000m` = 1 CPU core, `100m` = 0.1 core
- Memory: `Mi` (Mebibytes), `Gi` (Gibibytes)
- CPU limit exceeded → Pod is **THROTTLED** (slowed down)
- Memory limit exceeded → Pod is **KILLED** (OOMKilled)
- Mnemonic: **M**emory=**M**urder(KILLED), **C**PU=**C**alm down(THROTTLED)

---

# SESSION 4: ReplicaSets & Deployments

## ReplicaSet
- Ensures a specified number of pod replicas are running
- Self-healing — if a pod dies, creates a new one
- Rarely used directly — use Deployment instead

## Deployment
- Manages ReplicaSets
- Adds rolling updates and rollback capability
- **Most commonly used workload resource**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ehr-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: ehr-app          # Must match template labels!
  template:
    metadata:
      labels:
        app: ehr-app        # Must match selector!
    spec:
      containers:
        - name: ehr-container
          image: ehr-app:1.0
          ports:
            - containerPort: 8080
```

## Important
- `selector.matchLabels` MUST match `template.metadata.labels`
- Two label locations: metadata.labels (for Deployment) and template.metadata.labels (for Pods)

## Deployment Commands
```bash
kubectl apply -f deployment.yaml
kubectl get deployments
kubectl get rs                          # Get ReplicaSets
kubectl scale deployment <name> --replicas=5
kubectl rollout status deployment <name>
kubectl rollout history deployment <name>
kubectl rollout undo deployment <name>  # Rollback
kubectl set image deployment/<name> container=image:tag
```

---

# SESSION 5: Services & Networking

## Why Services?
- Pods have temporary IPs — they change on restart
- Service provides a **stable IP and DNS name**

## Service Types

### ClusterIP (Default) — Internal Only
```yaml
apiVersion: v1
kind: Service
metadata:
  name: ehr-service
spec:
  type: ClusterIP
  selector:
    app: ehr-app
  ports:
    - port: 80            # Service port (what other services use)
      targetPort: 8080    # Pod's container port
```

### NodePort — External (Development)
```yaml
spec:
  type: NodePort
  ports:
    - port: 80            # Service port
      targetPort: 8080    # Container port
      nodePort: 31000     # External port (30000-32767)
```

### LoadBalancer — External (Production/Cloud)
```yaml
spec:
  type: LoadBalancer
  ports:
    - port: 80
      targetPort: 8080
```

## Port Summary
| Port | What It Is | Example |
|------|-----------|---------|
| containerPort / targetPort | Port your app listens on | 8080 |
| port | Service port (internal access) | 80 |
| nodePort | External port on every node | 31000 |

## K8s DNS
- Services get DNS names automatically
- Format: `<service-name>.<namespace>.svc.cluster.local`
- Example: `ehr-service.production.svc.cluster.local`

---

# SESSION 6: Namespaces & Resource Management

## Namespaces
- Virtual clusters within a cluster
- Isolation between teams/environments
- Default namespaces: `default`, `kube-system`, `kube-public`

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: production
```

```bash
kubectl create ns development
kubectl get ns
kubectl get pods -n production          # Pods in specific namespace
kubectl get pods --all-namespaces       # Pods across all namespaces
```

## ResourceQuota — Namespace-Level Budget
```yaml
apiVersion: v1
kind: ResourceQuota
metadata:
  name: prod-quota
  namespace: production
spec:
  hard:
    requests.cpu: "4"
    requests.memory: 8Gi
    pods: "20"
```

## LimitRange — Per-Container Defaults
```yaml
apiVersion: v1
kind: LimitRange
metadata:
  name: prod-limits
  namespace: production
spec:
  limits:
    - default:
        cpu: "500m"
        memory: "512Mi"
      defaultRequest:
        cpu: "200m"
        memory: "256Mi"
      type: Container
```

- ResourceQuota = total budget for entire namespace
- LimitRange = default limits for each container

---

# SESSION 7: ConfigMaps & Secrets

## ConfigMap — Non-Sensitive Configuration
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  APP_NAME: "EHR Application"
  APP_ENV: "production"
  DB_HOST: "mysql-service"
```

### Using ConfigMap in Pod
```yaml
# Method 1: Load ALL keys
envFrom:
  - configMapRef:
      name: app-config

# Method 2: Load specific key
env:
  - name: DATABASE_HOST
    valueFrom:
      configMapKeyRef:
        name: app-config
        key: DB_HOST
```

## Secret — Sensitive Data (base64 encoded, NOT encrypted)
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: db-secret
type: Opaque
data:
  DB_PASSWORD: cGFzc3dvcmQxMjM=      # base64 encoded

# OR use stringData (plain text, auto-encoded)
stringData:
  DB_PASSWORD: password123
```

### Using Secret in Pod
```yaml
env:
  - name: DB_PASSWORD
    valueFrom:
      secretKeyRef:
        name: db-secret
        key: DB_PASSWORD
```

### Base64 Commands
```bash
echo -n "password123" | base64          # Encode
echo "cGFzc3dvcmQxMjM=" | base64 -d    # Decode
```

---

# SESSION 8: Volumes & Persistent Storage

## Volume Types

### emptyDir — Temporary (dies with pod)
```yaml
volumes:
  - name: temp-data
    emptyDir: {}
```

### hostPath — Node's filesystem
```yaml
volumes:
  - name: node-data
    hostPath:
      path: /data/app
```

### PersistentVolume (PV) + PersistentVolumeClaim (PVC)
```yaml
# PV — Created by Admin (the actual storage)
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mysql-pv
spec:
  capacity:
    storage: 10Gi
  accessModes:
    - ReadWriteOnce
  persistentVolumeReclaimPolicy: Retain
  hostPath:
    path: /data/mysql

---
# PVC — Created by Developer (request for storage)
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
```

### Using PVC in Pod
```yaml
volumes:
  - name: mysql-storage
    persistentVolumeClaim:
      claimName: mysql-pvc
containers:
  - name: mysql
    volumeMounts:
      - name: mysql-storage
        mountPath: /var/lib/mysql
```

## Access Modes
| Mode | Short | Description |
|------|-------|-------------|
| ReadWriteOnce | RWO | One node read/write |
| ReadOnlyMany | ROX | Many nodes read-only |
| ReadWriteMany | RWX | Many nodes read/write |

## Reclaim Policies
- **Retain** — Keep data after PVC deleted (manual cleanup)
- **Delete** — Delete data when PVC deleted
- **Recycle** — Wipe data and make PV available again

## StorageClass — Dynamic Provisioning
```yaml
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: fast-storage
provisioner: ebs.csi.aws.com
parameters:
  type: gp3
```

---

# SESSION 9: Jobs, CronJobs & DaemonSets

## Job — One-Time Task
```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: db-migration
spec:
  completions: 1
  parallelism: 1
  template:
    spec:
      containers:
        - name: migration
          image: migration-tool:1.0
          command: ["./migrate.sh"]
      restartPolicy: Never
```

## CronJob — Scheduled Task
```yaml
apiVersion: batch/v1
kind: CronJob
metadata:
  name: nightly-backup
spec:
  schedule: "0 2 * * *"           # 2 AM daily
  jobTemplate:
    spec:
      template:
        spec:
          containers:
            - name: backup
              image: backup-tool:1.0
          restartPolicy: OnFailure
```

### Cron Format
```
┌───────── minute (0-59)
│ ┌─────── hour (0-23)
│ │ ┌───── day of month (1-31)
│ │ │ ┌─── month (1-12)
│ │ │ │ ┌─ day of week (0-6, Sun=0)
│ │ │ │ │
* * * * *

"0 2 * * *"     = Every day at 2:00 AM
"*/5 * * * *"   = Every 5 minutes
"0 0 * * 0"     = Every Sunday midnight
```

## DaemonSet — One Pod Per Node
```yaml
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: log-collector
spec:
  selector:
    matchLabels:
      app: fluentd
  template:
    metadata:
      labels:
        app: fluentd
    spec:
      containers:
        - name: fluentd
          image: fluentd:latest
```

- No `replicas` field — automatically one per node
- Use for: log collectors, monitoring agents, network plugins

---

# SESSION 10: Ingress & Traffic Routing

## What is Ingress?
- Single entry point for external traffic
- Routes to different services based on URL path or hostname
- Requires **Ingress Controller** (NOT built-in, must be installed!)

## Components
- **Ingress Controller** = Nginx pod (must install separately)
- **Ingress Resource** = YAML rules for routing

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: app-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  ingressClassName: nginx
  tls:
    - hosts:
        - ehr.mycompany.com
      secretName: tls-secret
  rules:
    - host: ehr.mycompany.com
      http:
        paths:
          - path: /api
            pathType: Prefix
            backend:
              service:
                name: api-service
                port:
                  number: 80
          - path: /admin
            pathType: Prefix
            backend:
              service:
                name: admin-service
                port:
                  number: 80
```

## Routing Types
- **Path-based:** `ehr.com/api` → API Service, `ehr.com/admin` → Admin Service
- **Host-based:** `api.ehr.com` → API Service, `admin.ehr.com` → Admin Service

## Ingress Controller Installation (Minikube)
```bash
minikube addons enable ingress
```

---

# SESSION 11: Health Checks & Probes

## Three Types of Probes

### Liveness Probe — "Is it alive?"
- If fails → K8s **restarts** the pod
```yaml
livenessProbe:
  httpGet:
    path: /health
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10
```

### Readiness Probe — "Is it ready for traffic?"
- If fails → K8s **stops sending traffic** (removes from Service)
```yaml
readinessProbe:
  httpGet:
    path: /ready
    port: 8080
  initialDelaySeconds: 10
  periodSeconds: 5
```

### Startup Probe — "Has it started yet?"
- For slow-starting apps (Spring Boot!)
- If fails → K8s **kills and restarts** pod
```yaml
startupProbe:
  httpGet:
    path: /health
    port: 8080
  failureThreshold: 30
  periodSeconds: 10
  # Total wait: 30 × 10 = 300 seconds (5 min)
```

## Probe Methods
| Method | How It Checks |
|--------|--------------|
| `httpGet` | HTTP request to endpoint |
| `tcpSocket` | TCP connection to port |
| `exec` | Runs command inside container |

---

# SESSION 12: Rolling Updates & Deployment Strategies

## Rolling Update (Default)
```yaml
spec:
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1            # Extra pods during update
      maxUnavailable: 0      # Zero downtime!
```

## Recreate Strategy
```yaml
spec:
  strategy:
    type: Recreate           # Kill all old → create all new (has downtime!)
```

## Deployment Strategies Summary
| Strategy | How It Works | Downtime |
|----------|-------------|----------|
| **Rolling Update** | Gradually replace old with new | No |
| **Recreate** | Kill all old, then create new | Yes |
| **Blue/Green** | Run both, switch Service selector | No |
| **Canary** | Route small % traffic to new version | No |

## Rollback Commands
```bash
kubectl rollout status deployment/<name>
kubectl rollout history deployment/<name>
kubectl rollout undo deployment/<name>              # Rollback to previous
kubectl rollout undo deployment/<name> --to-revision=2  # Specific revision
```

---

# SESSION 13: RBAC & Security

## RBAC Components

### WHO — ServiceAccount
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: ehr-service-account
  namespace: production
```

### WHAT — Role (namespace) / ClusterRole (cluster-wide)
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: pod-reader
  namespace: production
rules:
  - apiGroups: [""]
    resources: ["pods"]
    verbs: ["get", "list", "watch"]
```

### CONNECT — RoleBinding / ClusterRoleBinding
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: read-pods
  namespace: production
subjects:
  - kind: ServiceAccount
    name: ehr-service-account
roleRef:
  kind: Role
  name: pod-reader
  apiGroup: rbac.authorization.k8s.io
```

## Network Policy
```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-ehr-only
spec:
  podSelector:
    matchLabels:
      app: mysql
  ingress:
    - from:
        - podSelector:
            matchLabels:
              app: ehr-app
      ports:
        - port: 3306
```

## Security Context
```yaml
securityContext:
  runAsNonRoot: true
  runAsUser: 1000
  readOnlyRootFilesystem: true
```

---

# SESSION 14: StatefulSets

## When to Use
- Databases (MySQL, MongoDB, Kafka, ZooKeeper)
- Apps that need stable identity and persistent storage

## 4 Guarantees
1. **Stable pod names:** mysql-0, mysql-1, mysql-2 (not random)
2. **Stable network identity:** mysql-0.mysql-headless.default.svc.cluster.local
3. **Ordered startup/shutdown:** 0 first, then 1, then 2
4. **Per-pod PVC:** Each pod gets its own storage via volumeClaimTemplates

## StatefulSet YAML
```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: mysql
spec:
  serviceName: mysql-headless     # REQUIRED — links to headless service
  replicas: 3
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
        - name: mysql
          image: mysql:8.0
          ports:
            - containerPort: 3306
          volumeMounts:
            - name: data
              mountPath: /var/lib/mysql
  volumeClaimTemplates:
    - metadata:
        name: data
      spec:
        accessModes: ["ReadWriteOnce"]
        resources:
          requests:
            storage: 10Gi
```

## Headless Service (clusterIP: None)
```yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql-headless
spec:
  clusterIP: None
  selector:
    app: mysql
  ports:
    - port: 3306
```

## Key Points
- PVC naming: `{vctName}-{stsName}-{index}` → data-mysql-0, data-mysql-1
- Scale down: highest index deleted first (mysql-2 first)
- PVCs are RETAINED when StatefulSet is deleted (data safety)
- Pod DNS: `mysql-0.mysql-headless.default.svc.cluster.local`

## StatefulSet vs Deployment
| | Deployment | StatefulSet |
|--|-----------|-------------|
| Pod names | Random (abc-xyz) | Ordered (mysql-0, 1, 2) |
| Storage | Shared or none | Per-pod PVC |
| Startup | All at once | Ordered |
| Use for | Stateless apps | Databases |

---

# SESSION 15: Helm — Package Manager

## What is Helm?
- Package manager for K8s (like apt/yum/npm)
- One command installs entire application stack

## 3 Core Concepts
| Concept | What It Is | Example |
|---------|-----------|---------|
| **Chart** | Package of K8s templates | bitnami/mysql |
| **Release** | Installed instance | my-mysql |
| **Repository** | Chart store | https://charts.bitnami.com |

## Chart Structure
```
mychart/
├── Chart.yaml          # Metadata (name, version)
├── values.yaml         # Default values
├── values-prod.yaml    # Production overrides
└── templates/
    ├── deployment.yaml # {{ .Values.replicas }}
    ├── service.yaml
    └── configmap.yaml
```

## Template Syntax
```yaml
replicas: {{ .Values.replicas }}
name: {{ .Release.Name }}-app
image: {{ .Values.image }}:{{ .Values.tag }}
```

## Helm Commands
```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm search repo mysql

helm install my-app ./chart                    # Install
helm install my-app ./chart -f values-prod.yaml   # With overrides
helm install my-app ./chart --set replicas=5   # Inline override

helm upgrade my-app ./chart --set tag=v2.0     # Upgrade
helm rollback my-app 2                         # Rollback to revision 2
helm uninstall my-app                          # Delete

helm list                    # List releases
helm history my-app          # Show revisions
helm status my-app           # Release status
helm template ./chart        # Render YAML without deploying
helm install my-app ./chart --dry-run  # Simulate
```

## Value Override Priority
```
--set (highest) > -f values-prod.yaml > values.yaml (lowest)
```

## Chart.yaml
```yaml
apiVersion: v2              # Helm 3 uses v2
name: ehr-chart
version: 1.0.0              # Chart version
appVersion: "2.0.0"         # Your app's version
```

## Helm 2 vs Helm 3
- Helm 3 **removed Tiller** (security risk in Helm 2)
- Helm 3 stores releases in **Secrets** (not ConfigMaps)
- Helm 3 uses Chart apiVersion **v2**

---

# SESSION 16: Monitoring & Logging

## Monitoring vs Logging
| | Monitoring | Logging |
|--|-----------|---------|
| What | Numbers (CPU: 45%) | Text (ERROR: NullPointer) |
| Answers | "Is the app healthy?" | "Why did it crash?" |
| Tools | Prometheus + Grafana | Elasticsearch + Fluentd + Kibana |
| Analogy | Car dashboard | Car black box |

## Monitoring Stack

### Metrics Server — Basic
```bash
minikube addons enable metrics-server
kubectl top pods
kubectl top nodes
```

### Prometheus — Data Collector
- **PULL model** — scrapes /metrics from pods every 15s
- Stores as time-series data
- Query with **PromQL**

### Grafana — Dashboard
- Connects to Prometheus as data source
- Beautiful graphs, charts, gauges
- Set up alerts (Slack/Email)

### AlertManager — Notifications
```
Prometheus detects issue → AlertManager → Slack/Email/PagerDuty
```

## Logging Stack (EFK)
```
F = Fluentd        → COLLECTS logs (runs as DaemonSet, one per node)
E = Elasticsearch  → STORES logs
K = Kibana         → VIEWS/searches logs
```

## Install with Helm
```bash
helm install monitoring prometheus-community/kube-prometheus-stack \
  --namespace monitoring --create-namespace
```

---

# SESSION 17: Auto Scaling

## 3 Types of Auto Scaling

### HPA — Horizontal Pod Autoscaler (Most Important!)
- Scales **number of pods**
- Needs: Metrics Server + resource requests
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: ehr-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: ehr-app
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 50
```

**HPA Formula:**
```
Desired Replicas = Current Replicas × (Current Metric / Target Metric)
Example: 3 × (90% / 50%) = 3 × 1.8 = 5.4 → 6 pods
```

```bash
kubectl autoscale deployment ehr-app --min=2 --max=10 --cpu-percent=50
kubectl get hpa
```

### VPA — Vertical Pod Autoscaler
- Scales **pod resources** (CPU/Memory per pod)
- **Restarts pods** to apply new resources
- Best for databases
- Modes: Off (recommend only), Initial (first create), Auto (restart pods)

### Cluster Autoscaler / Karpenter
- Scales **nodes** (EC2 instances)
- Triggers on **Pending pods** (no space on existing nodes)
- Cloud providers only (EKS, GKE, AKS)

## Cooldown Periods
- Scale UP: **3 minutes** (react fast)
- Scale DOWN: **5 minutes** (react slowly, avoid flapping)

## When to Use
| Autoscaler | Best For |
|-----------|----------|
| HPA | Stateless apps (APIs, web servers) |
| VPA | Stateful apps (databases) |
| CA/Karpenter | When nodes are full |

---

# SESSION 18: CI/CD with Kubernetes

## CI/CD Pipeline
```
CI (Continuous Integration):
Code Push → Checkout → Build → Test → Docker Build → Push Image

CD (Continuous Deployment):
Pull Image → Update K8s → Deploy → Verify
```

## GitHub Actions Pipeline
- File location: `.github/workflows/deploy.yaml`
- Triggers on push to main branch
- Uses `${{ github.sha }}` for unique image tags
- Uses `${{ secrets.DOCKER_PASSWORD }}` for secrets

## Jenkins Pipeline
- File: `Jenkinsfile`
- `post { failure {} }` block for automatic rollback on failure

## GitOps (ArgoCD)
- **Git = single source of truth**
- ArgoCD watches Git → auto-syncs K8s cluster to match
- Traditional: pipeline **pushes** to K8s
- GitOps: ArgoCD **pulls** from Git
- Rollback = `git revert` → ArgoCD auto-syncs

## Image Tagging
```
Git SHA:    ehr-app:a1b2c3d    ✅ (unique per commit)
Semver:     ehr-app:v2.1.0     ✅ (releases)
latest:     ehr-app:latest     ❌ NEVER in production!
```

## Secrets in CI/CD
- GitHub Actions: `Settings → Secrets`
- Jenkins: `Credentials Manager`
- NEVER hardcode in pipeline files!

---

# SESSION 19: Service Mesh (Istio)

## What is Service Mesh?
- Infrastructure layer for service-to-service communication
- Handles encryption, routing, retries, observability
- **All without changing application code!**

## Architecture
```
Control Plane: istiod (Pilot + Citadel + Galley)
Data Plane: Envoy sidecar proxy in every pod
```

## Sidecar Pattern
- Every pod gets an Envoy proxy container automatically injected
- App → Envoy → (encrypted) → Envoy → App

## Key Features

### mTLS — Automatic Encryption
```yaml
apiVersion: security.istio.io/v1beta1
kind: PeerAuthentication
metadata:
  name: default
spec:
  mtls:
    mode: STRICT    # STRICT | PERMISSIVE | DISABLE
```

### VirtualService — Traffic Routing
```yaml
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
spec:
  http:
    - route:
        - destination:
            host: app
            subset: v1
          weight: 90        # 90% to v1
        - destination:
            host: app
            subset: v2
          weight: 10        # 10% to v2 (canary!)
      retries:
        attempts: 3
      timeout: 10s
```

### DestinationRule — Define Subsets
```yaml
apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
spec:
  host: app
  subsets:
    - name: v1
      labels:
        version: v1
    - name: v2
      labels:
        version: v2
```

### AuthorizationPolicy — Access Control
```yaml
apiVersion: security.istio.io/v1beta1
kind: AuthorizationPolicy
spec:
  selector:
    matchLabels:
      app: patient-service
  rules:
    - from:
        - source:
            principals: ["cluster.local/ns/default/sa/ehr-api"]
```

## Enable Sidecar Injection
```bash
kubectl label namespace default istio-injection=enabled
# Pods will show 2/2 containers (app + envoy)
```

## Use When
- 10+ microservices
- Need mTLS, complex routing, distributed tracing
- Don't use for simple apps (overkill)

---

# SESSION 20: Multi-Cluster & Production Best Practices

## Multi-Cluster Strategies
| Strategy | Description |
|----------|------------|
| Environment Separation | Dev / Staging / Prod clusters |
| Regional | US / EU / Asia clusters |
| Active-Active | Both serve traffic |
| Active-Passive | Standby takes over on failure |

## Production Checklist

### 1. Resource Requests & Limits — ALWAYS
```yaml
resources:
  requests:
    cpu: "200m"
    memory: "256Mi"
  limits:
    cpu: "500m"
    memory: "512Mi"
```

### 2. Health Probes — ALWAYS
- Liveness + Readiness + Startup probes

### 3. Multiple Replicas
- Never run 1 replica in production, minimum 3

### 4. Pod Disruption Budget
```yaml
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: app-pdb
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: ehr-app
```

### 5. Namespace Isolation + ResourceQuota

### 6. Specific Image Tags (never :latest)

### 7. Secrets Management (K8s Secrets / Vault)

### 8. Network Policies (default deny)

### 9. Rolling Update with maxUnavailable: 0

### 10. HPA Auto Scaling

### 11. Monitoring (Prometheus + Grafana)

### 12. Logging (EFK Stack)

### 13. RBAC — Least Privilege

### 14. Ingress with TLS

### 15. Backup with Velero + etcd Snapshots

---

# SESSION 21: Custom Resources & Operators

## CRD — Custom Resource Definition
- Teaches K8s about new resource types
- apiVersion: `apiextensions.k8s.io/v1`

```yaml
apiVersion: apiextensions.k8s.io/v1
kind: CustomResourceDefinition
metadata:
  name: mysqlclusters.databases.mycompany.com
spec:
  group: databases.mycompany.com
  versions:
    - name: v1
      served: true
      storage: true
      schema:
        openAPIV3Schema:
          type: object
          properties:
            spec:
              type: object
              properties:
                replicas:
                  type: integer
  scope: Namespaced
  names:
    plural: mysqlclusters
    singular: mysqlcluster
    kind: MySQLCluster
    shortNames:
      - mysql
```

## CR — Custom Resource (Instance)
```yaml
apiVersion: databases.mycompany.com/v1
kind: MySQLCluster
metadata:
  name: ehr-database
spec:
  replicas: 3
  version: "8.0"
```

## Operator — The Brain
- Custom controller that watches CRs and takes action
- **Reconciliation Loop:** Observe → Compare → Act → Repeat

## CRD vs CR vs Operator
| | CRD | CR | Operator |
|--|-----|-----|---------|
| Role | Blueprint/Schema | Instance/Data | Logic/Brain |
| Analogy | Table structure | Table row | Stored procedure |

## Popular Operators
- MySQL Operator, PostgreSQL Operator, Strimzi (Kafka)
- Cert-Manager, Prometheus Operator, ArgoCD

## Operator Maturity Levels
- Level 1: Basic install
- Level 2: Seamless upgrades
- Level 3: Full lifecycle (backup, restore)
- Level 4: Deep insights (metrics, alerts)
- Level 5: Auto-pilot (auto-scaling, self-healing)

---

# SESSION 22: Real-World Project — Full EHR Deployment

## Deployment Order
```bash
# 1. Foundation
kubectl apply -f namespace.yaml
kubectl apply -f resource-quota.yaml
kubectl apply -f limit-range.yaml

# 2. Configuration
kubectl apply -f configmap.yaml
kubectl apply -f secrets.yaml

# 3. Security
kubectl apply -f rbac.yaml

# 4. Database (must be ready before app)
kubectl apply -f mysql-headless-service.yaml
kubectl apply -f mysql-statefulset.yaml

# 5. Application
kubectl apply -f ehr-deployment.yaml
kubectl apply -f ehr-service.yaml

# 6. External Access
kubectl apply -f ingress.yaml

# 7. Production Safeguards
kubectl apply -f hpa.yaml
kubectl apply -f pdb.yaml
kubectl apply -f network-policy.yaml
```

## With Helm (One Command)
```bash
helm install ehr-production ./ehr-helm-chart \
  -f values-prod.yaml \
  --namespace ehr-production \
  --create-namespace
```

---

# API VERSION REFERENCE

| apiVersion | Resources |
|-----------|-----------|
| `v1` | Pod, Service, ConfigMap, Secret, Namespace, PV, PVC, ServiceAccount, LimitRange, ResourceQuota |
| `apps/v1` | Deployment, ReplicaSet, StatefulSet, DaemonSet |
| `batch/v1` | Job, CronJob |
| `networking.k8s.io/v1` | Ingress, NetworkPolicy |
| `rbac.authorization.k8s.io/v1` | Role, ClusterRole, RoleBinding, ClusterRoleBinding |
| `autoscaling/v2` | HorizontalPodAutoscaler |
| `policy/v1` | PodDisruptionBudget |
| `storage.k8s.io/v1` | StorageClass |
| `apiextensions.k8s.io/v1` | CustomResourceDefinition |

---

# EKS CLUSTER ADD-ONS (Your Company)

| Add-on | Purpose |
|--------|---------|
| **Karpenter** | Auto-scales EC2 nodes for pending pods |
| **Nginx Ingress** | Routes external traffic to services |
| **Cert Manager** | Auto-creates and renews SSL certificates |
| **External DNS** | Auto-creates Route 53 DNS records |
| **Metrics Server** | Enables kubectl top and HPA |
| **EBS CSI** | Creates AWS EBS volumes for PVCs |
| **Internal Ingress** | Routes private/internal traffic only |
| **OIDC** | SSO authentication (WHO are you?) + RBAC (WHAT can you do?) |
| **GitOps Agent** | Auto-syncs K8s cluster to match Git repo |

---

# KUBECTL CHEAT SHEET

```bash
# Cluster
kubectl cluster-info
kubectl config get-contexts
kubectl config use-context <name>

# Create/Apply
kubectl apply -f <file.yaml>
kubectl create ns <namespace>

# Get
kubectl get pods/deployments/services/nodes
kubectl get pods -n <namespace>
kubectl get pods -o wide
kubectl get all -n <namespace>
kubectl get hpa

# Describe
kubectl describe pod/deployment/service <name>

# Logs
kubectl logs <pod>
kubectl logs <pod> -f
kubectl logs <pod> --previous
kubectl logs <pod> -c <container>

# Exec
kubectl exec -it <pod> -- /bin/sh

# Scale
kubectl scale deployment <name> --replicas=5
kubectl autoscale deployment <name> --min=2 --max=10 --cpu-percent=50

# Rollout
kubectl rollout status deployment/<name>
kubectl rollout history deployment/<name>
kubectl rollout undo deployment/<name>
kubectl rollout undo deployment/<name> --to-revision=2

# Delete
kubectl delete pod/deployment/service <name>
kubectl delete -f <file.yaml>
kubectl delete ns <namespace>

# Resource Usage
kubectl top pods
kubectl top nodes
```

---

# SCORE TRACKER

| Session | Topic | Score |
|---------|-------|-------|
| 3 | Pods | 53% |
| 3 (Rev) | Pods Revision | 95% |
| 4 | ReplicaSets & Deployments | 71% |
| 5 | Services & Networking | 70% |
| 6 | Namespaces & Resources | 85% |
| 7 | ConfigMaps & Secrets | 90% |
| 8 | Volumes & Storage | 85% |
| 9 | Jobs, CronJobs, DaemonSets | 90% |
| 10 | Ingress | 85% |
| 11 | Health Probes | 100% |
| 12 | Rolling Updates | 90% |
| 13 | RBAC & Security | 95% |
| 14 | StatefulSets | 85% |
| 15 | Helm | 75% |
| 16 | Monitoring & Logging | 90% |
| 17 | Auto Scaling | 90% |
| 18 | CI/CD with Kubernetes | 95% |
| 19 | Service Mesh (Istio) | Skipped |
| 20 | Multi-Cluster & Production | 100% |
| 21 | Custom Resources & Operators | 100% |
| 22 | Final — Full Deployment | 90% |

**Journey: 53% → 100% | Average: ~87%**
