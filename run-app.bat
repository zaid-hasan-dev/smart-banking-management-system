@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
set "PATH=%JAVA_HOME%\bin;%~dp0.tools\apache-maven-3.9.11\bin;%PATH%"
cd /d "%~dp0"
mvn javafx:run
