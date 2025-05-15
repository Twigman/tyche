# Tyche – Spring Boot Architecture Showcase

**Tyche** is the central automation service for my personal smart home setup.  
It collects data from various sensors, controls lights through ZigBee, and integrates with external services like my FritzBox router.  
The service runs on a Raspberry Pi and is designed to be modular, secure, and extensible. This repository serves as a **technical showcase**, focusing on clean architecture and implementation quality.

> ⚠️ Note: This project is intentionally incomplete and not directly executable.  
> Sensitive information (e.g., network endpoints, device MAC addresses, location data) has been removed or anonymized.  
> While example configuration files are included, some components are not fully configured for privacy reasons.  
> The purpose of this repository is to demonstrate architecture and implementation – not to provide a production-ready system.

---

## Project Overview

- **Communication**
  REST API for initial configuration; WebSocket/STOMP for live updates
- **Smart Home Control**
  Reads data from sensors, controls lighting devices via ZigBee 
- **External Integration**
  Connected to FritzBox to detect device presence (e.g., smartphone in Wi-Fi) 
- **Security** 
  TLS, Spring Security, DPoP & Bearer token support
- **Persistence**
  MongoDB for device state storage

---

## Implemented Features

### Infrastructure & Communication

- **Pluggable WebSocket implementation**
  - Support for Spring and Jetty (as part of this showcase)
- **REST APIs**
  - Endpoints for: Temperature, Humidity, Presence, Light Level, Pressure, Phone, Profile, Timer
- **WebSocket Integration**
  - Incoming messages from Raspberry Pi via basic WebSocket
  - Outgoing updates to frontend via STOMP
- **SOAP Client**
  - Access to FritzBox using JAXB
  - SSL certificates included in a custom truststore

### Data Handling & Persistence

- **JSON Handling**
  - Message parsing using Jackson ObjectMapper
  - Controlled serialization using filters
- **MongoDB Integration**
  - Persistent storage for sensors, lights and profiles
  - Custom repository interfaces and methods

---

## Architecture & Design Highlights

- **Layered Architecture**  
  Clear separation between controllers, services, and data access layers, following SOLID principles and single-responsibility design.

- **Modular & Extensible Design**  
  Components are isolated and replaceable. Each module (e.g., sensor types, light control) adheres to strict interface boundaries, allowing for future expansion with minimal code changes.

- **Event-Driven Processing**  
  Core interactions (especially via WebSocket/STOMP) are fully event-based. Incoming and outgoing events are loosely coupled via a shared dispatcher mechanism, improving testability and scalability.

- **Polymorphic Sensor Modeling**  
  Sensors are built from multiple component objects (e.g., config/state) that can be either generalized or fully customized depending on the device.  
  A polymorphic inheritance approach enables unified handling across different sensor types while preserving flexibility.

- **Advanced Configuration System**  
  Automation behavior is defined via YAML files, allowing users to create dynamic behavior profiles.  
  These profiles can include nested references (e.g., shared light configurations), making the system highly reusable and easy to extend.  
  Devices are mapped to rooms via ID, enabling automatic group-based logic without changing the core logic.

---

## Configuration & Automation Profiles

Tyche's behavior is highly configurable via external YAML files. This includes dynamic automation profiles, reusable color presets, and rule-based behavior switching.

### Profile System

The `automation-profiles.yml` defines behavior modes (e.g. `HOME`, `AWAY`, `COOKING`) which control:

- Whether motion detection and light automation are active
- Room-specific presets (e.g., enabling lights in `LIVINGROOM`)
- Transitions between profiles (e.g., auto-switching to `COOKING` when activity pattern is detected in the kitchen)

### Color Profiles

Light configurations (color temperature, brightness, effects) are abstracted via named color profiles defined in `color-profiles.yml`. These profiles can be reused across rooms and scenarios.

### Example Snippets

```yaml
automation:
  activeProfile: HOME
  profiles:
    HOME:
      activeMotionDetection: true
      activeLightAutomation: true
      sensors:
        targetTemperatureLivingroom: 20.5
      presets:
        ALL:
          lights:
            enabled: false
        LIVINGROOM:
          lights:
            enabled: true
        KITCHEN:
          autoProfileSwitch:
            enabled: true
            visitThreshold: 3
            inTimespanSec: 180
            toProfile: COOKING
    COOKING:
      activeMotionDetection: true
      activeLightAutomation: true
      autoHomeProfile: true
      presets:
        ALL:
          lights:
            enabled: false
        LIVINGROOM:
          lights:
            enabled: true
        KITCHEN:
          lights:
            enabled: true
            ignoreSensors: true
```

```yaml
color:
  profiles:
    DEFAULT_CT_BRI:
      bri: 252
      ct: 341
```

This modular configuration allows for:
- Declarative automation logic
- Easy expansion with new rooms or devices
- Reusability of color profiles and presets
- Flexible event-triggered behavior

---

## Web Frontend

A companion frontend project is available here:  
👉 [tyche-web-interface](https://github.com/Twigman/tyche-web-interface)

---

## Docker Setup

### Development (MongoDB only):

```bash
docker compose -f docker-compose.dev.yml up -d
```

### Build for Raspberry:

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

### Start on Raspberry:

```bash
docker compose -f docker-compose.prod.yml up -d
```

---

## Final Note

This repository is a technical showcase, focused on architecture and code quality.
It is not intended for direct deployment or reuse without adjustment.


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
