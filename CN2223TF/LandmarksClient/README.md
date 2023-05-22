# Landmarks gRPC Client

> This file contains the technical documentation for the gRPC client used to interact with the Landmarks service. The
> Landmarks client is a console application that allows users to submit photos, retrieve results, and get identified
> photos from the Landmarks service.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Installation and Usage](#installation-and-usage)

---

## Prerequisites

Before using the Landmarks client, ensure you have the following:

- Java Development Kit (JDK) 11 or higher installed.
- Access to the Landmarks service endpoint (IP address and port number).

<!--Update after IP lookup functionality is implemented-->

---

## Setup

To use the Landmarks client, follow these steps:

1. Clone the Landmarks App repository from GitHub.

---

## Installation and Usage

1. Build the client project using Maven:

   ```shell
   mvn clean package
   ```

   Remember that you need to have Maven installed in your machine. If you don't have it, you can use an IDE like
   IntelliJ IDEA or Eclipse, which have Maven integrated.

2. Update the `SVC_IP` and `SVC_PORT` constants in the `LandmarksClient` class with the IP address and port number of
   the Landmarks service endpoint.

3. Run the Landmarks client using the following command:

   ```shell
   java -jar target/LandmarksClient-1.0-jar-with-dependencies.jar
   ```

4. The client will display a menu with options for interacting with the Landmarks service. Follow the on-screen
   instructions to submit photos, retrieve results, and get identified photos.

5. Follow the prompts in the console to enter the required information, such as photo name, photo path, request ID, and
   confidence threshold.

6. The client will communicate with the Landmarks service and display the results or save the downloaded map images in
   the specified directory.
