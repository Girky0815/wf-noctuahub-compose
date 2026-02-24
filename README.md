# Noctua Hub(Compose 版)
Material 3 Expressive を使用した Warframe ワールドステータス確認アプリ(Compose 版)

---
このリポジトリは、Android およびデスクトップ (JVM) をターゲットとした Kotlin Multiplatform プロジェクトです。

* [/composeApp](./composeApp/src) は、Compose Multiplatform アプリケーション間で共有されるコードのためのディレクトリです。
  ここにはいくつかのサブフォルダーが含まれています：
  - [commonMain](./composeApp/src/commonMain/kotlin) は、すべてのターゲットに共通するコード用です。
  - その他のフォルダーは、フォルダー名に示されたプラットフォーム専用にコンパイルされる Kotlin コード用です。
    例えば、Kotlin アプリの iOS 部分で Apple の CoreCrypto を使用したい場合、[iosMain](./composeApp/src/iosMain/kotlin) フォルダーがそのような呼び出しを記述する適切な場所になります。
    同様に、デスクトップ (JVM) 固有の部分を編集したい場合は、[jvmMain](./composeApp/src/jvmMain/kotlin) フォルダーが適切な場所となります。

### Android アプリケーションのビルドと実行

開発バージョンの Android アプリをビルドして実行するには、IDE のツールバーにある実行ウィジェットから実行構成（Run Configuration）を使用するか、ターミナルから直接ビルドしてください。

- macOS/Linux の場合
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- Windows の場合
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### デスクトップ (JVM) アプリケーションのビルドと実行

開発バージョンのデスクトップアプリをビルドして実行するには、IDE のツールバーにある実行ウィジェットから実行構成を使用するか、ターミナルから直接実行してください。

- macOS/Linux の場合
  ```shell
  ./gradlew :composeApp:run
  ```
- Windows の場合
  ```shell
  .\gradlew.bat :composeApp:run
  ```

---

[Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) についての詳細はこちら…

<!--
This is a Kotlin Multiplatform project targeting Android, Desktop (JVM).

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
-->
