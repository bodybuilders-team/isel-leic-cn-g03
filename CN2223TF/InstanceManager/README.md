# Instance Manager

> This file contains the Instance Manager documentation for the Landmarks Recognition System.
> Instance Manager is responsible for managing the instances of the Landmark Recognition System.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Installation and Usage](#installation-and-usage)

---

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Google Cloud Platform (GCP) project with Compute Engine enabled

---

## Setup

To use the Instance Manager, follow these steps:

1. Clone the repository and navigate to the `InstanceManager` directory.

---

## Installation and Usage

The Instance Manager provides a menu-based interface to interact with the system. The following options are available:

1. **List gRPC Server VM instances**: Lists the instances in the gRPC Server instance group.
2. **Resize gRPC Server VM instances**: Resizes the gRPC Server instance group by specifying a new size.
3. **List Landmark App VM instances**: Lists the instances in the Landmark App instance group.
4. **Resize Landmark App VM instances**: Resizes the Landmark App instance group by specifying a new size.
5. **Exit**: Exits the Instance Manager.

Before running the Instance Manager, make sure to configure the following settings in the `InstanceManager` class:

- `PROJECT_ID`: The ID of your GCP project.
- `ZONE`: The zone in which the instances are located.
- `LANDMARK_APP_INSTANCE_GROUP_NAME`: The name of the Landmark App instance group.
- `LANDMARK_APP_INSTANCE_GROUP_MIN_SIZE`: The minimum size of the Landmark App instance group.
- `LANDMARK_APP_INSTANCE_GROUP_MAX_SIZE`: The maximum size of the Landmark App instance group.
- `GRPC_SERVER_INSTANCE_GROUP_NAME`: The name of the gRPC Server instance group.
- `GRPC_SERVER_INSTANCE_GROUP_MIN_SIZE`: The minimum size of the gRPC Server instance group.
- `GRPC_SERVER_INSTANCE_GROUP_MAX_SIZE`: The maximum size of the gRPC Server instance group.