import re

file_path = "android/app/build.gradle"
try:
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    # Block to inject inside android { ... }
    compat_block = """
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = '17'
    }
"""

    if "compileOptions" in content:
        content = re.sub(r'compileOptions\s*\{[^}]*\}', 'compileOptions {\n        sourceCompatibility JavaVersion.VERSION_17\n        targetCompatibility JavaVersion.VERSION_17\n    }', content)
    else:
        content = re.sub(r'(android\s*\{)', r'\1' + compat_block, content, count=1)

    if "kotlinOptions" in content:
        content = re.sub(r'kotlinOptions\s*\{[^}]*\}', "kotlinOptions {\n        jvmTarget = '17'\n    }", content)

    with open(file_path, "w", encoding="utf-8") as f:
        f.write(content)
    print("Updated android/app/build.gradle successfully")
except Exception as e:
    print(f"Error: {e}")
