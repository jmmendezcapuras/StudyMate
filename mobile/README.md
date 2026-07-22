# StudyMate — Android (Kotlin)

Native Android client for StudyMate, built with Jetpack Compose. It talks to
the Spring Boot backend in `../backend` over REST (Retrofit), using the same
JWT-based auth contract as the web app in `../web`.

## Package
`edu.cit.capuras.studymate.mobile` (applicationId `edu.cit.capuras.studymate`)

## Structure (Vertical Slice Architecture, mirrors the web client's `features/`)
```
core/network/       ApiClient (Retrofit + auth interceptor), SessionManager
feature/auth/       Login, Register, AuthViewModel, AuthApiService, DTOs
feature/subject/    Dashboard (subjects panel), SubjectViewModel, SubjectApiService, DTOs
feature/session/    Study sessions panel, SessionViewModel, SessionApiService, DTOs
feature/admin/      Admin user-management screen, AdminViewModel, AdminApiService, DTOs
ui/theme/           Color/Theme/Type — mirrors web/src/index.css's palette exactly
```

## Feature parity with the web app
- **Auth**: register (username/email/password), login, JWT stored locally and
  attached to every request via an OkHttp interceptor, logout (revokes the
  JWT server-side via `POST /api/auth/logout`, same as web).
- **Subjects**: list, add, delete — `GET/POST/DELETE /api/subjects`.
- **Study sessions**: list, log, delete — `GET/POST/DELETE /api/sessions`.
- **Admin** (visible only to accounts with role `ADMIN`, same as the web
  app's `/admin` route): view all users, create additional admin accounts,
  delete non-admin accounts — `GET/POST /api/admin/users`,
  `DELETE /api/admin/users/{id}`.
- **Role-based navigation**: on login/register and on app restart, admins are
  routed to the admin screen and students to the dashboard — same behavior
  as the web client's `ProtectedRoute`.

`ui/theme/Color.kt` mirrors the CSS custom properties in `web/src/index.css`
(`--accent: #10665A`, `--bg: #F4F6F5`, etc.) so subjects and UI chrome use the
same colors on both platforms.

## Running it in Android Studio
1. Open the `mobile/` folder in Android Studio (Koala or newer) as a project and let Gradle sync.
2. Start the backend first: `cd ../backend && ./mvnw spring-boot:run` (listens on `localhost:8080`).
3. Run the app on an **emulator** — it's pre-configured to reach the backend at
   `http://10.0.2.2:8080/api/`, which is the emulator's alias for your host machine.
   - Running on a **physical device** instead? Change `BASE_URL` in
     `core/network/ApiClient.kt` to your computer's LAN IP, e.g.
     `http://192.168.1.5:8080/api/`, and make sure the phone is on the same
     Wi-Fi network as the backend. Add that IP to
     `res/xml/network_security_config.xml` too (cleartext HTTP is only
     allowlisted for `10.0.2.2`/`localhost` by default).
   - Deployed the backend somewhere with HTTPS? Point `BASE_URL` there instead
     and you can drop the network security config/`usesCleartextTraffic` entirely.
4. Register a new account, or log in with one you already created via the web app —
   both clients hit the same backend and database.

## Generating a signed release APK
The release build type only attaches a signing config if `mobile/keystore.properties`
exists (it's gitignored — every machine needs its own keystore, never commit one).

1. In Android Studio: **Build → Generate Signed Bundle / APK… → APK**, then
   either point it at an existing `.jks`/`.keystore` file or create a new one
   through the wizard. Finishing the wizard once will also write
   `mobile/keystore.properties` for you automatically on subsequent
   command-line builds.
2. Or set it up manually for CLI builds — create `mobile/keystore.properties`:
   ```properties
   storeFile=../my-release-key.jks
   storePassword=...
   keyAlias=...
   keyPassword=...
   ```
   then run `./gradlew assembleRelease` — the output APK will already be signed.
3. Without `keystore.properties`, `assembleRelease` still succeeds but produces
   an **unsigned** APK (fine for `assembleDebug`/day-to-day development, not
   for distribution).

## Notes
- `SessionManager` persists `id`/`username`/`role`/JWT in SharedPreferences so
  the app remembers who's logged in (and whether they're an admin) across
  restarts, and restores the JWT into memory on process start so the auth
  interceptor keeps working after the app is killed and reopened.
- `network_security_config.xml` allows cleartext HTTP only to `10.0.2.2` and
  `localhost` for local development; remove it (and `usesCleartextTraffic`
  in the manifest) once the backend is deployed over HTTPS.
