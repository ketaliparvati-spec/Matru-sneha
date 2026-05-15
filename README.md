# Matru-Sneh (ಮಾತೃ-ಸ್ನೇಹ) 🤰✨

**Matru-Sneh** is a production-ready Android application designed specifically to support expectant mothers in rural Karnataka, India. Built with a focus on accessibility, localization, and smart health tracking, it serves as a companion to ensure a safe and healthy pregnancy journey.

---

## 🌟 Key Features

### 🤖 AI Health Companion
Integrated with **Google Gemini (via OpenRouter)**, the app provides daily, context-aware health suggestions based on the mother's current pregnancy week, nutrition logs, and baby kick counts. The AI is localized to suggest Karnataka-specific foods like Ragi Mudde, Jowar Roti, and local greens.

### 📊 Dynamic Health Analytics
- **Kick Counter:** A simple, one-tap interface to record baby kicks.
- **7-Day History:** Visual representation of baby activity using high-fidelity bar charts (Vico Charts), helping mothers identify trends and stay reassured.

### 🥗 Karnataka-Centric Nutrition Tracker
Customized nutrition checklist focusing on local dietary habits:
- **Energy:** Ragi, Jowar, Anna-Saru.
- **Protein:** Togari Bele (Dal), Milk, Eggs.
- **Iron:** Soppu (Green Leaves), Jaggery.

### 🛡️ Safety First
- **Persistent Emergency Contacts:** Save and quick-dial numbers for Doctors, ASHA workers, and family.
- **Danger Signs Guide:** A simplified visual guide to identifying pregnancy red flags.
- **Checkup Reminders:** Schedule future clinic visits with local push notifications.

### 🌍 Localization
Full support for **English and Kannada (ಕನ್ನಡ)** with a quick-toggle switch on the dashboard for seamless accessibility.

---

## 🛠️ Technical Stack

- **UI:** Jetpack Compose (Modern, Declarative UI)
- **Database:** Room (Offline-first architecture for rural connectivity)
- **Networking:** HttpURLConnection with JSON parsing
- **AI:** OpenRouter (Gemini 1.5 Flash)
- **Charts:** Vico Charting Library
- **Background Tasks:** WorkManager (for reminders)
- **Theme:** Material 3 with a "Care-focused" Pink & Peach palette

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Iguana (or newer)
- Android SDK 34 (Upside Down Cake)
- OpenRouter API Key (placed in `AiSuggestionService.kt`)

### Build & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/ketaliparvati-spec/Matru-sneha.git
   ```
2. Open in Android Studio.
3. Sync Gradle and hit **Run**.

---

## 📂 Project Structure
- `data/local`: Room entities, DAOs, and Database configuration.
- `ui/screens`: Screen-specific Composable functions (Home, Health, etc.).
- `ui/viewmodel`: State management and business logic.
- `data/ai`: AI prompt engineering and service integration.

---

## 📜 License
This project is built for social impact and health awareness.

*Developed with care for mothers everywhere.* ❤️
