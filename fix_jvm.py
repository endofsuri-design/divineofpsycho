import re, glob, os

files = glob.glob('**/build.gradle*', recursive=True)
for path in files:
    if os.path.isfile(path):
        with open(path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Replace Java 1.8 / VERSION_1_8 with 17
        new_content = re.sub(r'VERSION_1_8', 'VERSION_17', content)
        new_content = re.sub(r'jvmTarget\s*=\s*[\'"]1\.8[\'"]', 'jvmTarget = "17"', new_content)
        new_content = re.sub(r'targetCompatibility\s*=\s*[\'"]1\.8[\'"]', 'targetCompatibility = "17"', new_content)
        new_content = re.sub(r'sourceCompatibility\s*=\s*[\'"]1\.8[\'"]', 'sourceCompatibility = "17"', new_content)
        
        if new_content != content:
            with open(path, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print("Fixed:", path)
