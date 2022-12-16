# CI/CD - Запуск тестов на Jenkins

Проект содержит:
- конфиги для настройки docker-compose
- конфиги для генерации job-ов в Jenkins
- скрипт генерации pipeline для запуска UI-тестов на Selenoid
- настройку Allure-отчетности


## Быстрый старт:
1. В приложении Docker Desktop запустить:
- docker-compose jenkins 
- контейнер с Selenoid_1
![img.png](img/img.png)
2. Поднять Selenoid UI для мониторинга работы тесты внутри Selenoid (необязательно).

- Для этого из директории c:\Work\OTUS\Selenoid\ggr_config\ нужно выполнить команду
```
start /b selenoid-ui_windows_amd64.exe --selenoid-uri http://127.0.0.1:4445 -listen ":8090" -allowed-origin "*"
```
- После выполнения команды сомандную строку не закрывать. 
- Selenoid UI будет доступен по адресу: http://127.0.0.1:8090/
3. Авторизоваться в Jenkins: http://localhost/ (admin/admin)
4. Собрать с параметрами по-умолчанию сборку для job **ui_autotests**
5. Мониторинг выполнения сборка по этапам:
![img_2.png](img/img_2.png)
Мониторим процессы, виидим, что подняты контейнеры динамического сборщика maven-slave и один контейнера Selenoid с chrome v.104.0
![img_1.png](img/img_1.png)
6. Проверяем генерацию Allure Report
![img_3.png](img/img_3.png)
 

