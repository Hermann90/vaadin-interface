import sys

def replace_password(file_path, new_password):
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    content = content.replace("<PASSWORD>", new_password)

    with open(file_path, "w", encoding="utf-8") as f:
        f.write(content)

if __name__ == "__main__":
    password = sys.argv[1]
    replace_password("test.yml", password)
