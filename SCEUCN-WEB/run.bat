@echo off
echo Running [slice2cs ..\ZeroIce.ice] ..
slice2cs ..\ZeroIce.ice --output-dir .\ZeroIce\

REM echo Copying class from Server ..
REM xcopy /Y ..\SCEUCN-SERVER\Model\Vehiculo.cs .\Model\
REM xcopy /Y ..\SCEUCN-SERVER\Model\Persona.cs .\Model\
REM xcopy /Y ..\SCEUCN-SERVER\Model\Registro.cs .\Model\
REM xcopy /Y ..\SCEUCN-SERVER\Model\Logo.cs .\Model\
REM xcopy /Y ..\SCEUCN-SERVER\Controller\ModelConverter.cs .\Controller\

echo Running [dotnet run] ..
dotnet run