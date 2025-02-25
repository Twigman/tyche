# tyche-showcase
Showcase for a Spring Boot project<br />
<br />
This is a part of my home automation project. It runs on a Raspberry Pi with a ZigBee module. Several light sources and various sensors are connected to it.<br />
The application uses REST requests to retrieve the initial configuration. Changes are communicated via WebSocket.<br />
<br />
Ideas:<br />
* [x] Pluggable WebSocket implementation
  - concrete implementation for Spring and Jetty (for the showcase)
* [x] JSON Handling
  - json messages are parsed into classes via ObjectMapper (Jackson)
  - using filters for serialization
* [x] REST APIs
  - Temperature endpoint
  - Sensors endpoint
* [x] SOAP Client
  - ~~WSDL files generated from fritzbox doc with ChatGPT~~
    - files aren't complete even after several attempts
  - ~~generated classes with maven plugin~~
    - As only a few interfaces are used, the classes were created manually for JAXB
  - SSL certificates in truststore
* [x] Persistant storage with MongoDB
  - sensors and their states are stored
  - sensors are loaded from the database when the application is started
  - lights and their states are stored
  - lights are loaded from the database when the application is started
  - custom methods and interface
* [x] Events
* [ ] Mocking WebService endpoint
* [ ] Web-Frontend
* [x] Docker setup
  - environment for local IDE and production on raspberry
  - MongoDB
  - Tyche
* [x] JUnit Tests
  - HueLights
  - ChangeChecker
* [ ] UML diagrams  

<br />
The project is still in progress and will be updated<br />
<br />
Start only MongoDB container for development with IDE:<br />
`docker compose -f docker-compose.dev.yml up -d`<br />
<br />
Build for Raspberry:<br /> 
`docker compose -f docker-compose.prod.yml up -d --build`<br />
<br />
Start on Raspberry:<br />
`docker compose -f docker-compose.prod.yml up -d`
