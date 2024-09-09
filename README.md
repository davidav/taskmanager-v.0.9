# Task Management - простая система управления задачами


## Возможности:
- Система обеспечивает создание, редактирование, удаление и просмотр задач.
- Система поддерживает аутентификацию и авторизацию пользователей по email и паролю
- Доступ к API аутентифицирован с помощью JWT токена.
- Пользователи могут управлять своими задачами: создавать новые, редактировать существующие, просматривать и удалять, менять статус и назначать исполнителей задачи
- Пользователи могут просматривать задачи других пользователей, а исполнители задачи могут менять статус своих задач.
- API позволяет получать задачи конкретного автора или исполнителя, а также все комментарии к ним. 
- Обеспечена фильтрация и пагинация вывода.
- Сервис обрабатывает ошибки, а также валидирует входящие данные.
- API описан с помощью Open API и Swagger.
 
---
## Технологии:
### Проект выполнен с использованием фреймфорка Spring Boot
- Защита от несанкционированного доступа выполнена на Spring Security
- В процессе мапинга сущностей использован MapStruct
- Обработка ошибок производится на уровне контроллеров
- Для хранения информации использована база данных Postgresql
- Для кеширования Refresh token использована база данных Redis
- Рабочая среда подготавливается с помощью Docker
  - контейнер для базы данных postgresql; 
  -  контейнер для базы данных redis;


---
## Запуск приложения и его окружения
- программа контейнеризиации Docker должна быть запущена
- из терминала заустить файл настройки окружения docker-compose.yml из папки docker в корневой директории проекта
- запустить программу

---

#### Выполнены интеграционные тесты на основные операции с данными





