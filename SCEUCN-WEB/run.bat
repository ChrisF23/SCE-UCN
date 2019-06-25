@echo off
echo Running [slice2cs ..\ZeroIce.ice] ..
slice2cs ..\ZeroIce.ice

echo Copying class from Server ..
xcopy /Y ..\SCEUCN-SERVER\Model\Vehiculo.cs .\Model\
xcopy /Y ..\SCEUCN-SERVER\Model\Persona.cs .\Model\
xcopy /Y ..\SCEUCN-SERVER\Model\Registro.cs .\Model\
xcopy /Y ..\SCEUCN-SERVER\Model\Logo.cs .\Model\
xcopy /Y ..\SCEUCN-SERVER\Controller\ModelConverter.cs .\Controller\

echo Running [dotnet run] ..
dotnet run