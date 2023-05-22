# Landmarks gRPC API

> This file contains the technical documentation for the gRPC API used to interact with the Landmarks service. The
> Landmarks service allows users to submit photos and retrieve information about identified landmarks.

## Table of Contents

- [API Specifications](#api-specifications)
    - [LandmarksService](#landmarksservice)
        - [Message Definitions](#message-definitions)
- [Installation and Usage](#installation-and-usage)

---

## API Specifications

The gRPC API is defined using the Protocol Buffers language (proto3) and consists of the following service and message
definitions:

### LandmarksService

The `LandmarksService` service provides three RPC methods:

1. `submitPhoto`: Streams a photo to be submitted for landmark identification.
2. `getResults`: Retrieves the identification results for a previously submitted photo.
3. `getIdentifiedPhotos`: Retrieves a list of identified photos based on a confidence threshold.

### Message Definitions

The API defines the following messages:

1. `SubmitPhotoRequest`: Contains the name and binary data of a photo to be submitted.

    - `photo_name` (string): The name of the photo.
    - `photo` (bytes): The binary data of the photo.

2. `SubmitPhotoResponse`: Contains the request ID for the submitted photo.

    - `request_id` (string): The ID assigned to the submitted photo.

3. `GetResultsRequest`: Contains the request ID of a submitted photo to retrieve the identification results.

    - `request_id` (string): The ID of the submitted photo.

4. `GetResultsResponse`: Contains the identified landmarks and an image map.

    - `landmarks` (repeated Landmark): A list of identified landmarks.
    - `map` (ImageMap): An image map associated with the identification results.

5. `Landmark`: Represents an identified landmark.

    - `name` (string): The name of the landmark.
    - `latitude` (double): The latitude coordinate of the landmark.
    - `longitude` (double): The longitude coordinate of the landmark.
    - `confidence` (float): The confidence score of the identification.

6. `ImageMap`: Contains binary image data.

    - `image_data` (bytes): The binary data of the image map.

7. `GetIdentifiedPhotosRequest`: Contains a confidence threshold to retrieve identified photos.

    - `confidence_threshold` (float): The confidence threshold for photo identification.

8. `GetIdentifiedPhotosResponse`: Contains a list of identified photos.

    - `identified_photos` (repeated IdentifiedPhoto): A list of identified photos.

9. `IdentifiedPhoto`: Represents an identified photo.

    - `photo_name` (string): The name of the identified photo.
    - `landmark_name` (string): The name of the identified landmark.
    - `confidence` (float): The confidence score of the identification.

---

## Installation and Usage

To use the gRPC API for interacting with the Landmarks service, follow the below steps:

1. Clone this repository to your local machine.

2. Install the necessary dependencies and libraries for your programming language.

3. Generate the gRPC client code from the provided proto file.

4. Connect to the Landmarks service using the generated client code.

5. Utilize the RPC methods to submit photos, retrieve results, and get identified photos.

Refer to the documentation of your chosen programming language's gRPC implementation for specific instructions on
generating client code and making RPC calls.

If you want to install this gRPC API as a package, using Maven, you can run the following commands:

 ```bash
 mvn clean package install
 ```

Remember that you need to have Maven installed in your machine. If you don't have it, you can use an IDE like IntelliJ
IDEA or Eclipse, which have Maven integrated.