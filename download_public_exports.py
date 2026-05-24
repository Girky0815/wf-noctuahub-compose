import urllib.request
import lzma
import os

def download_all_public_exports():
    temp_dir = "./temp"
    if not os.path.exists(temp_dir):
        os.makedirs(temp_dir)
        print(f"Created directory {temp_dir}")

    # 1. 日本語マニフェストのダウンロードと解凍
    manifest_url = "https://origin.warframe.com/PublicExport/index_ja.txt.lzma"
    print("Downloading index_ja.txt.lzma...")
    try:
        req = urllib.request.Request(
            manifest_url, 
            headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36'}
        )
        manifest_data = urllib.request.urlopen(req).read()
    except Exception as e:
        print(f"Failed to download manifest: {e}")
        return

    print("Decompressing index_ja.txt.lzma using LZMA...")
    try:
        decompressed_manifest = lzma.decompress(
            manifest_data[13:], 
            format=lzma.FORMAT_RAW, 
            filters=[{'id': lzma.FILTER_LZMA1}]
        ).decode("utf-8")
    except Exception as e:
        print(f"Failed to decompress manifest: {e}")
        return

    # 2. 各ライン（各JSONファイル情報）を走査してダウンロード
    base_manifest_url = "http://content.warframe.com/PublicExport/Manifest/"
    lines = [line.strip() for line in decompressed_manifest.split("\n") if line.strip()]

    print(f"Found {len(lines)} files to download.")

    for line in lines:
        if "!" in line:
            file_name = line.split("!")[0]
            download_url = base_manifest_url + line
            dest_path = os.path.join(temp_dir, file_name)
            
            print(f"Downloading {file_name} ...")
            try:
                req = urllib.request.Request(
                    download_url, 
                    headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36'}
                )
                json_data = urllib.request.urlopen(req).read()
                with open(dest_path, "wb") as f:
                    f.write(json_data)
                print(f"Successfully saved {file_name} to {dest_path}")
            except Exception as e:
                print(f"Failed to download {file_name}: {e}")

    print("\nAll Public Export JSON downloads completed successfully!")

if __name__ == "__main__":
    download_all_public_exports()
