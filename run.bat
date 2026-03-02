@echo off
chcp 65001 > NUL
REM Java 24 のパスを設定 (現在の環境に合わせる)
set JAVA_HOME=C:\Program Files\Java\jdk-24

echo ===================================================
echo Noctua Hub (Compose Desktop) 起動スクリプト
echo ===================================================
echo.
echo [ホットリロード (HMR) についての注意点]
echo Compose Desktop は、Web版 (Vite等) と異なり、コマンドラインからの
echo 起動では完全自動のホットリロードは標準サポートされていません。
echo.
echo UIの変更を即座に見ながら開発したい場合は、スクリプトからの起動ではなく
echo IntelliJ IDEA や Android Studio などの IDE で【デバッグモード(Debug)】
echo で実行し、「Reload Changed Classes (クラスとリソースの更新)」機能を
echo 利用することをおすすめします（HotSwap が適用されます）。
echo ===================================================
echo.

echo ビルドおよびアプリを起動しています...
call gradlew.bat :composeApp:run

pause
