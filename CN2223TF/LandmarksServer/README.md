# Landmarks gRPC Server

> The Landmarks gRPC Server is a Java-based server application that provides a gRPC service for detecting landmarks in
> photos. This README document provides technical documentation for the server application, including an overview,
> installation instructions, and usage details.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Installation and Usage](#installation-and-usage)

---

## Overview

The Landmarks gRPC Server is implemented using Java and the gRPC framework. It provides the following main features:

- Receiving and processing photos to detect landmarks.
- Retrieving results of landmark detection for a given request ID.
- Retrieving identified photos with a confidence threshold.

The server uses Google Cloud Firestore for metadata storage and Google Cloud Storage for storing photos. The landmark
detection process is performed using a Pub/Sub-based approach.

The server consists of the following main components:

- `Main`: The entry point of the server application. It initializes the server, sets up dependencies, and starts the
  gRPC server.
- `LandmarksServer`: The implementation of the gRPC service. It handles gRPC requests, processes photos, and retrieves
  results.
- `Service`: The service layer that encapsulates the business logic and interacts with data storage and landmark
  detection components.
- `Config`: A configuration class that holds various constants used by the server.

---

## Prerequisites

Before running the Landmarks gRPC Server, ensure you have the following:

- Java Development Kit (JDK) 11 or higher installed.
- Google Cloud project with enabled APIs: Firestore, Storage and Pub/Sub.
- Google Cloud service account key file with appropriate permissions.

---

## Setup

To set up the Landmarks gRPC Server, follow these steps:

1. Clone the Landmarks App repository from GitHub.
2. Set the environment variable `GOOGLE_APPLICATION_CREDENTIALS` to the path of your service account key file.
3. Replace the placeholders in the `Config` class with your project-specific values:
    - `PROJECT_ID`: Your Google Cloud project ID.
    - `SUBSCRIPTION_ID`: The name of the Pub/Sub subscription to use.
    - `MAPS_BUCKET_NAME`: The name of the Google Cloud Storage bucket for storing maps.
    - `FIRESTORE_COLLECTION_NAME`: The name of the Firestore collection for storing landmarks.
    - `GOOGLE_MAPS_API_KEY`: Your API key for the Google Maps API.

---

## Installation and Usage

To run the Landmarks gRPC Server, follow these steps:

1. Build the server application using Maven:

   ```shell
   mvn clean package
   ```

   Remember that you need to have Maven installed in your machine. If you don't have it, you can use an IDE like
   IntelliJ IDEA or Eclipse, which have Maven integrated.

2. To start the Landmarks gRPC Server, use the following command:

    ```shell
    java -jar target/LandmarksServer-1.0-jar-with-dependencies.jar
    ```

By default, the server listens on port 8000.

After starting the server, you can use a gRPC client to interact with the server's API. The server provides the
following gRPC service methods:

- `submitPhoto`: Submits a photo for landmark detection. The client sends the photo data as a stream
  of `SubmitPhotoRequest` messages. The server processes the photo and assigns a unique request ID.
- `getResults`: Retrieves the results of landmark detection for a given request ID.
- `getIdentifiedPhotos`: Retrieves a list of identified photos with a confidence threshold.

Refer to the [LandmarksContract](../LandmarksContract/README.md) project for more details about the gRPC service.
