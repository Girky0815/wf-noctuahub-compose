# Noctua Hub (Compose 版)

Material 3 Expressive (M3E) を採用した、AndroidおよびWindows向けのオープンソースなWarframeワールドステータス確認アプリです。

## 概要
Noctua Hub (Compose 版) は、ゲーム「Warframe」の現在のゲーム内状況（アラート、Void亀裂、ソーティ、各種ステータスなど）をリアルタイムに確認できるユーティリティアプリです。

本アプリは**完全日本語対応**（現時点では日本語のみサポート）しています。Warframe公式の翻訳データ（Public Export）を取得してアプリ内で変換・パースしているため、通常はゲームのシステム内部ID（`/Lotus/...` 等）で取得されてしまうミッションや報酬、各種アイテムの名前・説明文も、自然な日本語で表示されます。

UIデザインには最新の **Material 3 Expressive (M3E)** を採用しており、Android 16の設定アプリのような先進的かつ直感的なデザインに近づけています。これにより、Androidデバイス全体のシステムUIと非常によく馴染み、シームレスで美しいユーザー体験を提供します。

また、作者自身がアプリ内課金や広告を非常に嫌っているため、本アプリには**広告やアプリ内購入などの要素は一切存在しません**。完全無料で、純粋に情報収集のためだけに使用できます。

<!-- ## アプリ制作の経緯
今までReact版にて制作していましたが、Warframe Status APIの取得不安定さなどから、この度Warframe公式公開データを用いて情報を表示するようにしてみました。 -->

## 要件
- **Android**: Android 14 以上（ターゲット SDK: Android 17）
- **Windows**: Windows 11 24H2 以降（※現在、動作検証は十分に行われていません）

## 免責事項
- 本アプリおよび作者は、Warframeの運営元である Digital Extremes (DE) 社とは一切関係ありません。
- Noctua は Dante のアビリティから名付けられており、PCパーツ販売のNoctua社とも関係ありません。
- 本アプリは完全に非公式であり、ファンによるファンのためのツールとして提供されています。
- 本アプリは完全に無料で使用できます。
- 本アプリを使用したことによって生じたいかなる損害（端末の不具合など）についても、作者は一切の責任を負いません。自己責任でのご利用をお願いいたします。（なお、本アプリはゲームアカウントやゲームデータには一切アクセスしないため、ゲーム内のデータ等に影響を与えることはありません。）
- 動作検証は **Pixel 8** および **Pixel 10** でのみ行っており、他の一部のAndroidデバイスでは正常に動作しない可能性があります。
- Windows向けのインストール用インストーラー（exe版）は現在ベータ版であり、正常に動作しない可能性が非常に高いです。もしWindowsで動作させたい場合は、本リポジトリをクローンし、以下のコマンドを使用して開発環境から実行することを推奨します。

### Windows での実行手順（コマンド）
Java（JDK 24推奨）がインストールされた環境で、リポジトリのルートディレクトリにて以下のコマンドを実行してください。

- **Windows PowerShell / コマンドプロンプト**:
  ```shell
  .\gradlew.bat :composeApp:run
  ```

---

## 機能
- **日本語対応**: Warframe公式の翻訳データ（Public Export）を取得してアプリ内で変換・パースしているため、通常はゲームのシステム内部ID（`/Lotus/...` 等）で取得されてしまうミッションや報酬、各種アイテムの名前・説明文も、自然な日本語で表示されます。
- **ワールドステータスの確認**: 各オープンワールド（地球、シータス、フォーチュナ、カンビオン隔離庫など）の昼夜・気温・サイクル情報をリアルタイム表示。
- **亀裂・ミッションの追跡**: 現在発生しているVoid亀裂やミッションの種類、報酬の確認。
- **アラート・イベント情報**: 開催中のイベントや、公式のアラート情報を取得。
- **ニュースの優先言語表示**: Warframeの最新ニュースを日本語タイトル優先で表示（日本語がない場合は英語タイトルで表示）。
- **Nightwave**: 現在のウィークリー/デイリーアクトの進行確認。
- **商人の追跡**: Baro Ki'Teer (バロ吉 / Voidの商人) の到着までの時間や、販売アイテムリストの確認。
- **深淵 / 次元アルキメデア対応**: アルキメデアの各種モディファイア情報の詳細表示。
- **ディセンディアにも対応**: 最新コンテンツ(Update 41)のディセンディアにも対応済み。

## どんなことができるか
Warframeのゲーム内情報を手元で手軽にチェックできます。
- 今どの亀裂ミッション（レリック）がアクティブか？
- フォーチュナは今「温暖」か「極寒」か？
- 今週のソーティやアルコン討伐戦のミッションとターゲットは何か？
- Baro Ki'Teer (バロ吉) は何を持ってきているか？
- Nightwaveのお題は何か？
などを、ゲームを起動しなくても即座に確認できます。
※「バロ吉」は公式のゲーム内翻訳ではありませんが、一部コミュニティやプレイヤー間で親しまれている愛称です。

## スクリーンショット
*(後日追加予定)*

---

## データセーフティ情報
- **データの取得・送信**: 本アプリがユーザーの個人情報を収集したり、外部のサーバーに送信したりすることは一切ありません。
- **第三者提供**: 収集を行っていないため、第三者へデータを提供することは一切ありません。
- ※本アプリはWarframe公式API（`api.warframe.com`）およびGitHub（アップデートチェック用）と通信を行いますが、これは純粋に公開情報の取得のみを目的としており、送信されるのは標準的なネットワーク接続情報のみです。

---

<!--
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

(旧英語ドキュメント部分)
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
