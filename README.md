# PasswordManager

This is an educational project at Chalmers University of Technology. The project aims to implement a simple password manager with a client and a server.

## Components

The project is divided into three parts: The PasswordManager client/server written in Java, the web UI written with Node.JS and pug, and last but not least the PostgreSQL database.

## Installation/build instructions

### Server stack

The server stack is built using Docker compose. Simply run the following docker compose command to build and deploy the server stack:

```bash
docker-compose up --build -d
```

in the root folder of the git repo. This will deploy the server, web ui and postgresql server. Note that the postgresql database will not retain its data unless specified in the docker-compose.yaml file. Use the docker-compose-permanent.yaml for this. Docker will use an internal volume to store the database data when using this mode. Execute the following to run with permanent database data:

```
docker-compose -f docker-compose-permanent.yaml up --build -d
```

To stop and remove the server stack, simply run

```bash
docker-compose down
```

in the same directory. If running the permanent database version and you want to delete the postgreSQL database data, then run the following command:

```
docker-compose -f docker-compose-permanent.yaml down -v
```

There is a config file in the Docker subdirectory in the repo. This is the configuration file that is used by the server and the web ui when running the docker-compose commands above.

### Client

The client can be built by stepping into the "PasswordManager" subdirectory and running the following command:

```bash
mvn -f pom-deploy-client.xml clean package
```

When building a release build of the client with the command above, a jar is created and the corresponding client config file is copied to the same directory as the jar file, so the user does not have to specify any explicit config file. A zip file is also created, containing the jar and config file.

Apache Maven is needed for this to work. Prebuilt images can be found in the "Releases" section on Github.

### Web UI
The web UI is written in Node.JS and Pug. To run the Web UI, simply step into "Password-LostPassword" subdirectory, then run the following two commands:

```
npm install
```

```
npm start
```

The web UI will listen on port 3000 by default. This can however be specified in the config.json file located in the same directory as the Node.JS web UI project. The web UI will also start as a part of the server stack when building and starting the server stack with the docker-compose commands above.

## Configuration

The program uses a configuration file named config.json. The config file is written in json and is used to configure the application mode, server IP address and port, as well as various server secrets. See below for a complete reference of the config file:

### appmode

Used to specify if the app should run in client or server mode. For client mode, specify

```json
"appMode":"Client"
```

For server mode, set

```json
"appMode":"Server"
```

### serverIp

Used to specify the IP address or hostname for the server. If the app is running in client mode, this is the IP address/hostname used to connect to the server. If the app is running in server mode, this is the IP address that the server is listening to. See examples below:

```json
"serverIp":"my-public-server.com"
```

```json
"serverIp":"127.0.0.1"
```

```json
"serverIp":"0.0.0.0"
```

### serverPort

Used to specify the server port. If the app is running in client mode, this is the port used to connect to the server. If the app is running in server mode, this is the port that the server will be listening to. See example below:

```json
"serverPort":50000
```

### dbHostName

Only used in server mode and by the Node.JS web UI. This specifies the IP address/hostname of the database server. Note that only PostgreSQL servers are supported. See example below:

```json
"dbHostName":"my-public-database.com"
```

```json
"dbHostName":"192.168.0.5"
```

### dbPort

Only used in server mode and by the Node.JS web UI. This specifies the port that the PostgreSQL database server is listening to. See example below:

```json
"dbPort":5432
```

### dbUserName

Only used in server mode and by the Node.JS web UI. This specifies the user used to connect to the PostGreSQL database server. If you are building the server stack yourself with Docker, the default postgresql username is "passwordmanager". See example below:

```json
"dbUserName":"passwordmanager"
```

### dbPassword

Only used in server mode and by the Node.JS web UI. This specifies the user used to connect to the PostGreSQL database server. If you are building the server stack yourself with Docker, the default postgresql password for the passwordmanager user is "passwordmanager". See example below:

```json
"dbPassword":"passwordmanager"
```

```json
"dbPassword":"my-secret-password"
```

### webPort

Used only for the Node.JS web UI. This specifies the port that the web UI is listening to. See example below:

```json
"webPort":3000
```

### publicDomainName

Used only in server mode. This specifies the public domain name for the Node.JS web UI. If the web UI and server are only run locally, specify the IP address/local hostname for the web UI. See examples below:

```json
"publicDomainName":"my-public-webui.com"
```

```json
"publicDomainName":"192.168.0.10"
```

### serverEmail

Used only in server mode. This specifies the email-address used to send "forgot password" emails to the user. As of right now, only Google Gmail addresses are supported by the application. See example below:

```json
"serverEmail":"my-sweet-google-mail@gmail.com"
```

### serverPassword
Used only in server mode. This specifies the password for the email-address specified in "serverEmail". See example below:

```json
"serverPassword":"my.secret_p@ssw0rd"
```