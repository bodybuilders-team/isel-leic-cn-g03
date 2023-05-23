# IP Lookup

> This file contains the IP Lookup Cloud Function documentation for the Landmarks Recognition System.
> The IP Lookup is a Cloud Function that provides the functionality to retrieve the IP addresses of instances belonging
> to a specific managed instance group. It is executed when an HTTP request is sent to the Cloud Function's endpoint,
> and
> the response is sent back to the client as the HTTP response.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Deployment](#deployment)
- [Usage](#usage)
- [Limitations](#limitations)

---

## Overview

The IP Lookup Cloud Function is designed to work with Google Cloud Platform (GCP) and utilizes the Google Cloud Compute
Engine API to retrieve the IP addresses of instances. It receives an HTTP request with the query parameter "
instance-group" specifying the name of the managed instance group. The function then retrieves the IP addresses
associated with the instances in that group and returns them as a comma-separated list in the HTTP response.

---

## Prerequisites

To use the IP Lookup Cloud Function, ensure the following prerequisites are met:

- A GCP project with Compute Engine enabled.
- The necessary permissions to deploy and invoke Cloud Functions.
- The Google Cloud SDK (gcloud) installed on your local machine.

---

## Deployment

Follow these steps to deploy the IP Lookup Cloud Function:

1. Clone the repository and navigate to the `ip_lookup` directory.
2. Open the `Entrypoint.java` file.
3. Set the values of `PROJECT_ID` and `ZONE` variables to your GCP project ID and desired zone, respectively.
4. Build the project using Maven:
   ```
   mvn clean package
   ```
5. Deploy the Cloud Function using a gcloud command similar to the following, replacing the parameters with the
   appropriate values to match your environment:
   ```
   gcloud functions deploy funcIPLookup --project=cn2223-t1-g03 --allow-unauthenticated --entry-point=pt.isel.cn.landmarks.ip_lookup.Entrypoint --runtime=java11 --trigger-http --region=europe-west1 --source=target/deployment --service-account=landmarks-instance-manager@cn2223-t1-g03.iam.gserviceaccount.com --max-instances=3
   ```

---

## Usage

To use the IP Lookup Cloud Function, send an HTTP GET request to the deployed endpoint with the query parameter "
instance-group" set to the name of the managed instance group for which you want to retrieve the IP addresses. The
response will contain a comma-separated list of IP addresses associated with the instances in the specified group.

Example usage:

```
GET https://REGION-PROJECT_ID.cloudfunctions.net/ip-lookup?instance-group=INSTANCE_GROUP_NAME
```

Replace the following placeholders:

- `REGION`: The region where the Cloud Function is deployed.
- `PROJECT_ID`: Your GCP project ID.
- `INSTANCE_GROUP_NAME`: The name of the managed instance group.

---

## Limitations

- The IP Lookup Cloud Function requires the necessary permissions to access the Google Cloud Compute Engine API. Make
  sure the service account associated with the Cloud Function has the appropriate roles and permissions.
- The Cloud Function assumes that the instances in the managed instance group have at least one network interface with
  an access configuration containing a public IP address (NAT IP).
- The Cloud Function returns the IP addresses as a comma-separated list. If there are no instances or no IP addresses
  found for the specified instance group, the response will be an empty string.
