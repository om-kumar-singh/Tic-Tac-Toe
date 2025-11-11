# Tic-Tac-Toe Android App

A clean, classic Tic-Tac-Toe game built with Android Studio (Java), featuring a modern Light theme and adaptive app icon.

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)

## 📱 App Details
- **Package**: `com.example.tictactoev4`
- **Min SDK**: 21 (Android Lollipop)
- **Target SDK**: Latest Android
- **Build Tool**: Gradle (Kotlin DSL)

## 🎮 Features
- **Single-player mode** with basic AI opponent
- **Two-player mode** for local multiplayer
- **Clean, modern Light theme** UI
- **Adaptive app icon** for different device resolutions
- **Simple and intuitive** gameplay
- **Win detection** and game state management

## 🚀 Quick Start

### Prerequisites
- Android Studio (Giraffe or later recommended)
- Android SDK 21+
- Java Development Kit

### Run Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/om-kumar-singh/Tic-Tac-Toe.git
   ```

2. Open the project in Android Studio

3. Let Gradle sync dependencies automatically

4. Run the `app` module on an Android emulator or physical device

### Direct APK Download
[![Download APK](https://img.shields.io/badge/Download-APK-brightgreen?style=for-the-badge&logo=android)](https://github.com/om-kumar-singh/Tic-Tac-Toe/releases/download/v0.4/app-debug.apk)

Get the latest APK directly: **v0.4 Release**

## 🔨 Building

### Build APK via Android Studio
- **Build** > **Build Bundle(s)/APK(s)** > **Build APK(s)**

### Build APK via Command Line
```bash
./gradlew assembleDebug
```

The generated APK will be available at `app/build/outputs/apk/debug/app-debug.apk`

## 📁 Project Structure
```
app/
├── src/main/
│   ├── java/com/example/tictactoev4/
│   │   ├── MainActivity.java          # Main menu activity
│   │   └── GameActivity.java          # Game logic activity
│   ├── res/
│   │   ├── layout/                    # XML layout files
│   │   ├── drawable/                  # Icons and images
│   │   ├── values/                    # Colors, themes, strings
│   │   └── mipmap/                    # App icons
│   └── AndroidManifest.xml            # App configuration
```

## 🎯 Game Modes

### Single Player
- Play against a basic AI opponent
- Perfect for quick practice sessions

### Two Players
- Local multiplayer on the same device
- Take turns with a friend

## 🤝 Contributing
Contributions are welcome! Feel free to fork the project and submit pull requests.

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
**Developed with ❤️ using Android Studio & Java**

⭐ Star this repo if you find it helpful!
