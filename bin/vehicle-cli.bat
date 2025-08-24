rem runs vehicle-cli-*.jar

@echo off

chcp 1251

set LANG=ru_RU.UTF-8
set LANGUAGE=ru
set LC_CTYPE=ru_RU.UTF-8

set TZ=UTC

set "CURRENT_DIR=%~dp0"

rem getting the full name of jar-file
set "JAR_FILE=%CURRENT_DIR%\vehicle-cli-*.jar"
for %%f in ("%JAR_FILE%") do (
    set "JAR_FILE=%CURRENT_DIR%\%%~nxf"
)

set "JAVA_VM_OPTS=-Xmx1024m"
set "JAVA_VM_OPTS=%JAVA_VM_OPTS% -Xms256m"
set "JAVA_VM_OPTS=%JAVA_VM_OPTS% -XX:-OmitStackTraceInFastThrow"

set "JAVA_CMD=java %JAVA_VM_OPTS% -jar ^"%JAR_FILE%^""

%JAVA_CMD% %*
