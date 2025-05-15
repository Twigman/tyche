# Tyche – Spring Boot Architecture Showcase

**Tyche** is the central automation service for my personal smart home setup.  
It collects data from various sensors, controls lights through ZigBee, and integrates with external services like my FritzBox router.  
The service runs on a Raspberry Pi and is designed to be modular, secure, and extensible. This repository serves as a **technical showcase**, focusing on clean architecture and implementation quality.

> ⚠️ Note: This project is intentionally incomplete and not directly executable.  
> Sensitive information (e.g., network endpoints, device MAC addresses, location data) has been removed or anonymized.  
> While example configuration files are included, some components are not fully configured for privacy reasons.  
> The purpose of this repository is to demonstrate architecture and implementation – not to provide a production-ready system.

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
NOTE: The uploaded project is not complete/executable. The real configuration and some config beans are missing! This is an architecture showcase with implementation details in the first place.<br />
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
