import os
import re

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    new_lines = []
    for line in lines:
        match = re.match(r'^( +)(.*)', line)
        if match:
            spaces = match.group(1)
            content = match.group(2)
            new_lines.append(spaces.replace('    ', '  ') + content + '\n')
            # The replace will replace 8 spaces with 4, 12 spaces with 6.
        else:
            new_lines.append(line)
            
    with open(filepath, 'w', encoding='utf-8') as f:
        f.writelines(new_lines)

files_to_format = [
    'composeApp/src/commonMain/kotlin/jp/girky/wf_noctuahub/utils/Translations.kt',
]
for file in files_to_format:
    process_file(file)

