# Suri Esports — complete starter package

## Included
- Android Jetpack Compose app
- Node/Express API
- SQLite database
- Admin login and admin dashboard
- Tournament creation/status/room management API
- Player registration with UTR/reference and PENDING/VERIFIED/REJECTED states
- UPI flow structure

## Run backend
```bash
cd backend
npm install
ADMIN_SECRET="replace-with-your-secret" npm start
```
Open `admin/index.html` through a static server and point `/api` to your backend (or serve admin from your preferred host).

## Android
Open `android/` in Android Studio and run. The emulator uses `10.0.2.2:8080` for the local backend. For a real phone, change `API` in `MainActivity.kt` to your HTTPS backend URL.

## Real payment
The app currently uses UPI/UTR + server-side verification status. To automatically verify payments, connect a legitimate merchant payment provider on the backend and implement its webhook/signature verification. Never put merchant secret keys inside the Android app.
