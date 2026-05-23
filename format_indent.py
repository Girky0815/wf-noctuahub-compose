import os

def convert_file(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        new_lines = []
        for line in lines:
            stripped = line.lstrip(' ')
            space_count = len(line) - len(stripped)
            
            if space_count > 0:
                # 4スペースインデントを2スペースインデントに半減させる
                # 4の倍数で端数がある場合も正しく処理します
                new_space_count = (space_count // 4) * 2 + (space_count % 4)
                new_line = ' ' * new_space_count + stripped
                new_lines.append(new_line)
            else:
                new_lines.append(line)
                
        with open(file_path, 'w', encoding='utf-8') as f:
            f.writelines(new_lines)
    except Exception as e:
        print(f"Error converting {file_path}: {e}")

def main():
    src_dir = r"c:\Users\kirby\app\Noctua Hub\composeApp\src"
    for root, dirs, files in os.walk(src_dir):
        for file in files:
            if file.endswith('.kt') or file.endswith('.kts'):
                file_path = os.path.join(root, file)
                print(f"Formatting: {file_path}")
                convert_file(file_path)

if __name__ == '__main__':
    main()
