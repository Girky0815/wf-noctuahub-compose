#!/bin/bash

# 文字コードをUTF-8に設定
export LANG=ja_JP.UTF-8

# Java 24 のパスを設定 (Windows のパススタイルから Git Bash 用のパススタイル)
export JAVA_HOME="/c/Program Files/Java/jdk-24"

echo "==================================================="
echo "Noctua Hub (Compose Desktop) 起動スクリプト (Git Bash 用)"
echo "==================================================="
echo ""
echo "[ホットリロード (HMR) についての注意点]"
echo "Compose Desktop は、Web版 (Vite等) と異なり、コマンドラインからの"
echo "起動では完全自動のホットリロードは標準サポートされていません。"
echo ""
echo "UIの変更を即座に見ながら開発したい場合は、スクリプトからの起動ではなく"
echo "IntelliJ IDEA や Android Studio などの IDE で【デバッグモード(Debug)】"
echo "で実行し、「Reload Changed Classes (クラスとリソースの更新)」機能を"
echo "利用することをおすすめします（HotSwap が適用されます）。"
echo "==================================================="
echo ""

echo "ビルドおよびアプリを起動しています..."
./gradlew :composeApp:run
