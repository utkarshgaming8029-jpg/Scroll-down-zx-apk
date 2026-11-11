# 🚀 NEXORA – Connect Beyond Limits
### Production-Ready Native Android Social Media Application

**Application ID:** `com.nexora.social`  
**Platform:** Android (Min SDK 24, Target SDK 36)  
**Tech Stack:** Kotlin, Jetpack Compose, Material 3, AndroidX, Coroutines & Flow, Coil, Google Firebase Architecture

---

## 📱 Features Overview

- **Splash Screen:** Animated glowing Nexora 'N' insignia with brand tagline.
- **Authentication & Registration:** Sign-in, sign-up forms with validation and one-tap **Instant Demo Mode** for immediate testability on any device.
- **Home Feed:**
  - Interactive Stories tray (add own story, view other creators' stories with gradient rings).
  - High-fidelity posts with media rendering, hashtags, locations, and author info.
  - Interactive Actions: Like (with animated counters), Save/Bookmark, Share, and Comments.
  - Post options menu for Report and Block actions.
- **Stories Viewer:** Fullscreen story experience with segmented auto-progress bars, tap navigation (left/right), pause, heart reaction, and direct reply bar.
- **Explore & Search:**
  - Real-time creator, tag, and content search bar.
  - Trending hashtags section with post counts.
  - Filter chips (*All, Trending, Tech, Cyberpunk, Design, Gaming, Photography, Sound*).
  - Multi-column visual discovery media grid.
- **Reels (Short Videos):**
  - Immersive vertical scrolling short-form video feed.
  - Double-tap anywhere on screen to trigger the floating heart animation.
  - Spinning vinyl sound disc with dynamic track information.
  - Quick action sidebar: Like, Comment, Share, and Save.
- **Post Creator:**
  - Visual asset selector with high-res presets.
  - Live caption composer.
  - Interactive hashtag chips and location tagging.
  - Instant live feed injection upon publishing.
- **Direct Messaging (DMs):**
  - Conversation list with live active status indicators and unread badges.
  - Chat room with real-time send/receive messaging, message bubbles, and timestamps.
- **Activity & Notifications:**
  - Activity stream for likes, comments, mentions, and new followers.
  - Category filters (*All, Follows, Mentions*).
  - One-tap "Mark all as read".
- **User Profiles:**
  - Header cover gradient with circular glowing avatar and verified badge.
  - Real-time statistics: Posts count, Followers, Following.
  - Bio, website link, and "Edit Profile" dialog.
  - Three-tab switcher: Posts grid, Reels, and Saved bookmarks.
- **Settings, Safety & Moderation:**
  - Interactive Demo Mode toggle.
  - Firebase Cloud Backend status & step-by-step setup guide.
  - Moderation center with active reports and blocked users list.
  - Account privacy and push notification preferences.
  - One-tap Cache Cleaner and secure Log Out.

---

## 🛠️ Mobile-Only Guide (No PC or Android Studio Required)

Because you are using an Android smartphone, you can build, upload, and install Nexora directly using GitHub and GitHub Actions.

### 1. Uploading Nexora from an Android Phone to GitHub

1. Open your mobile browser (e.g. Chrome) and navigate to **[github.com](https://github.com)**.
2. Sign in to your GitHub account.
3. Tap the **+** (plus icon) in the top right and select **New repository**.
4. Name your repository: `nexora-social` (choose **Public** or **Private**).
5. Leave "Add a README file" unchecked and tap **Create repository**.
6. On the empty repository page, tap **"uploading an existing file"**.
7. If downloading as a ZIP from AI Studio:
   - Extract the ZIP on your phone using your file manager or an app like *ZArchiver*.
   - Upload the project files or push them via mobile Git client (like **Termux** or **MGit**).
   - If using **Termux** on Android:
     ```bash
     git init
     git remote add origin https://github.com/YOUR_USERNAME/nexora-social.git
     git branch -M main
     git add .
     git commit -m "Initial production release of Nexora"
     git push -u origin main
     ```

---

## ⚙️ Automated Cloud APK Build (GitHub Actions)

Nexora includes an automated CI workflow at `.github/workflows/android-apk.yml`. Every time you push to the `main` branch, GitHub builds the APK on Ubuntu cloud runners.

### How to Download the Built APK from GitHub Actions:

1. On your phone's browser, open your GitHub repository: `https://github.com/YOUR_USERNAME/nexora-social`.
2. Tap the **Actions** tab at the top.
3. Tap the latest workflow run labeled **"Build Nexora Android APK"**.
4. Scroll down to the **Artifacts** section at the bottom of the page.
5. Tap **nexora-debug-apk** (ZIP file containing `app-debug.apk`).
6. Once downloaded:
   - Extract `app-debug.apk` using your phone's Files app.
   - Tap `app-debug.apk` to install on your Android device.
   - If prompted, toggle **"Allow from this source"**.

---

## 🏷️ Creating a GitHub Release with APK Attached (From Your Phone)

You can trigger a formal GitHub Release directly from the GitHub Actions UI:

1. In your GitHub repository on mobile, go to the **Actions** tab.
2. Under "Workflows" on the left menu (or dropdown), tap **Build Nexora Android APK**.
3. Tap **Run workflow** (dropdown button).
4. Check the box: **"Create a GitHub Release with APK attached?"**
5. Enter a tag name (e.g. `v1.0.0`).
6. Tap the green **Run workflow** button.
7. Wait ~2–3 minutes for the build to finish.
8. Go to the **Releases** tab on your repository to find `Nexora Social - Release v1.0.0` with `app-debug.apk` ready for direct 1-tap download!

---

## 🔥 Firebase Cloud Backend Configuration Guide

Nexora operates in **Demo Mode** out of the box with zero external configuration required. To link your own live Google Firebase backend:

### Step 1: Create a Project in Firebase Console
1. Navigate to **[console.firebase.google.com](https://console.firebase.google.com)** on your browser.
2. Tap **Add project** and enter a name (e.g., `Nexora-Social`).
3. Turn off or on Google Analytics as preferred, then tap **Create project**.

### Step 2: Register the Android App
1. On the Firebase Project Overview page, tap the **Android icon** (or "Add App" > Android).
2. For **Android package name**, enter verbatim:
   ```
   com.nexora.social
   ```
3. For App nickname: `Nexora Android`.
4. Tap **Register app**.

### Step 3: Add `google-services.json`
1. Download `google-services.json` provided in Step 2 of the Firebase setup wizard.
2. Place this file into the root of the `/app/` directory:
   ```
   app/google-services.json
   ```

### Step 4: Enable Firebase Services
1. **Firebase Authentication:**
   - In the Firebase sidebar, go to **Build** > **Authentication**.
   - Tap **Get Started** and enable **Email/Password** and/or **Google**.
2. **Cloud Firestore:**
   - In the sidebar, go to **Build** > **Firestore Database**.
   - Tap **Create database** > Start in **Test mode** (or apply production rules).
3. **Cloud Storage (Optional for media uploads):**
   - Go to **Build** > **Storage** > **Get Started**.

Once `app/google-services.json` is added and committed, the Gradle Google Services plugin will bind your live credentials on every build!

---

## 🛡️ Security & Privacy Architecture

- **No Hardcoded Secrets:** No sensitive passwords or API keys are committed to source control.
- **Input Sanitization:** User captions, messages, and report text are trimmed and validated.
- **User Safety First:** Native reporting workflow with categorical flagging and account blocking.
- **Edge-to-Edge:** Native compliance with Android 15 & 16 gesture bars and notch insets.
