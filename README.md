# tyche-showcase
Showcase for a Spring Boot project<br />
<br />
This is a part of my home automation project. It runs on a Raspberry Pi with a ZigBee module. Several light sources and various sensors are connected to it.<br />
The application uses REST requests to retrieve the initial configuration. Changes are communicated via WebSocket.<br />
<br />
Ideas:<br />
* [x] Pluggable WebSocket implementation
  - concrete implementation for Spring and Jetty
* [x] JSON Handling
  - json messages are parsed into classes via ObjectMapper (Jackson)
* [ ] REST APIs
* [ ] Persistant storage with MongoDB
  - sensors and their states are stored
  - sensors are loaded from the database when the application is started
* [ ] Mocking WebService endpoint
* [ ] Web-Frontend
* [ ] Docker setup
  - MongoDB

<br />
The project is still in progress and will be updated
