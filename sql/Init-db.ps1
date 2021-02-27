$installedPrograms = Get-ChildItem HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall | % { Get-ItemProperty $_.PsPath }
$postgre = $installedPrograms | Select DisplayName, InstallLocation | Where-Object -Property DisplayName -Like '*PostgreSQL*' | Select InstallLocation
$location = $postgre.InstallLocation

$psqlPath = Join-Path -Path $location -ChildPath 'bin\psql.exe'

$psqlPath = '''' + $psqlPath + ''''

echo $psqlPath

$parentFolder = Split-Path -parent $MyInvocation.MyCommand.Path
$sqlScriptPath = Join-Path -Path $parentFolder -ChildPath 'passwordmanager-schema.sql'

$sqlScriptPath = '''' + $sqlScriptPath + ''''

echo $parentFolder
echo ''
echo $sqlScriptPath
echo ''

Invoke-Expression "& $psqlPath -h localhost -d postgres -U postgres -p 5432 -a -q -f $sqlScriptPath"
