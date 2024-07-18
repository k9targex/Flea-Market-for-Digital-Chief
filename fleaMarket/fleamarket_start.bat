@echo off
setlocal EnableDelayedExpansion

set /p PGPATH="Enter the path to PostgreSQL bin directory (e.g., C:\Program Files\PostgreSQL\16\bin): "
set /p db_user="Enter pgAdmin username: "
set /p db_password="Enter pgAdmin password: "


REM Путь к файлу application.properties 
set properties_file=src\main\resources\application.properties

REM Поиск и замена имени пользователя
powershell -Command "(Get-Content '%properties_file%') | ForEach-Object { $_ -replace '^spring.datasource.username=.*', 'spring.datasource.username=%db_user%' } | Set-Content '%properties_file%'"

REM Поиск и замена пароля
powershell -Command "(Get-Content '%properties_file%') | ForEach-Object { $_ -replace '^spring.datasource.password=.*', 'spring.datasource.password=%db_password%' } | Set-Content '%properties_file%'"




REM Create the database FleaMarket with case sensitivity
"%PGPATH%\psql" -U %db_user% -c "CREATE DATABASE \"FleaMarket\";" -h localhost

if %ERRORLEVEL% NEQ 0 (
    echo Error creating the database.
    exit /b %ERRORLEVEL%
)

echo Database "FleaMarket" created successfully.

REM Run mvn clean install
call mvn clean install

if %ERRORLEVEL% NEQ 0 (
    echo Error running mvn clean install.
    exit /b %ERRORLEVEL%
)

REM Change directory to the target folder where the JAR file is located


REM Run java -jar
java -jar target/fleaMarket-0.0.1-SNAPSHOT.jar

echo Application started.

pause
