# Setup Guide for Emergency Alert Android Application

## 🔥 Firebase Setup (Required)

### Step 1: Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or select an existing project
3. Follow the setup wizard

### Step 2: Add Android App
1. In Firebase Console, click "Add app" and select Android
2. Register your app:
   - **Package name**: `com.example.emergencyalertandroidapplication`
   - **App nickname**: Emergency Alert (optional)
   - **Debug signing certificate**: Optional for now
3. Download `google-services.json`
4. Place the file in: `app/google-services.json`

### Step 3: Enable Firebase Services

#### Authentication
1. Go to Authentication → Sign-in method
2. Enable "Email/Password"
3. Save

#### Cloud Firestore
1. Go to Firestore Database
2. Click "Create database"
3. Start in **test mode** (for development)
4. Choose a location
5. Enable

#### Cloud Messaging
1. Go to Cloud Messaging
2. The service is automatically enabled
3. Note: You'll need to configure server-side code to send notifications to specific devices

## 🌤️ Weather API Setup (Optional)

### Step 1: Get API Key
1. Sign up at [OpenWeatherMap](https://openweathermap.org/api)
2. Go to API keys section
3. Generate a new API key (free tier available)

### Step 2: Configure in Project
1. Open `app/build.gradle.kts`
2. Find the line:
   ```kotlin
   buildConfigField("String", "WEATHER_API_KEY", "\"YOUR_WEATHER_API_KEY\"")
   ```
3. Replace `YOUR_WEATHER_API_KEY` with your actual key:
   ```kotlin
   buildConfigField("String", "WEATHER_API_KEY", "\"your_actual_api_key_here\"")
   ```
4. Sync Gradle

## 📱 Building the App

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK (API 26+)

### Steps
1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Ensure `google-services.json` is in `app/` directory
4. Build the project (Build → Make Project)
5. Run on emulator or device

## ⚠️ Important Notes

### Firebase Cloud Messaging (FCM)
The app includes FCM service setup, but you'll need to:
1. Implement server-side code to send notifications to specific device tokens
2. Store device FCM tokens when users register
3. Send notifications when emergency alerts are triggered

### Location Permissions
- The app will request location permissions on first launch
- Grant permissions for the app to function properly
- Location is only accessed when sending an alert (privacy-friendly)

### Testing
- Use a physical device or emulator with Google Play Services
- Test with a real Firebase project (not just the placeholder)
- Ensure internet connection for Firebase and Weather API

## 🐛 Troubleshooting

### Build Errors
- **Missing google-services.json**: Ensure the file is in `app/` directory
- **Gradle sync fails**: Check internet connection and Firebase setup
- **Permission errors**: Ensure all required permissions are in AndroidManifest.xml

### Runtime Errors
- **Firebase not initialized**: Check google-services.json is correct
- **Location not working**: Grant location permissions in device settings
- **Weather not loading**: Check API key and internet connection

## 📚 Additional Resources

- [Firebase Documentation](https://firebase.google.com/docs)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [OpenWeatherMap API](https://openweathermap.org/api)
