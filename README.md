# COMP3074 — Assignment 1

Payroll Calculator by Phillip Onofua (101489064).

The main screen accepts hours worked, hourly rate and a decimal tax rate (`0.15` means 15%). It displays regular pay, overtime pay, total pay and tax in Canadian dollars. Regular pay covers at most 40 hours; overtime pays 1.5 times the hourly rate. Following the assignment formula, tax applies to regular pay only. Invalid or missing inputs display field errors.

The About button opens a separate `AboutActivity` containing the student name and ID. ActionBar Up and system Back finish About and return to the existing payroll screen with its values intact.

## Build and check

Open in Android Studio with Android SDK 37 and JDK 17 or later, or run:

```sh
./gradlew assembleDebug testDebugUnitTest lintDebug
./gradlew connectedDebugAndroidTest
```

The second command requires a connected Android device/emulator (API 24 or later). Unit tests cover the 40-hour boundary, overtime, decimal precision, zero values and invalid inputs. Device tests cover calculation, validation, state preservation and About navigation.

Package: `ca.gbc.comp3074.onofua_phillip.assignment1`. This repository contains application source, resources, build files, tests and this README.
