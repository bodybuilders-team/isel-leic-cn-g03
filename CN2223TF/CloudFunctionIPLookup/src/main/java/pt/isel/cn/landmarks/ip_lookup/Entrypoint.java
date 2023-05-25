package pt.isel.cn.landmarks.ip_lookup;


import com.google.cloud.compute.v1.InstancesClient;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.stream.StreamSupport;

/**
 * Entrypoint of the Cloud Function.
 * <p>
 * It is executed when an HTTP request is sent to the Cloud Function's
 * endpoint. The response is sent back to the client as the HTTP response.
 */
public class Entrypoint implements HttpFunction {

    private static final String PROJECT_ID = "cn2223-t1-g03";
    private static final String ZONE = "europe-southwest1-a";

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        String instanceGroupName = request.getFirstQueryParameter("instance-group").orElseThrow();

        String[] ips = getIpInstancesFromGroup(instanceGroupName);

        BufferedWriter writer = response.getWriter();
        writer.write(String.join(",", ips)); // IPs separated by commas
        writer.close();
    }

    /**
     * Gets the list of ip addresses of the instances of a managed instance group.
     *
     * @param instanceGroupName the name of the managed instance group
     * @return the list of ip addresses
     * @throws IOException if the client fails to close
     */
    static String[] getIpInstancesFromGroup(String instanceGroupName) throws IOException {
        System.out.println("Instances of instance group: " + instanceGroupName);
        try (InstancesClient client = InstancesClient.create()) {
            return StreamSupport.stream(client.list(PROJECT_ID, ZONE).iterateAll().spliterator(), false)
                    .filter(instance -> instance.getName().contains(instanceGroupName))
                    .map(instance -> instance.getNetworkInterfaces(0).getAccessConfigs(0).getNatIP())
                    .toArray(String[]::new);
        }
    }
}
