# StudyMate — Android (Kotlin)

Native Android client for StudyMate, built with Jetpack Compose. It talks to
the Spring Boot backend in `../backend` over REST (Retrofit) using the same
`username` / `password` auth contract as the web app.

## Package
`edu.cit.capuras.studymate.mobile` (applicationId `edu.cit.capuras.studymate`)

## Screens
- **LoginScreen** — username/password, calls `POST /api/auth/login`
- **RegisterScreen** — username/password/confirm, calls `POST /api/auth/register`
- **DashboardScreen** — placeholder post-login screen (subjects/sessions come in a later assignment)

## Running it

1. Open the `mobile/` folder in Android Studio (Koala or newer) as a project and let Gradle sync.
2. Start the backend first: `cd ../backend && ./mvnw spring-boot:run` (listens on `localhost:8080`).
3. Run the app on an **emulator** — it's pre-configured to reach the backend at
   `http://10.0.2.2:8080/api/`, which is the emulator's alias for your host machine.
   - Running on a **physical device** instead? Change `BASE_URL` in
     `network/ApiClient.kt` to your computer's LAN IP, e.g. `http://192.168.1.5:8080/api/`,
     and make sure the phone is on the same network.
4. Register a new account, or log in with one you already created via the web app —
   both clients hit the same backend and Supabase database.

## Notes
- There's no JWT/session token yet on the backend, so the app just stores the
  returned `id`/`username` locally (`SessionManager`, SharedPreferences) to know
  who's logged in across app restarts.
- `network_security_config.xml` allows cleartext HTTP only to `10.0.2.2` and
  `localhost` for local development; remove it once the backend is deployed over HTTPS.
