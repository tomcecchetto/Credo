# Releasing Credo

How to build a new version and get it onto Larissa's phone.
Releases are built on the **Windows PC** (that's where the keystore lives).

## Version numbers

- `versionName` is what people see: `MAJOR.MINOR.PATCH`
    - new features → `1.3` → `1.4`
    - bug fix only → `1.3` → `1.3.1`
- `versionCode` goes up by **1** on every release, no exceptions.
  Android refuses to install an update with the same or a lower code.

## Before you start

- All feature work is committed.
- **Database changed?** (new/changed entity or column)
    - `version` in `CredoDatabase` is +1
    - `AutoMigration(from = X, to = X + 1)` added (or a manual `Migration`)
    - new columns on existing tables have `@ColumnInfo(defaultValue = ...)`
    - the new schema JSON in `app/schemas/` is committed
    - the migration test in step 3 is **mandatory**

## 1. Version

In `app/build.gradle.kts`:

```kotlin
versionCode = 4                 // previous + 1
versionName = "1.3.2"           // new version
```

Sync Gradle.

## 2. Build

1. Build → Generate Signed App Bundle or APK → **APK**
2. **Choose existing** → `D:\Dev\Keys\credo-release.jks`
3. Passwords and alias from the password manager
4. Build variant: **release** → Create

Output: `app\release\app-release.apk`

> Never "Create new" keystore. A new key means the app can't be updated anymore.

## 3. Test on my phone

USB debugging on, phone connected:

```
adb devices
adb install -r app\release\app-release.apk
```

- Install **over** the previous release, never uninstall first
- Settings → Apps → Credo shows the new version
- Old data is still there (courses, sessions, exams)
- Check everything that changed in this release

If the app crashes:

```
adb logcat -d -b crash
```

## 4. Git

```
git commit -m "Release <version>"
git push
git tag v<version>
git push --tags
```

## 5. Archive and send

```
Copy-Item app\release\app-release.apk D:\Dev\Keys\credo-v<version>.apk
```

Send `credo-v<version>.apk` to Larissa.
Remind her: **update, don't uninstall** (Android should say "Update").

## Known notes

- R8 optimization is **off** for release builds: it crashed the app on startup (v1.0).
- `colorpicker-compose` stays on **1.1.4** until Kotlin is updated to 2.4
  (1.3.0 needs a newer Kotlin than the project uses).
- Debug builds use the `.debug` app id, so debug and release can be
  installed side by side.
