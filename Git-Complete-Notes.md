# Git & GitHub — Complete Notes (Beginner to Advanced)
### Author: Devamatha | Sessions: 16 | Score: 85.8% (A-)

---

## REVISE FIRST — Your Weak Areas (From Test Results)

### Weak Area 1: reset vs revert vs restore (Session 7 — 73%)

```
restore = for FILES          "I want to undo my EDIT"
reset   = for LOCAL commits  "I committed but NOT pushed"
revert  = for PUSHED commits "I committed AND pushed"
```

```
reset --soft  = SAFE    (commit gone, code still STAGED)
reset --mixed = MEDIUM  (commit gone, code UNSTAGED)
reset --hard  = DANGER  (commit gone, code DELETED FOREVER!)
```

```
reset  = Time machine     → REWRITES history  (local only!)
revert = Apology letter   → PRESERVES history  (safe for team!)
```

```
Safety order: git revert → git reset --soft → git reset --hard
              (safest)                          (most dangerous)
```

### Weak Area 2: Branching Strategies (Session 10 — 60%)

```
Git Flow    = COMPLEX  | 5 branches | has "develop" + "hotfix" + "release"
GitHub Flow = SIMPLE   | 2 rules    | only main + feature branches
GitLab Flow = PIPELINE | environments | main → staging → production
Trunk-Based = FAST     | tiny branches | hours only, expert teams
```

```
Git Flow details:
  - feature branches come from DEVELOP (not main)
  - hotfix = emergency fix (not feature)
  - hotfix merges into BOTH main AND develop
  - GitHub Flow has only 2 rules (not 5)
```

```
Small team + web app?          → GitHub Flow
Large team + versions?         → Git Flow
Multiple environments?         → GitLab Flow
Expert team + strong CI/CD?    → Trunk-Based
```

---

# PAGE 1 — Level 1: Sessions 1 & 2

---

## Session 1: Git Basics

**Git** = A tool that tracks changes in your code over time (version control).

**Repository (Repo)** = A project folder tracked by Git.
- **Local repo** = On your computer
- **Remote repo** = On GitHub/GitLab server

**.git/ folder** = Hidden folder where Git stores all version history. Never delete it.

### Essential Commands

| Command | What it Does |
|---------|-------------|
| `git --version` | Check Git is installed |
| `git config --global user.name "Name"` | Set your name (one-time) |
| `git config --global user.email "email"` | Set your email (one-time) |
| `git init` | Create a new repository |
| `git status` | Check current state of files |

### The 3 Stages of Git

```
Working Directory  →  Staging Area  →  Repository
  (your edits)       (git add)        (git commit)
     RED               GREEN           SAVED
```

### git status output

| Symbol | Meaning |
|--------|---------|
| `modified` (red) | File changed, NOT staged |
| `untracked` (red) | New file, Git doesn't know it |
| `new file` (green) | Staged, ready to commit |

---

## Session 2: Core Workflow

### The Golden Flow: edit → add → commit

| Command | What it Does |
|---------|-------------|
| `git add file.txt` | Stage one file |
| `git add .` | Stage ALL files |
| `git commit -m "message"` | Save staged files with a label |
| `git log --oneline` | View commit history (short) |
| `git log --oneline -5` | View last 5 commits |

### .gitignore — Files Git Should Never Track

```
target/          # Build output
.idea/           # IDE settings
*.log            # Log files
.env             # Secrets/passwords
node_modules/    # Dependencies
```

### Good Commit Messages

```
BAD:  "changes", "update", "asdf"
GOOD: "fix login crash when password is empty"
GOOD: "add JWT authentication to user API"
```

---

# PAGE 2 — Level 1: Sessions 3 & 4

---

## Session 3: Branching Basics

**Branch** = A separate line of development (work without affecting main).

```
main ──────────────────────────── (stable code)
       \                       /
        ● ── ● ── ● ── ● ── ●   (feature work)
        create   work       merge back
```

### Commands

| Command | What it Does |
|---------|-------------|
| `git branch` | List all branches |
| `git branch feature-x` | Create new branch |
| `git switch feature-x` | Switch to branch |
| `git switch -c feature-x` | Create + switch (shortcut) |
| `git switch main` | Go back to main |
| `git merge feature-x` | Merge branch into current branch |
| `git branch -d feature-x` | Delete merged branch |
| `git branch -D feature-x` | Force delete (even if not merged) |

### Branch Naming: `feature-login`, `bugfix-header`, `release-v1.0`

### Golden Rule: Never work directly on main. Always create a branch.

---

## Session 4: Remote Repositories

**Remote** = Your repo on the internet (GitHub/GitLab). Default name: `origin`.

```
push  = UPLOAD   (local → GitHub)
pull  = DOWNLOAD (GitHub → local)
clone = COPY     (first time only)
fetch = PEEK     (check without merging)
```

### Commands

| Command | What it Does |
|---------|-------------|
| `git remote -v` | See remote connection |
| `git remote add origin <url>` | Connect to GitHub |
| `git clone <url>` | Download full project |
| `git push` | Upload changes to GitHub |
| `git push -u origin branch-name` | Push NEW branch to GitHub |
| `git pull` | Download + merge latest changes |
| `git fetch` | Check for updates (no merge) |

### fetch vs pull

```
git fetch = Check mailbox (just look)     — safe
git pull  = Check + open + read (merge)   — can cause conflicts
```

### HTTPS vs SSH

```
HTTPS: https://github.com/user/repo.git  (username + token)
SSH:   git@github.com:user/repo.git      (SSH key, no password)
```

### Local branch not on GitHub? You never pushed it!
```
git push -u origin branch-name    # Push it to GitHub
```

---

# PAGE 3 — Level 2: Sessions 5 & 6

---

## Session 5: Merge & Conflicts

### 3 Types of Merge

| Type | When | Result |
|------|------|--------|
| **Fast-forward** | Main has no new commits | Pointer moves forward, no merge commit |
| **3-way merge** | Both branches have changes | Creates a merge commit |
| **Conflict** | Same line changed differently | YOU must resolve manually |

### Conflict Markers

```
<<<<<<< HEAD
  YOUR code (current branch)
=======
  THEIR code (incoming branch)
>>>>>>> branch-name
```

### Resolving Conflicts

```
Step 1: Open conflicted file
Step 2: Choose what to keep (yours / theirs / combine)
Step 3: Remove ALL markers (<<<, ===, >>>)
Step 4: git add .
Step 5: git commit -m "resolve conflict"
```

### git diff

| Command | Shows |
|---------|-------|
| `git diff` | Unstaged changes |
| `git diff --staged` | Staged changes |
| `git diff main..feature` | Difference between branches |

```
- line (red)   = removed/old
+ line (green) = added/new
  (no symbol)  = unchanged
```

---

## Session 6: Collaboration Workflow

### Fork vs Clone

```
Fork  = Copy repo to YOUR GitHub account  (server → server)
Clone = Download repo to YOUR computer    (server → computer)
```

### Pull Request (PR) = Request to review & merge your code

```
PR ≠ direct merge
PR = merge + review + discussion + approval
```

**GitHub: Pull Request** | **GitLab: Merge Request** (same concept)

### PR Workflow

```
Create branch → Code → Add → Commit → Push → Create PR → Review → Merge
```

### Code Review — 3 Actions

| Action | Meaning |
|--------|---------|
| **Approve** | Code looks good, merge it |
| **Request Changes** | Fix issues first |
| **Comment** | Question or suggestion |

### Push more commits → PR updates automatically (no new PR needed)

### Auto-close Issues: Write `Fixes #1` or `Closes #5` in PR description

### 3 Merge Options on GitHub

| Option | What it Does |
|--------|-------------|
| **Merge commit** | Keeps all commits (default) |
| **Squash and merge** | Combines into ONE commit (cleanest) |
| **Rebase and merge** | Replays commits linearly |

---

# PAGE 4 — Level 2: Sessions 7 & 8

---

## Session 7: Stashing & Undoing

### git stash — Save work temporarily

| Command | What it Does |
|---------|-------------|
| `git stash` | Save current work to shelf |
| `git stash -m "message"` | Save with description |
| `git stash list` | See all stashes |
| `git stash pop` | Restore + remove from shelf |
| `git stash apply` | Restore + keep on shelf |
| `git stash drop` | Delete one stash |
| `git stash clear` | Delete ALL stashes |

### git restore — Discard changes

| Command | What it Does |
|---------|-------------|
| `git restore file.txt` | Discard changes (PERMANENT!) |
| `git restore --staged file.txt` | Unstage (undo git add) |
| `git restore .` | Discard ALL changes |

### git reset — Undo commits (LOCAL only)

```
--soft  = Commit gone, code STAGED       (safe)
--mixed = Commit gone, code UNSTAGED     (default)
--hard  = Commit gone, code DELETED      (DANGEROUS!)
```

```
git reset --soft HEAD~1    # Undo last commit, keep staged
git reset --hard HEAD~1    # Undo last commit, DELETE everything
```

### git revert — Undo commits (PUSHED/shared)

```
git revert HEAD    # Creates a NEW commit that undoes the last one
                   # History is PRESERVED (safe for team)
```

### DECISION CHART: Which undo command?

```
Haven't added yet?        → git restore file.txt
Added but not committed?  → git restore --staged file.txt
Committed but NOT pushed? → git reset --soft HEAD~1
Committed AND pushed?     → git revert HEAD
Need to save work quickly?→ git stash
```

### KEY RULE:
```
restore = for FILES (eraser)
reset   = for LOCAL commits (time machine)
revert  = for PUSHED commits (apology letter)
```

---

## Session 8: Git Log & History

| Command | What it Shows | Analogy |
|---------|-------------|---------|
| `git log` | Commit history | History book |
| `git blame` | Who changed each line | Detective |
| `git show` | Details of one commit | Magnifying glass |
| `git reflog` | ALL actions (even deleted) | Security camera |

### git log variations

| Command | What it Shows |
|---------|-------------|
| `git log --oneline` | Short format |
| `git log --oneline --graph --all` | Visual branch diagram |
| `git log --author="name"` | Filter by person |
| `git log --after="2026-01-01"` | Filter by date |
| `git log --grep="keyword"` | Search by message |
| `git log --stat` | Show files changed |

### git blame

```bash
git blame file.txt           # Who changed each line
git blame file.txt -L 10,20  # Blame lines 10-20 only
```

### git reflog — Recovery safety net

```bash
git reflog                        # Find lost commit
git reset --hard <commit-id>      # Recover it!
```

---

# PAGE 5 — Level 3: Sessions 9 & 10

---

## Session 9: Rebasing

**Rebase** = Replay your commits on top of the latest main (clean straight line).

```
MERGE result (messy):
  main:    A ── B ── C ── D ── M
                      \       /
  feature:             E ── F       (diamond shape)

REBASE result (clean):
  main:    A ── B ── C ── D
                            \
  feature:              D ── E' ── F'  (straight line)
```

### Commands

```bash
git switch feature-x       # Go to your branch
git rebase main            # Rebase onto latest main
git rebase --continue      # Continue after fixing conflict
git rebase --abort         # Cancel rebase
git push --force           # Force push after rebase (history changed)
```

### Interactive Rebase — Clean up messy commits

```bash
git rebase -i HEAD~5       # Edit last 5 commits
```

| Keyword | What it Does |
|---------|-------------|
| `pick` | Keep as it is |
| `squash` | Combine with commit above |
| `reword` | Change commit message only |
| `drop` | Delete the commit |
| `edit` | Pause and modify |

### Merge vs Rebase

| | Merge | Rebase |
|--|-------|--------|
| History | Messy (diamond) | Clean (straight line) |
| Creates merge commit | Yes | No |
| Safe for shared branches | Yes | NO |
| Use when | Shared branches | Private/local branches |

### GOLDEN RULE: Never rebase a branch others are using!

---

## Session 10: Branching Strategies

### 4 Strategies

| Strategy | One-Line Summary | Best For |
|----------|-----------------|----------|
| **Git Flow** | 5 branches (main, develop, feature, release, hotfix) | Large teams, versioned releases |
| **GitHub Flow** | 2 rules: main + feature branches, PR, deploy | Small teams, web apps |
| **GitLab Flow** | Environment branches (main→staging→production) | Multiple environments |
| **Trunk-Based** | Everyone on main, tiny branches for hours | Expert teams, fast CI/CD |

### Git Flow — 5 Branch Types

```
main      = Production (live for users)
develop   = All development happens here
feature/* = Individual features (from develop)
release/* = Preparing for release
hotfix/*  = Emergency fix (from main, merge to BOTH main AND develop)
```

### GitHub Flow — 2 Rules

```
Rule 1: main is ALWAYS deployable
Rule 2: Branch → PR → Review → Merge → Deploy
```

### Decision:

```
Small team + web app?           → GitHub Flow
Large team + versions?          → Git Flow
Multiple environments?          → GitLab Flow
Expert team + fast deployment?  → Trunk-Based
```

---

# PAGE 6 — Level 3: Sessions 11 & 12

---

## Session 11: Cherry-Pick, Tags & Releases

### Cherry-Pick — Pick ONE commit from another branch

```bash
git switch main                  # Go to target branch
git cherry-pick abc1234          # Copy that ONE commit here
git cherry-pick --abort          # Cancel if conflict
git cherry-pick --continue       # Continue after fixing conflict
```

```
Merge       = Take ALL fruits from tree
Cherry-pick = Pick ONLY the one cherry you want
```

### Tags — Mark versions

```bash
git tag v1.0                             # Lightweight tag
git tag -a v1.0 -m "First release"       # Annotated tag (recommended)
git tag -a v1.0 -m "msg" abc1234         # Tag an old commit
git tag                                  # List all tags
git push origin v1.0                     # Push one tag
git push origin --tags                   # Push ALL tags
git tag -d v1.0                          # Delete local tag
git push origin --delete v1.0            # Delete remote tag
```

### Semantic Versioning: vMAJOR.MINOR.PATCH

```
PATCH = Bug fix          (v1.0.0 → v1.0.1)
MINOR = New feature      (v1.0.0 → v1.1.0)
MAJOR = Breaking change  (v1.0.0 → v2.0.0)
```

### GitHub Releases = Tag + Description + Downloadable files (ZIP)

```
Push tag → GitHub repo → Releases → Create new release → Publish
```

---

## Session 12: Submodules & Subtrees

### Submodule = LINK (pointer) to another repo (rented TV)

```bash
git submodule add <url> <path>           # Add submodule
git clone --recurse-submodules <url>     # Clone with submodules
git submodule init && git submodule update  # Download submodule code
```

### Subtree = FULL COPY of another repo (purchased TV)

```bash
git subtree add --prefix=<path> <url> main --squash    # Add subtree
git subtree pull --prefix=<path> <url> main --squash   # Update subtree
```

### Comparison

| | Submodule | Subtree |
|--|-----------|---------|
| What | Link (pointer) | Full copy |
| Needs `.gitmodules` | Yes | No |
| Team cloning | Extra steps needed | Just `git clone` works |
| Best for | Shared library, one team manages | Simple inclusion, customize |

### Not sure which? → Use Subtree (simpler)

---

# PAGE 7 — Level 4: Sessions 13 & 14

---

## Session 13: GitHub Actions (CI/CD)

```
CI = Continuous Integration  → Auto TEST & BUILD on every push
CD = Continuous Deployment   → Auto DEPLOY after tests pass
```

### File location: `.github/workflows/ci.yml`

### YAML Structure — 4 Key Parts

```yaml
name: CI Pipeline              # 1. NAME

on:                             # 2. TRIGGER (when to run)
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:                           # 3. JOBS (what to do)
  build:
    runs-on: ubuntu-latest      # GitHub-hosted machine

    steps:                      # 4. STEPS (individual tasks)
      - uses: actions/checkout@v4        # Pre-built action
      - run: mvn clean package           # Shell command
```

### Key Concepts

| Concept | Meaning |
|---------|---------|
| `uses:` | Pre-built action (plug & play) |
| `run:` | Your own shell command |
| `needs: test` | Wait for "test" job to finish first |
| `${{ secrets.NAME }}` | Access hidden passwords |
| `on: workflow_dispatch` | Manual trigger (button click) |

### CI/CD Flow Order: Build → Test → Push Image → Deploy

### Docker + CI: ci.yml calls → docker-compose → Dockerfile

```
ci.yml     = BOSS (when & what)
compose    = MANAGER (run containers)
Dockerfile = WORKER (build one container)
```

---

## Session 14: GitLab CI/CD

### File: `.gitlab-ci.yml` (in project ROOT)

### Same pipeline, different syntax:

| Concept | GitHub Actions | GitLab CI |
|---------|---------------|-----------|
| File | `.github/workflows/*.yml` | `.gitlab-ci.yml` |
| Commands | `run:` | `script:` |
| Machine | `runs-on:` | `image:` |
| Job order | `needs:` | `stages:` |
| Triggers | `on:` | `only:` or `rules:` |
| Secrets | `${{ secrets.NAME }}` | `$VARIABLE_NAME` |
| Checkout | `uses: actions/checkout@v4` | Automatic (built-in) |

### GitLab CI Example

```yaml
stages:
  - build
  - test

image: maven:3.9-eclipse-temurin-17

build:
  stage: build
  script:
    - mvn clean package -DskipTests

test:
  stage: test
  script:
    - mvn test
```

### GitLab Keywords

| Keyword | What it Does |
|---------|-------------|
| `stages:` | Order of execution |
| `image:` | Docker image for job |
| `script:` | Commands to run |
| `only:` | Which branches trigger |
| `when: manual` | Run only on button click |
| `artifacts:` | Save files between stages |
| `cache:` | Cache dependencies |

### GitLab provides registry variables automatically ($CI_REGISTRY_USER, $CI_REGISTRY_PASSWORD)

---

# PAGE 8 — Level 4: Sessions 15 & 16

---

## Session 15: Advanced GitHub Features

### Protected Branches — Guard main from direct pushes

```
Settings → Branches → Add rule → main
  ☑ Require PR before merging
  ☑ Require approvals
  ☑ Require CI status checks to pass
```

### CODEOWNERS — Auto-assign reviewers

```
# .github/CODEOWNERS
*                    @Devamatha           # Default reviewer
*.java               @backend-team       # Java files
Dockerfile           @devops-team        # Docker files
```

### GitHub Pages — Free static website hosting

```
Settings → Pages → Source: main → Folder: /docs
URL: https://username.github.io/repo-name/
Can host: HTML, CSS, JS, docs, portfolio
Cannot host: Backend apps (Spring Boot, Node.js)
```

### Webhooks — GitHub notifies your server on events

```
Event on GitHub → HTTP request → Your server
Example: PR created → Slack notification
Like a doorbell — you only go when it rings
```

### GitHub CLI (gh) — GitHub from terminal

```bash
gh pr create --title "title" --body "desc"   # Create PR
gh pr list                                    # List PRs
gh pr merge 1                                 # Merge PR
gh issue create --title "Bug"                 # Create issue
gh issue list                                 # List issues
gh run list                                   # List CI runs
gh release create v1.0                        # Create release
```

### Environments — Deployment settings

```
development → staging → production
Each has: secrets, approval rules, wait timers
```

---

## Session 16: Git Internals & Troubleshooting

### 4 Git Object Types

```
BLOB   = File content        (pages of a book)
TREE   = Folder structure    (table of contents)
COMMIT = Snapshot            (library stamp)
TAG    = Label               (sticker on book)
```

### git bisect — Find bug-introducing commit (binary search)

```bash
git bisect start
git bisect bad                    # Current is broken
git bisect good abc1234           # This old commit was working
# Git checks middle commit → you test → say good/bad → repeat
git bisect reset                  # Done, exit bisect
```

### Maintenance

```bash
git fsck              # Health check (is data corrupted?)
git gc                # Garbage collection (clean up, optimize)
git gc --aggressive   # Deep clean (for large repos)
```

### git commit --amend — Fix last commit

```bash
git commit --amend -m "new message"       # Change message
git add file && git commit --amend --no-edit  # Add forgotten file
```

---

# PAGE 9 — Quick Reference: All Commands

---

## Most Used Commands (Daily)

```bash
git status                    # What changed?
git add .                     # Stage everything
git commit -m "message"       # Save snapshot
git push                      # Upload to GitHub
git pull                      # Download from GitHub
git switch -c branch-name     # Create & switch branch
git switch main               # Go back to main
git merge branch-name         # Merge branch into current
git log --oneline             # View history
git stash                     # Save work temporarily
git stash pop                 # Get it back
```

## Branch Commands

```bash
git branch                    # List branches
git branch -a                 # List all (local + remote)
git branch -d name            # Delete merged branch
git branch -D name            # Force delete branch
git push -u origin name       # Push new branch to GitHub
git push origin --delete name # Delete remote branch
```

## Undo Commands

```bash
git restore file.txt              # Discard file changes
git restore --staged file.txt     # Unstage (undo add)
git reset --soft HEAD~1           # Undo commit, keep staged
git reset --hard HEAD~1           # Undo commit, DELETE all
git revert HEAD                   # Safe undo (pushed commits)
git reflog                        # Find lost commits
```

## Inspection Commands

```bash
git log --oneline --graph --all   # Visual history
git log --author="name"           # By person
git log --grep="keyword"          # By message
git diff                          # See changes
git diff --staged                 # See staged changes
git blame file.txt                # Who changed each line
git show abc1234                  # Details of one commit
```

## Tag & Release Commands

```bash
git tag -a v1.0 -m "message"     # Create tag
git push origin v1.0              # Push tag to GitHub
git push origin --tags            # Push ALL tags
```

## Advanced Commands

```bash
git cherry-pick abc1234           # Pick one commit
git rebase main                   # Rebase onto main
git rebase -i HEAD~5              # Interactive rebase
git bisect start                  # Find bug with binary search
git stash -m "description"        # Stash with message
git commit --amend -m "new msg"   # Fix last commit message
```

---

# PAGE 10 — Troubleshooting & Cheat Sheet

---

## Common Problems & Solutions

| Problem | Solution |
|---------|----------|
| Committed to wrong branch | `git switch -c correct-branch` then `git switch main && git reset --hard HEAD~1` |
| Want to change last commit message | `git commit --amend -m "new message"` |
| Forgot to add file to commit | `git add file && git commit --amend --no-edit` |
| Need to undo a pushed commit | `git revert HEAD && git push` |
| Lost a commit (reset --hard) | `git reflog` → `git reset --hard <id>` |
| Deleted branch accidentally | `git reflog` → `git switch -c branch <id>` |
| Merge conflicts everywhere | `git merge --abort` (start fresh) |
| Detached HEAD | `git switch main` |
| Repo is slow/huge | `git gc --aggressive` |
| Git data corrupted | `git fsck` |
| Empty submodule folder | `git submodule init && git submodule update` |

## Key Differences to Remember

```
add vs commit:       add = select files, commit = save them
push vs pull:        push = upload, pull = download
fetch vs pull:       fetch = check only, pull = check + merge
merge vs rebase:     merge = messy history, rebase = clean history
reset vs revert:     reset = rewrite history, revert = preserve history
restore vs reset:    restore = undo files, reset = undo commits
-d vs -D:            -d = safe delete, -D = force delete
--soft vs --hard:    soft = keep code, hard = delete code
fork vs clone:       fork = copy to your GitHub, clone = download
submodule vs subtree: submodule = link, subtree = full copy
uses vs run (CI):    uses = pre-built action, run = shell command
```

## Semantic Versioning: vMAJOR.MINOR.PATCH

```
Bug fix     → PATCH (v1.0.0 → v1.0.1)
New feature → MINOR (v1.0.0 → v1.1.0)
Breaking    → MAJOR (v1.0.0 → v2.0.0)
```

## Branching Strategy Quick Pick

```
Small team + web app        → GitHub Flow
Large team + versions       → Git Flow
Multiple environments       → GitLab Flow
Expert team + fast CI/CD    → Trunk-Based
```

## CI/CD Quick Reference

```
GitHub: .github/workflows/ci.yml    |  GitLab: .gitlab-ci.yml
run:                                |  script:
runs-on:                            |  image:
needs:                              |  stages:
${{ secrets.NAME }}                 |  $VARIABLE_NAME
```

---

```
Course Completed: 16 Sessions | 12 Tests | Score: 85.8% | Grade: A-
```
