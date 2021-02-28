$GIT_STATUS = $(git status -s)
$CURRENT_BRANCH = $(git branch --show-current)

if (![string]::IsNullOrEmpty($GIT_STATUS)) {
    Write-Output "Git Status:"
    Write-Output $GIT_STATUS
    Write-Output "You have uncommitted changes in your git tree. Commit the changes and try again."
    return
}

. .\version-number.ps1

$OLD_SNAPSHOT = "$VERSION-SNAPSHOT"

$POM_CLIENT = Get-Content ..\pom-deploy-client.xml
$POM_SERVER = Get-Content ..\pom-deployment.xml
$POM_DEV = Get-Content ..\pom.xml
$DOCKERFILE_SERVER = Get-Content ..\..\Dockerfile-server

$SET_VERSION=$VERSION

$POM_CLIENT = $POM_CLIENT -replace $OLD_SNAPSHOT,$SET_VERSION
Out-File -InputObject $POM_CLIENT -FilePath ..\pom-deploy-client.xml

$POM_SERVER = $POM_SERVER -replace $OLD_SNAPSHOT,$SET_VERSION
Out-File -InputObject $POM_SERVER -FilePath ..\pom-deployment.xml

$DOCKERFILE_SERVER = $DOCKERFILE_SERVER -replace $OLD_SNAPSHOT,$SET_VERSION
Out-File -InputObject $DOCKERFILE_SERVER -FilePath ..\..\Dockerfile-server

$COMMIT_MESSAGE="Release version $VERSION."

git commit -a -m $COMMIT_MESSAGE
git tag -a v$VERSION -m $COMMIT_MESSAGE

. .\update-version-number.ps1

$NEXT_SNAPSHOT = "$VERSION-SNAPSHOT"

$POM_CLIENT = $POM_CLIENT -replace $SET_VERSION,$NEXT_SNAPSHOT
Out-File -InputObject $POM_CLIENT -FilePath ..\pom-deploy-client.xml

$POM_SERVER = $POM_SERVER -replace $SET_VERSION,$NEXT_SNAPSHOT
Out-File -InputObject $POM_SERVER -FilePath ..\pom-deployment.xml

$POM_DEV = $POM_DEV -replace $OLD_SNAPSHOT,$NEXT_SNAPSHOT
Out-File -InputObject $POM_DEV -FilePath ..\pom.xml

$DOCKERFILE_SERVER = $DOCKERFILE_SERVER -replace $SET_VERSION,$NEXT_SNAPSHOT
Out-File -InputObject $DOCKERFILE_SERVER -FilePath ..\..\Dockerfile-server

$COMMIT_MESSAGE="Update pom files to version $NEXT_SNAPSHOT."

git commit -a -m $COMMIT_MESSAGE
git push origin $CURRENT_BRANCH
git push origin --tags