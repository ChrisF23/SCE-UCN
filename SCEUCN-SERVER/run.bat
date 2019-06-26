@echo off
echo Running [slice2cs ..\ZeroIce.ice] ..
slice2cs ..\ZeroIce.ice --output-dir .\ZeroIce\

echo Running [dotnet run] ..
dotnet run --verbosity normal
