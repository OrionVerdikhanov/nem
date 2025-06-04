#!/bin/bash

set -euo pipefail

# Цветной вывод для информативности
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Инициализация git submodule...${NC}"
git submodule update --init --recursive

# Проверка наличия директории _symbol
if [[ ! -d "_symbol" ]]; then
    echo -e "${RED}Ошибка: директория _symbol не найдена!${NC}"
    exit 1
fi

echo -e "${GREEN}Включаем sparse checkout для _symbol...${NC}"
git -C _symbol config core.sparseCheckout true

SPARSE_FILE=".git/modules/_symbol/info/sparse-checkout"

# Создаем sparse-checkout файл, если его нет
if [[ ! -f "$SPARSE_FILE" ]]; then
    mkdir -p "$(dirname "$SPARSE_FILE")"
    touch "$SPARSE_FILE"
fi

# Добавляем нужные директории, избегая дублирования
for dir in "jenkins/*" "linters/*" "tests/*"; do
    if ! grep -qxF "$dir" "$SPARSE_FILE"; then
        echo "$dir" >> "$SPARSE_FILE"
        echo -e "${GREEN}Добавлено для sparse checkout: $dir${NC}"
    fi
done

echo -e "${GREEN}Применяем sparse checkout...${NC}"
git submodule update --force --checkout _symbol

echo -e "${GREEN}Инициализация завершена успешно!${NC}"
