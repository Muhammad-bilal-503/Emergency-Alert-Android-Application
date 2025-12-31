# Emergency Alert Android Application

A fully functional Emergency Alert Android Application built with Kotlin and Jetpack Compose, following modern Android development best practices.

## 🚨 Features

### 1. Authentication Module (Firebase Auth)
- User Registration & Login using Email and Password
- Secure user access
- Each user has their own emergency data

### 2. Home Screen (Emergency Dashboard)
- Large Emergency SOS Button
- Emergency Type selection (Medical, Safety, Accident, Fire)
- Confirmation dialog before sending alert
- Countdown timer (to prevent accidental presses)
- Real-time weather display

### 3. Emergency Contacts Management
- Add, edit, delete trusted contacts
- Maximum 5 emergency contacts
- Stored locally using Room Database
- Synced to Firebase for backup

### 4. Emergency Alert System (Core Feature)
When user presses SOS:
- Fetches real-time location
- Generates emergency message
- Sends alert to pre-saved contacts
- Alert includes: User name, Emergency type, Live Google Maps location link, Timestamp
- Delivery via Firebase Push Notifications

### 5. Location Tracking
- Uses device GPS
- Requests runtime permissions
- High accuracy mode
- Location updated at alert time only (privacy-friendly)

### 6. Alert History Screen
- List of previously sent alerts
- Displays: Date & time, Emergency type, Location snapshot
- Implemented using LazyColumn

### 7. REST API Integration
- Weather API integration
- Shows current weather on Home Screen
- Helpful during outdoor emergencies

### 8. Notifications System
- Firebase Cloud Messaging
- Contacts receive instant alert notification
- User receives confirmation notification

### 9. UI / UX Design
- Material You (Dynamic Colors)
- Clean, minimal, emergency-focused UI
- Large buttons for quick access
- Accessibility friendly design

### 10. Dark Mode
- Supports system dark mode
- Improves night-time usability

## 🛠 Technology Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM
- **Database**: Room (local) + Firebase Firestore (cloud)
- **Authentication**: Firebase Authentication
- **Notifications**: Firebase Cloud Messaging
- **APIs**: Google Maps / Location API, OpenWeatherMap API
- **Minimum Android Version**: Android 8+ (API 26)

## 📋 Prerequisites

1. Android Studio Hedgehog or later
2. JDK 11 or higher
3. Firebase project setup
4. OpenWeatherMap API key (optional, for weather feature)

## 🔧 Setup Instructions

### 1. Firebase Setup

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app to your Firebase project
3. Download `google-services.json` file
4. Place it in `app/` directory
5. Enable the following Firebase services:
   - Authentication (Email/Password)
   - Cloud Firestore
   - Cloud Messaging

### 2. Weather API Setup (Optional)

1. Get a free API key from [OpenWeatherMap](https://openweathermap.org/api)
2. Open `app/build.gradle.kts`
3. Replace `YOUR_WEATHER_API_KEY` with your actual API key:
   ```kotlin
   buildConfigField("String", "WEATHER_API_KEY", "\"your_actual_api_key\"")
   ```

### 3. Build and Run

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project
4. Run on an emulator or physical device

## 📱 App Structure

```
app/
├── data/
│   ├── database/          # Room database setup
│   ├── model/             # Data models
│   ├── remote/            # API services
│   └── repository/        # Repository layer
├── navigation/            # Navigation setup
├── service/              # Background services (FCM)
├── ui/
│   ├── screen/           # UI screens
│   ├── theme/            # Material You theme
│   └── viewmodel/        # ViewModels
└── util/                 # Utility classes
```

## 🏗 Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture:

- **Model**: Data models and Room entities
- **View**: Jetpack Compose UI screens
- **ViewModel**: Business logic and state management
- **Repository**: Data source abstraction (Room + Firebase)

## 🔐 Permissions

The app requires the following permissions:
- `INTERNET` - For API calls and Firebase
- `ACCESS_FINE_LOCATION` - For GPS location
- `ACCESS_COARSE_LOCATION` - For approximate location
- `POST_NOTIFICATIONS` - For push notifications (Android 13+)
- `SEND_SMS` - For SMS fallback (conceptual)

## 🧪 Testing

The app includes:
- Unit tests for ViewModels
- Instrumented tests for UI components
- Edge case handling (no internet, no location, etc.)

## 📝 Notes

- The app uses Room for local storage and Firebase for cloud sync
- Maximum 5 emergency contacts per user
- Location is only fetched when sending an alert (privacy-friendly)
- Weather API requires internet connection
- Firebase setup is required for authentication and notifications

## 🎯 Future Enhancements

- SMS fallback for alerts
- Voice recording during emergency
- Panic button widget
- Multiple language support
- Emergency services integration

## 📄 License

This project is created for educational purposes.

## 👨‍💻 Development

Built with ❤️ using Kotlin and Jetpack Compose

---

**Important**: Make sure to add your `google-services.json` file and configure Firebase before running the app!
