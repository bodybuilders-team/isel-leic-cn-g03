package pt.isel.cn.landmarks.instance_manager;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.compute.v1.InstanceGroupManagersClient;
import com.google.cloud.compute.v1.ListManagedInstancesInstanceGroupManagersRequest;
import com.google.cloud.compute.v1.ManagedInstance;
import com.google.cloud.compute.v1.Operation;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Instance Manager is responsible for managing the instances of the Landmark Recognition System.
 */
public class InstanceManager {

    private static final String PROJECT_ID = "cn2223-t1-g03";
    private static final String ZONE = "europe-southwest1-a";

    private static final String LANDMARK_APP_INSTANCE_GROUP_NAME = "instance-group-landmarks-app";
    private static final int LANDMARK_APP_INSTANCE_GROUP_MIN_SIZE = 0;
    private static final int LANDMARK_APP_INSTANCE_GROUP_MAX_SIZE = 2;

    private static final String GRPC_SERVER_INSTANCE_GROUP_NAME = "instance-group-landmarks-server";
    private static final int GRPC_SERVER_INSTANCE_GROUP_MIN_SIZE = 0; // 0 to disable the gRPC server
    private static final int GRPC_SERVER_INSTANCE_GROUP_MAX_SIZE = 3;

    private static InstanceGroupManagersClient managersClient;

    /**
     * Entry point of the instance manager.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        managersClient = InstanceGroupManagersClient.create();

        boolean end = false;
        while (!end) {
            int option = menu();
            switch (option) {
                case 0:
                    listManagedInstanceGroupVMs(GRPC_SERVER_INSTANCE_GROUP_NAME);
                    break;
                case 1:
                    resizeManagedInstanceGroup(
                            GRPC_SERVER_INSTANCE_GROUP_NAME,
                            getNewSize(GRPC_SERVER_INSTANCE_GROUP_MIN_SIZE, GRPC_SERVER_INSTANCE_GROUP_MAX_SIZE)
                    );
                    break;
                case 2:
                    listManagedInstanceGroupVMs(LANDMARK_APP_INSTANCE_GROUP_NAME);
                    break;
                case 3:
                    resizeManagedInstanceGroup(
                            LANDMARK_APP_INSTANCE_GROUP_NAME,
                            getNewSize(LANDMARK_APP_INSTANCE_GROUP_MIN_SIZE, LANDMARK_APP_INSTANCE_GROUP_MAX_SIZE)
                    );
                    break;
                case 4:
                    end = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }


    /**
     * Displays the menu and reads the user option.
     *
     * @return the user option
     */
    private static int menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println();
            System.out.println("########## Landmarks Instance Manager ##########");
            System.out.println("gRPC Server Instance Group:");
            System.out.println(" 0 - List gRPC Server VM instances");
            System.out.println(" 1 - Resize gRPC Server VM instances\n");
            System.out.println("Landmark App Instance Group:");
            System.out.println(" 2 - List Landmark App VM instances");
            System.out.println(" 3 - Resize Landmark App VM instances\n");
            System.out.println(" 4 - Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!(option >= 0 && option <= 4));
        return option;
    }

    /**
     * Lists the instances of a managed instance group.
     *
     * @param instanceGroupName the name of the managed instance group
     */
    static void listManagedInstanceGroupVMs(String instanceGroupName) {
        ListManagedInstancesInstanceGroupManagersRequest request = ListManagedInstancesInstanceGroupManagersRequest
                .newBuilder()
                .setInstanceGroupManager(instanceGroupName)
                .setProject(PROJECT_ID)
                .setReturnPartialSuccess(true)
                .setZone(ZONE)
                .build();

        if (!managersClient.listManagedInstances(request).iterateAll().iterator().hasNext()) {
            System.out.println("No instances found for instance group: " + instanceGroupName);
            return;
        }

        System.out.println("Instances of instance group: " + instanceGroupName);
        for (ManagedInstance instance : managersClient.listManagedInstances(request).iterateAll()) {
            System.out.println(" - " + instance.getInstance() + " with status = " + instance.getInstanceStatus());
        }
    }

    /**
     * Reads the new size of a managed instance group.
     *
     * @param minSize the minimum size of the managed instance group (inclusive)
     * @param maxSize the maximum size of the managed instance group (inclusive)
     * @return the new size of the managed instance group
     */
    static int getNewSize(int minSize, int maxSize) {
        Scanner scan = new Scanner(System.in);
        int newSize;
        do {
            System.out.print("Enter the new size of the instance group [" + minSize + "-" + maxSize + "]: ");
            newSize = scan.nextInt();
        } while (!(newSize >= minSize && newSize <= maxSize));

        return newSize;
    }

    /**
     * Resizes a managed instance group.
     *
     * @param instanceGroupName the name of the managed instance group
     * @param newSize           the new size of the managed instance group
     * @throws InterruptedException if the current thread was interrupted while waiting
     * @throws ExecutionException   if the computation threw an exception
     */
    static void resizeManagedInstanceGroup(String instanceGroupName, int newSize) throws InterruptedException, ExecutionException {
        System.out.println("Resizing instance group " + instanceGroupName + " to size " + newSize);
        OperationFuture<Operation, Operation> result = managersClient.resizeAsync(
                PROJECT_ID,
                ZONE,
                instanceGroupName,
                newSize
        );
        Operation oper = result.get();
        System.out.println("Resizing status: " + oper.getStatus());
    }
}
