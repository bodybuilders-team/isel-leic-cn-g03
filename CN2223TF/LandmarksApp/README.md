# Landmarks Application

> This file contains the Landmarks Application documentation for the Landmarks Recognition System.

The Landmarks App is an application for the detection and processing of landmarks. It leverages various services such as
Google Cloud Firestore, Google Cloud Storage, Google Vision API, Google Maps API, and Google Pub/Sub.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Installation and Usage](#installation-and-usage)
- [Architecture](#architecture)
- [Integration with Google Cloud Services](#integration-with-google-cloud-services)
- [Logging](#logging)
- [Error Handling](#error-handling)

---

## Prerequisites

Before running the Landmarks App, ensure that you have the following:

- Java Development Kit (JDK) 11 or higher installed.
- Google Cloud project with enabled APIs: Firestore, Storage, Vision, Maps, and Pub/Sub.
- Google Cloud service account key file with appropriate permissions.

---

## Setup

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

To build and run the Landmarks App, follow these steps:

1. Open a terminal or command prompt and navigate to the root directory of the Landmarks App.
2. Build the application using Maven:

   ```shell
   mvn clean package
   ```

   Remember that you need to have Maven installed in your machine. If you don't have it, you can use an IDE like
   IntelliJ IDEA or Eclipse, which have Maven integrated.

3. Run the application:

   ```shell
    java -jar target/LandmarksApp-1.0-jar-with-dependencies.jar
    ```

4. The Landmarks App will start and begin processing incoming messages from the Pub/Sub subscription. Press Enter to
   stop the application.

---

## Architecture

The Landmarks App follows a modular architecture with the following main components:

- **Main**: The entry point of the application. It initializes the necessary services and starts the Landmarks App.
- **LandmarksApp**: The main application class that coordinates the processing of incoming messages from Pub/Sub.
- **StorageService**: Provides functionality for interacting with Google Cloud Storage, including storing and retrieving
  photos and maps.
- **LandmarkDetectionService**: Implements the landmark detection logic using the Google Vision API.
- **MapsService**: Handles retrieving static maps from the Google Maps API.
- **LandmarksGooglePubsubService**: Connects to Google Pub/Sub and subscribes to incoming messages for processing.

---

## Integration with Google Cloud Services

The Landmarks App integrates with various Google Cloud services:

- **Google Cloud Firestore**: Used for storing metadata and landmark information.
- **Google Cloud Storage**: Stores photos and generated map images.
- **Google Vision API**: Enables landmark detection on photos.
- **Google Maps API**: Retrieves static maps for detected landmarks.
- **Google Pub/Sub**: Receives messages with photo details for processing.

---

## Logging

The Landmarks App utilizes logging to provide information about the execution and processing of requests. Log messages
are written to the console and include relevant details and status updates.

---

## Error Handling

The Landmarks App includes error handling and logging for various scenarios, such as I/O errors, missing API keys, and
storage failures. Errors are logged and appropriate actions are taken to ensure the application's stability and
reliability.
