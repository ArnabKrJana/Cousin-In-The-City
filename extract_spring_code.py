import os

PROJECT_ROOT = os.getcwd()
OUTPUT_FILE = "all_spring_code.txt"

# File extensions to include
EXTENSIONS = {
    ".java",
    ".kt",
    ".kts",
    ".properties",
    ".yml",
    ".yaml",
    ".xml",
    ".sql",
    ".gradle",
    ".groovy",
    ".sh",
    ".bat",
    ".cmd",
}

# Files without useful extensions
SPECIAL_FILES = {
    "Dockerfile",
    "docker-compose.yml",
    "docker-compose.yaml",
    "Jenkinsfile",
    ".gitignore",
    "gradlew",
    "gradlew.bat",
    "settings.gradle",
    "settings.gradle.kts",
    "build.gradle",
    "build.gradle.kts",
}

# Directories to skip
EXCLUDE_DIRS = {
    "build",
    "target",
    ".gradle",
    ".idea",
    ".git",
    "node_modules",
    "out",
}

def should_include(filename: str) -> bool:
    return (
        filename in SPECIAL_FILES
        or os.path.splitext(filename)[1] in EXTENSIONS
    )

with open(OUTPUT_FILE, "w", encoding="utf-8") as out:
    for root, dirs, files in os.walk(PROJECT_ROOT):

        # Skip unwanted directories
        dirs[:] = [d for d in dirs if d not in EXCLUDE_DIRS]

        for filename in sorted(files):
            if not should_include(filename):
                continue

            file_path = os.path.join(root, filename)
            relative_path = os.path.relpath(file_path, PROJECT_ROOT)

            out.write("\n" + "=" * 120 + "\n")
            out.write(f"FILE: {relative_path}\n")
            out.write("=" * 120 + "\n\n")

            try:
                with open(file_path, "r", encoding="utf-8") as f:
                    out.write(f.read())
            except UnicodeDecodeError:
                out.write("[SKIPPED: Binary or unsupported encoding]\n")
            except Exception as e:
                out.write(f"[ERROR READING FILE]: {e}\n")

            out.write("\n\n")

print(f"✅ Extracted project source into '{OUTPUT_FILE}'")