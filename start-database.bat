@echo off
start "" /min "C:\xampp\mysql_start.bat"
timeout /t 3 >nul
"C:\xampp\mysql\bin\mysql.exe" -u root < "%~dp0database.sql"
echo Database started and schema imported.
pause
