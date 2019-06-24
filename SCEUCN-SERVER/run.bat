@echo off
echo Running [slice2cs ..\Model.ice] ..
slice2cs ..\Model.ice

echo Running [dotnet run] ..
dotnet run