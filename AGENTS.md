# Noctua Hub (Compose 版)

## 概要
[Noctua Hub](https://github.com/Girky0815/wf-noctua-hub) (React)を改良し、Compose Platform を用いて Windows デスクトップ / Android で動くようにする。また、Noctua Hub で用いていた機能を継承するが、有志の Warframe Status API を用いず、Warframe 公式の API を用いるようにする。

## システム構成
- 言語: Kotlin
- UI: Compose Platform (v1.10.1)を使用して、Material 3 Expressive 準拠とする(Android 16の設定アプリのようなUI)

## 要件
- Windows: 11 24H2(10.0.26100) 以上
- Android: 12L(SDK 32) 以上、ターゲットバージョンは Android 16(SDK 36) 以上とする
- 言語: 現時点では日本語(ja-jp)のみ。
- Kotlin バージョン: 最新のものを検索して用いる
  - JDK 25 または Gladle で対応している最新バージョン。 JDK17など古いものは使用禁止。
- Material3 バージョン: 最新のものを検索して用いる


- UI は 次のアプリケーションの実装を参考にして、Material 3 Expressive 準拠とする(Android 16の設定アプリのようなUI)
    - UI デザイン(Material 3 Expressive、色設定): [nsh07/Tomato](https://github.com/nsh07/Tomato)
    - 画面構成: [Noctua Hub](https://github.com/Girky0815/wf-noctua-hub)
    - フォントはGoogle Sans Flex(OFL)とNoto Sans JP (OFL)を用いる。アイコンは Material Symbols Rounded (Apache License 2.0) を用いる。
- API は Warframe 公式のAPIから定期的(初期ではアプリ起動時と毎時00/10/20/30/40/50分とする。将来的にはユーザーが設定できるようにする)に取得し、アプリ内で使用できるよう、パースしながら利用する。
  - API: https://api.warframe.com/cdn/worldState.php にて、JSONフォーマットになっている
    - このままでは使用が困難であり、システムでパースして利用する(パース作業については、Public Export を参照)。 
  - これを用いることで、Warframeの現時点でのアラートや亀裂ミッションなどの情報が手に入る。
- **[Warframe Status API](https://github.com/WFCD/warframe-status)は使用しないこと。基本的にはこのAPIを使用するほうが楽ではあるものの、頻繁にダウンしたり(過去には60日以上りようできなくなったこともある)、更新が遅れたりする(数時間程度は更新されないこともざら)など、安定性が低いためである。**Noctua Hub React版では利用していたが、この理由によりCompose版では公式API/Public Export を使うこととする。これにより、より安定した挙動にすることが可能である。

## API の仕様
API: https://api.warframe.com/cdn/worldState.php
- 不定期ではあるものの、ゲーム内部で使用しているのもあり高頻度で更新されている。
- 日付時刻が Unix 時刻になっているため、アプリで使用する場合は UTC+9:00(JST) で日付・時刻に変換(`yyyy年m月d日 hh:mm:ss`形式に)する必要がある。

## Public Export
Public Export は DE が公開している公式のものであり、ゲーム内部や公式のアプリケーションなどでも使用されている。

API 上ではローカライズの関係上、情報を`DT_PROTOFRAME`(敵勢力)や`/Lotus/Levels/DevilTower/BossArenaUriel.level`(報酬やミッションの詳細など)などのような表記を用いている(APIデータの例は `example_json/wf-status_1771495618.json` を参照すること)。


Warframe がアップデートされた際には、以下の手順で Public Export を更新する必要がある。
Public Export は毎回ダウンロードすると時間やデータ通信量がかかるため、システム内でダウンロードを一時的に保存する。

パースのために Warframe 公式の Public Export を利用するための作業は次の通りである。なお、各JSONはUTF-8でエンコードされている。
1. オリジンサーバーにアクセスし、言語固有のインデックスファイルを取得する。**日本語版の情報を取得するため、以下のURLはそのまま使用する。**
  - https://origin.warframe.com/PublicExport/index_ja.txt.lzma
2. lzma 圧縮を解凍し、.txtから必要な情報を取得する。
  - lzma の例は次の通りである(Warframe v41.1.2)。これは例であり、システム内ではこの記述をそのまま使用しないこと。
    - **[重要] https://wiki.warframe.com/w/Public_Export に各JSONのスキーマの説明などが記載されている。必ず参照すること。**
    ```txt
ExportCustoms_ja.json!00_uVpp+tctFk5vATOJTj3LLw
ExportDrones_ja.json!00_TE8aELSmjnxa9kkx8nC+hA
ExportFlavour_ja.json!00_3B0Klo5u2jU1+8f9oeZrWA
ExportFusionBundles_ja.json!00_GtjUAEeETy9ruJB9nZXL1w
ExportGear_ja.json!00_T4aduZGA0l7zDhK0lxjF6A
ExportKeys_ja.json!00_MI85YwXHigddOsMQOzx5Eg
ExportRecipes_ja.json!00_6N5QmZE1NPUAt3xQQ8Js8g
ExportRegions_ja.json!00_MSPo3fDRHB7H+ylbS0Lh7w
ExportRelicArcane_ja.json!00_KIgQ6qv3tyNMCXYblh38qQ
ExportResources_ja.json!00_tsY7E3Y5LQKmtNbqQX06tg
ExportSentinels_ja.json!00_c8B4VjxWy1ngsjm+G2-nTQ
ExportSortieRewards_ja.json!00_SOYnb9kVBAW3l-FUS2ihOw
ExportUpgrades_ja.json!00_f-C7yiyJFAGI-tOfc1PQFQ
ExportWarframes_ja.json!00_w+3R-MwWjFKUTJGq5hIAUw
ExportWeapons_ja.json!00_vKWjUFXvsoLbfLMf8gHfCQ
ExportManifest.json!00_bcglTlTdPloy4wjxIQNCow
  ```
  - 各行はURLの一部であり、`http://content.warframe.com/PublicExport/Manifest/` のあとに続けてアクセスすることで、翻訳情報が書かれたJSONファイルを取得できる。
    - アップデートごとに`!00_`以降の文字列は変化する。また、**更新後は更新前のファイルは利用できなくなる**ため、利用できないか変更された場合は1から再度行う必要がある(1は固定で変化しない)。
      - アルゴリズムは任せるが、インデックスファイルの`ExportManifest.json`の`!00_`以降の文字列を比較することで、更新を検知できるはずである(違う場合は更新されたとみなす)。
3. 各JSONを1つの辞書ファイルにし、API のデータ表記と照らし合わせて変換する。
  - 概ね /Lotus/表記を 当てはまるキーのname値に変換すればよい(性能のデータ等は不要)。
  - 一部のデータで全角記号を使用しているものがあった(`％`、`：` など)。これらは半角記号に変換して用いる(`％`->`%`、`：`->`: `など)。
    - 句点(。)や読点(、)といったものは全角のままにする(半角になっていた場合は全角に直す必要はない)。
4. 変換後のAPIデータを用いてアプリ内の情報表示を行う。


### 各JSONの内容について
名前や説明文と表記したものはローカライズされたものが含まれている。一部(必要そうなもの)はサンプルを用意した(JSONのコメントアウトは本来エラーになるが、分かりやすいように説明を追加した)。
- ExportCustoms_ja.json: Warframe や武器のスキンなどの名前や説明文
- ExportDrones_ja.json: エクストラクター(放置により資源を採掘するアイテム)の情報。本システムでは用いる必要はない。
- ExportFlavour_ja.json: テンノ(プレイヤー)がカスタマイズできる項目(カラー設定、グリフ、装飾、フレームのアニメーション)の名前や説明文
- ExportFusionBundles_ja.json: MOD 変換に用いるMODの名前と説明文。不要
- ExportGear_ja.json: ギアアイテムの名前や説明文
- ExportKeys_ja.json: クエストの名前や説明文、クリア後のクエスト画面に表示されるテキストに加え、クランキーやMutalist Alad V 暗殺キーの名前と説明文を含む。おそらく不要。
- ExportRecipes_ja.json: 設計図の必要アイテムや制作時間。ここだけでは利用不可(ここも`/Lotus/`で表記されており、必要に応じて変換が必要)。おそらく不要。
- ExportRegions_ja.json: 星系マップの各ノード(ステージ)の名前や所属惑星、敵勢力(数字で表記されている)、敵レベルの最小値・最大値。
- ExportRelicArcane_ja.json: Void レリックの名称と報酬リスト、アルケインの名前と説明文(ランクごとに分かれている)。
  - レリックは精錬段階ごとにユニークIDが分かれている(無傷: Bronze、特別: Silver、完璧: Gold、光輝: Platinum)が、内容は全く同じ。
- ExportResources_ja.json: 資源等アイテムの名前と説明文。最重要と思われる。
- ExportSentinels_ja.json: ペット(センチネル、クブロウ、キャバット他)の名前と性能、説明文
- ExportSortieRewards_ja.json: ソーティの報酬、NightWave のチャレンジや報酬、レールジャックのセンティエント・アノマリー発生ノード、レールジャックやデュヴィリ漂流者の性能値、ブースターアイテムの名前や説明など。
- ExportUpgrades_ja.json: MOD の情報。名前だけ使えばよい。
- ExportWarframes_ja.json: Warframe/アークウィング の名前と性能、アビリティ、説明文。
- ExportWeapons_ja.json: 武器の名前と性能、説明文。
- ExportManifest.json: アイテムの画像データのURL。現時点では使用しない。
  - なお、textureLocation 値を`http://content.warframe.com/PublicExport/`のあとに続けてアクセスすることで、画像データを取得できる。

各 Export には一部正しくないデータがあり、必要に応じて修正するための関数が必要だと思われる(example_json参照)。

## GitHub Actions
GitHub Actions を用いて、アプリをビルドし GitHub のリリースにて配布できるようにする。

キーリングは未設定のため、ある程度動く状態になったら作成する。

Actionsでビルドに使用するシステムはなるべく最新かつ安定バージョンのもの(たとえば、Windows Latest、Java 25 など)を検索して用いる。

Actions 作動条件: バージョンタグ(v*)設定時
1. リリースノートの下書きを作成する。
2. Android 用、Windows 用のビルドを行う(vX.X.X はタグ名から取得したバージョン)。
- Android: noctuahub-vX.X.X-android-arm64-v8a.apk
- Windows: noctuahub-vX.X.X-windows-x64.exe
3. リリースにapkとexeを添付する。
4. 下書き状態でリリースに入れる。

## コーディング規約
- スペース数 2
- コメントは各関数に日本語で付与し、必要に応じて参照し変更できるようにすること。

## LLM の挙動
- **出力は必ず日本語で行うこと**。
  - メッセージの他に、Implementation Plan(実装計画)やToDo、Walkthrough(確認手順) なども日本語にして出力すること。
  - 英語で出力しかけた場合は日本語に必ず翻訳すること。
- Git コミット等は勝手に行う必要はない。例外として、ユーザーに指示された場合は、コミットメッセージは必ず日本語で設定しコミットすること。
  - 必要に応じて、3行目以降に詳しい説明を入れてもよい。 
  - `feat:` や `fix:`、`bump`等プレフィックスは英語で用いてもよい。 
    - 例: `feat: OOの機能を追加`(3行目以降に詳しい説明を入れてもよい)

## はじめに行うタスク
注意: Android Studio で Jetpack Compose を用いたアプリ開発と若干異なる可能性があるため、必要に応じてKotlinの公式情報などを検索すること。
0. 依存関係の設定(最重要)
  - **自身の学習結果をそのまま使用することを禁ずる。必ず最新のバージョンか、存在するパッケージかを検索して用いること。**
  - 安定するからといって古いバージョンの使用は認めない。
  - Android Studio 用ではなく、Compose Multiplatform 用の依存関係を検索して、ビルドエラーが発生しないように注意して設定すること。
1. UIの共通部分設定
  - デザインは [nsh07/Tomato](https://github.com/nsh07/Tomato) のリポジトリを参照し、それを準拠したものにする。
    - フォントの設定(指定やタイポグラフィ)や色使い(カラースキームの設定)、形状やアニメーション等をこのリポジトリに近づける。
      - ただし、リスト項目タップ時に角丸を強くする設定は使用しない。
    - Android Studio の Jetpack Compose による実装だが、Compose Multiplatform により同等の実装を行う。
  - **Material 3 の古い色設定は使用禁止!**
    - 背景色を Surface にするなどは禁止
  - まずは調査を行い、必要な作業を特定。
  - 調査結果を元に、必要な作業をToDoに追加し、ユーザーに確認を求める。
2. Warframe API の実装
  - まずは [Warframe Wiki 英語版](https://wiki.warframe.com/w/Public_Export) の情報を確認し、Public Export のスキーマ等仕様を理解する。
  - APIの/Lotus/データ表記を、Public Export のデータを参照して、日本語の名称に変換する処理を実装する。
3. Noctua Hub の移植
  - React 版の [Noctua Hub](https://github.com/Girky0815/wf-noctua-hub) から機能をなるべくそのまま移植する。
  - まずは Noctua Hub の画面構成や機能を確認し、必要な作業をToDoに追加し、ユーザーに確認を求める。
    - 配置等はなるべく同じようになるように移植すること。
      - ただし、UIのデザインはTomatoを優先する
    - 設定コンテンツ等もなるべく同じように移植すること。
    - ただし、Warframe Status API を用いている部分は、公式API+Public Export を用いるように実装を変更すること。
    - 深淵/次元アルキメデアのモディファイアについて、説明を充実させているため、なるべくこれを使えるようにすること(この部分だけ公式に従う必要はない)。