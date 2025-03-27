# tyche-showcase
Showcase for a Spring Boot project<br />
<br />
This is a part of my home automation project. It runs on a Raspberry Pi with a ZigBee module. Several light sources and various sensors are connected to it.<br />
The application uses REST requests to retrieve the initial configuration. Changes are communicated via WebSocket.<br />
<br />
Implementation:<br />
* [x] Pluggable WebSocket implementation
  - concrete implementation for Spring and Jetty (for the showcase)
* [x] JSON Handling
  - json messages are parsed into classes via ObjectMapper (Jackson)
  - using filters for serialization
* [x] REST APIs
  - Temperature endpoint
  - Humidity endpoint
  - Presence endpoint
  - LightLevel endpoint
  - Pressure endpoint
  - Sensors endpoint
* [x] SOAP Client
  - ~~WSDL files generated from fritzbox doc with ChatGPT~~
    - files aren't complete even after several attempts
  - ~~generated classes with maven plugin~~
    - As only a few interfaces are used, the classes were created manually for JAXB
  - SSL certificates in truststore
* [x] WebSocket
  - receives messages from Raspberry via basic WebSocket
  - delivers messages (to frontend) via STOMP
* [x] Persistant storage with MongoDB
  - sensors and their states are stored
  - sensors are loaded from the database when the application is started
  - lights and their states are stored
  - lights are loaded from the database when the application is started
  - custom methods and interface
* [x] Events
* [x] Spotify client
* [ ] Mocking WebService endpoint
* [x] Web-Frontend
  - https://github.com/Twigman/tyche-web-interface
* [x] Docker setup
  - environment for local IDE and production on raspberry
  - MongoDB
  - Tyche
* [x] JUnit Tests
  - HueLights
  - ChangeChecker
* [x] Spring Security
  - TLS
  - DPoP + Bearer
* [ ] UML diagrams
  - model classes
  - sequence for color profile usage

<br />
The project is still in progress and will be updated<br />
<br />
Start only MongoDB container for development with IDE:

```sh
docker compose -f docker-compose.dev.yml up -d
```

Build for Raspberry:

```sh
docker compose -f docker-compose.prod.yml up -d --build
```

Start on Raspberry:

```sh
docker compose -f docker-compose.prod.yml up -d
```
