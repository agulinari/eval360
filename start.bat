@echo off
start "Api Gateway" /B java -jar api-gateway\target\api-gateway-0.0.1-SNAPSHOT.jar
%SystemRoot%\System32\timeout.exe /T 2 /NOBREAK >nul
start "Service Registry" /B java -jar service-registry\target\service-registry-0.0.1-SNAPSHOT.jar
%SystemRoot%\System32\timeout.exe /T 2 /NOBREAK >nul
start "Users" /B java -jar users\target\users-0.0.1-SNAPSHOT.jar
%SystemRoot%\System32\timeout.exe /T 2 /NOBREAK >nul
start "Projects" /B java -jar projects\target\projects-0.0.1-SNAPSHOT.jar
%SystemRoot%\System32\timeout.exe /T 2 /NOBREAK >nul
start "Templates" /B java -jar templates\target\templates-0.0.1-SNAPSHOT.jar
%SystemRoot%\System32\timeout.exe /T 2 /NOBREAK >nul
start "Evaluations" /B java -jar evaluations\target\evaluations-0.0.1-SNAPSHOT.jar
%SystemRoot%\System32\timeout.exe /T 2 /NOBREAK >nul
start "Notifications" /B java -jar notifications\target\notifications-0.0.1-SNAPSHOT.jar
%SystemRoot%\System32\timeout.exe /T 2 /NOBREAK >nul
start "Reports" /B java -jar reports\target\reports-0.0.1-SNAPSHOT.jar
