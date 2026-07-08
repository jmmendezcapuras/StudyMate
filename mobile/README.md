# StudyMate — Android (Kotlin)

Native Android client for StudyMate, built with Jetpack Compose. It talks to
the Spring Boot backend in `../backend` over REST (Retrofit) using the same
`username` / `password` auth contract as the web app.

## Package
`edu.cit.capuras.studymate.mobile` (applicationId `edu.cit.capuras.studymate`)

## Screens
- **LoginScreen** — username/password, calls `POST /api/auth/login`
- **RegisterScreen** — username/password/confirm, calls `POST /api/auth/register`
- **DashboardScreen** — simplified mobile port of `web/src/pages/Dashboard.jsx`:
  add/view subjects — same teal/paper color palette and card-based styling
  as the web app.

## Design parity with the web app
`ui/theme/Color.kt` mirrors the CSS custom properties in `web/src/index.css`
exactly (`--accent: #10665A`, `--bg: #F4F6F5`, etc.), and `colorForSubject()`
uses the same palette/ordering as the web client's `colorForSubject()` in
`Dashboard.jsx`, so a given subject shows the same accent color on both
platforms. Typography follows the same display/body/mono hierarchy as the
web app (Fraunces-style serif headings, sans body text, monospace durations),
using system font families to keep the module dependency-free — swap in the
actual Fraunces/Inter font files later for pixel-perfect parity if needed.

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
