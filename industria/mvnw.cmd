@echo off
setlocal

if "%JAVA_HOME%"=="" (
    echo [ERROR] JAVA_HOME not set. Configure JAVA_HOME e tente novamente.
    exit /b 1
)

set "WRAPPER_JAR=%~dp0.mvn\wrapper\maven-wrapper.jar"
set "DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

if not exist "%WRAPPER_JAR%" (
    echo Downloading Maven Wrapper...
    powershell -Command "Invoke-WebRequest -Uri '%DOWNLOAD_URL%' -OutFile '%WRAPPER_JAR%'"
)

"%JAVA_HOME%\bin\java.exe" ^
    -Dmaven.multiModuleProjectDirectory="%~dp0" ^
    -classpath "%WRAPPER_JAR%" ^
    org.apache.maven.wrapper.MavenWrapperMain %*
