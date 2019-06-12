# Instrucciones para agregar Web Razor Pages (ASP .NET)

- Contar con conexión a Internet estable.
- (optativo\*) Mantener actualizada la dependencia con SQLite [Microsoft.EntityFrameworkCore.Sqlite](https://www.nuget.org/packages/Microsoft.EntityFrameworkCore.Sqlite/)
  - `dotnet add package Microsoft.EntityFrameworkCore.Sqlite`
- Agregar vía .NET CLI la dependencia [Microsoft.AspNet.Razor](https://www.nuget.org/packages/Microsoft.AspNet.Razor/) y [Microsoft.AspNetCore.Razor.Design](https://www.nuget.org/packages/Microsoft.AspNetCore.Razor.Design/)
  - `dotnet add package Microsoft.AspNet.Razor`
  - `dotnet add package Microsoft.AspNetCore.Razor.Design`
- Revisar el archivo `SCEUCN-SERVER.csproj` y asegurarse que se encuentran declaradas las 2 dependencias.

```xml
<Project Sdk="Microsoft.NET.Sdk">

  <PropertyGroup>
    <OutputType>Exe</OutputType>
    <TargetFramework>netcoreapp2.2</TargetFramework>
    <RootNamespace>SCEUCN_SERVER</RootNamespace>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="Microsoft.AspNet.Razor" Version="3.2.7" />
    <PackageReference Include="Microsoft.AspNetCore.Razor.Design" Version="2.2.0" />
    <PackageReference Include="Microsoft.EntityFrameworkCore.SQLite" Version="2.2.4" />
    <PackageReference Include="zeroc.ice.net" Version="3.7.2" />
  </ItemGroup>

</Project>
```
