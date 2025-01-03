# Проект: Управление балансом пользователя

## Описание

Данный проект представляет собой простое приложение на Java, которое управляет балансом пользователей через взаимодействие с базой данных PostgreSQL. На текущем этапе реализована базовая функциональность: хранение баланса пользователя и выполнение простейших операций: getBalance, putMoney, и takeMoney.

## Этапы разработки

1. Проектирование базы данных:
   - Создана таблица с полями:
     - user_id (ID пользователя)
     - balance (текущий баланс)
   
   Скриншот структуры базы данных:  
   (вставьте здесь скриншот)

2. Класс для работы с базой данных:
   - Реализован класс, включающий функции:
     - getBalance(userId) - получение текущего баланса по ID пользователя.
     - putMoney(userId, amount) - добавление денег на счет.
     - takeMoney(userId, amount) - снятие денег со счета.

3. Настройки подключения к базе данных:
   - Настройки (название БД, IP-адрес, логин и пароль) вынесены в отдельный файл, что позволяет пользователю легко их изменять.

4. Создание Rest API:
   - Реализован Rest API для операций:
     - GET /balance - получение баланса.
     - POST /putMoney - добавление денег.
     - POST /takeMoney - снятие денег.

## Технологии

- Язык программирования: Java
- База данных: PostgreSQL

## Запуск проекта

1. Установите PostgreSQL и создайте базу данных.
2. Скопируйте файлы проекта на локальную машину.
3. Настройте файл с данными для подключения к БД.
4. Запустите приложение.
   
![MWrPPTQiVAA](https://github.com/user-attachments/assets/0721839d-c007-4c47-a2ec-87ac8f998ee4)


## Дополнительная информация

- Вы можете найти дамп базы данных в этом репозитории.
- Пожалуйста, следите за обновлениями в проекте, функциональность будет расширяться.

Ссыл

== Спасибо за внимание! ==
