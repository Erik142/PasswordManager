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

in the same directory.

There is a config file in the Docker subdirectory in the repo. This is the configuration file that is used by the server and the web ui.

### Client

The client can be built by stepping into the "PasswordManager" subdirectory and running the following command:

```bash
mvn -f pom-deployment.xml clean package
```

Apache Maven is needed for this to work. Prebuilt images can be found in the "Releases" section on Github.

Be sure to specify the correct mode for the application in the "config.json" file under the output folder "target". For client mode, set

```json
"appMode":"Client"
```

For server mode, set

```json
"appMode":"Server"
```