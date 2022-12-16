# CI/CD - Запуск тестов на Jenkins

Проект содержит:
- конфиги для настройки docker-compose для Jenkins
- конфиги для генерации job-ов в Jenkins
- скрипт генерации pipeline для запуска UI-тестов на Selenoid
- настройку Allure-отчетности


## Быстрый старт:
1. В приложении Docker Desktop запустить:
- docker-compose jenkins 
- контейнер с Selenoid_1
![img.png](img/img.png)
2. Поднять Selenoid UI для мониторинга работы тестов внутри Selenoid (необязательно).

- Для этого из директории c:\Work\OTUS\Selenoid\ggr_config\ нужно выполнить команду
```
start /b selenoid-ui_windows_amd64.exe --selenoid-uri http://127.0.0.1:4445 -listen ":8090" -allowed-origin "*"
```
- После выполнения команды сомандную строку не закрывать. 
- Selenoid UI будет доступен по адресу: http://127.0.0.1:8090/
3. Авторизоваться в Jenkins: http://localhost/ (admin/admin)
4. Собрать с параметрами по-умолчанию сборку для job **ui_autotests**
5. Мониторинг выполнения сборки по этапам:
![img_2.png](img/img_2.png)
- Checkout - коннект к репозиторию с тестами на git и checkout его на docker-образ Jenkins
- Running UI tests - выгрузка всех зависимостей maven и запуск тестов. Для каждого теста будет подниматься образ selenoid с настроенной версией обозревателя.
- reports - генерация allure-отчетов из директории ./allure_results/

- Мониторим процессы, виидим, что подняты контейнеры динамического сборщика maven-slave и один контейнера Selenoid с chrome v.104.0
![img_1.png](img/img_1.png)
6. Проверяем генерацию Allure Report
![img_3.png](img/img_3.png)

## Конфигурация docker-compose Jenkins

## Настройки Jenkins


## Генерация job


## Скрипт генерации pipeline

## Настройка Allure Reports
1. В java-проекте должны быть произведены все настройки для Allure - см. [pom.xml](pom.xml)
2. В Jenkins установить Allure Plugin
3. В Jenkins необходимо настроить установку Allure Commandline:
- Перейти в "Настроить Jenkins" → "Конфигурация глобальных инструментов"
- В блоке Allure Commandline нажать кнопку Add Allure Commandline
- В поле Name вписать наименование Allure Commandline, например, Allure 2.20.1
- Выбрать версию библиотеки, которая будет выкачана из Maven central, например, 2.20.1
3. Конфигурация Job для сборки отчета:
- Создать тестовый job свободной конфигурацией.
- В разделе Post-build Actions нажать кнопку Add post-build action → Allure Report
- В поле Results указать путь до директории «allure-results» 
4. После выполнения всех настроек запустите свою джобу. 
5. После ее выполнения в блоке Build History напротив номера сборки появится значок Allure, 
кликнув по которому, вы увидите сформированный html-отчет (пустой).

Эти манипуляции позволяют закачать нужные зависимости. 
Теперь будет работать генерация allure-отчетов через stage('report') в скрипте pipeline .
