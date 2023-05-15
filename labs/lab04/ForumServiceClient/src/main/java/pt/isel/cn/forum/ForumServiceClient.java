package pt.isel.cn.forum;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.protobuf.Empty;
import forum.ExistingTopics;
import forum.ForumGrpc;
import forum.ForumMessage;
import forum.SubscribeUnSubscribe;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

/**
 * Client for the forum service using gRPC.
 */
public class ForumServiceClient {

    private static final String SVC_IP = "34.76.2.185";
    private static final int SVC_PORT = 8000;

    static StorageOperations soper;
    private static ManagedChannel channel;
    private static ForumGrpc.ForumStub nonBlockingStub;
    private static ForumGrpc.ForumBlockingStub blockingStub;

    /**
     * Entry point for the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        channel = ManagedChannelBuilder.forAddress(SVC_IP, SVC_PORT)
                // Channels are secure by default (via SSL/TLS).
                // For the example we disable TLS to avoid needing certificates.
                .usePlaintext()
                .build();

        nonBlockingStub = ForumGrpc.newStub(channel);
        blockingStub = ForumGrpc.newBlockingStub(channel);

        StorageOptions storageOptions = StorageOptions.getDefaultInstance();
        Storage storage = storageOptions.getService();
        String projID = storageOptions.getProjectId();
        if (projID != null)
            System.out.println("Current Project ID:" + projID);
        else {
            System.out.println("The environment variable GOOGLE_APPLICATION_CREDENTIALS isn't well defined!!");
            System.exit(-1);
        }
        soper = new StorageOperations(storage);

        while (true) {
            try {
                int option = menu();
                switch (option) {
                    case 0:
                        topicSubscribe();
                        break;
                    case 1:
                        topicUnSubscribe();
                        break;
                    case 2:
                        getAllTopics();
                        break;
                    case 3:
                        uploadBlob();
                        break;
                    case 4:
                        publishMessage();
                        break;
                    case 99:
                        System.exit(0);
                }
            } catch (Exception ex) {
                System.out.println("Error on call:" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /**
     * Prints the menu.
     *
     * @return the option selected
     */
    static int menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("########## MENU ##########");
            System.out.println("Options for Forum Service:");
            System.out.println(" 0: Subscribe to a topic");
            System.out.println(" 1: Unsubscribe from a topic");
            System.out.println(" 2: Get all topics");
            System.out.println(" 3: Upload blob");
            System.out.println(" 4: Publish a message");
            System.out.println("..........");
            System.out.println("99: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 4) || option == 99));
        return option;
    }

    /**
     * Subscribes to a topic.
     */
    static void topicSubscribe() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your user name: ");
        String userName = scan.nextLine();
        System.out.print("Enter the topic name: ");
        String topicName = scan.nextLine();

        nonBlockingStub.topicSubscribe(
                SubscribeUnSubscribe.newBuilder()
                        .setUsrName(userName)
                        .setTopicName(topicName)
                        .build(),
                new MessageStreamObserver(soper)
        );
    }

    /**
     * Unsubscribes from a topic.
     */
    static void topicUnSubscribe() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your user name: ");
        String userName = scan.nextLine();
        System.out.print("Enter the topic name: ");
        String topicName = scan.nextLine();

        blockingStub.topicUnSubscribe(
                SubscribeUnSubscribe.newBuilder()
                        .setUsrName(userName)
                        .setTopicName(topicName)
                        .build()
        );
        System.out.println("Unsubscribed from topic " + topicName);
    }

    /**
     * Gets all the topics.
     */
    static void getAllTopics() {
        ExistingTopics topics = blockingStub.getAllTopics(
                Empty.newBuilder().build()
        );
        System.out.println("Existing topics: ");
        for (String topic : topics.getTopicNameList())
            System.out.println(topic);
    }

    /**
     * Uploads a blob to a bucket.
     */
    static void uploadBlob() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the name of the Bucket? ");
        String bucketName = scan.nextLine();
        System.out.println("Enter the name of the Blob? ");
        String blobName = scan.nextLine();
        System.out.println("Enter the pathname of the file to upload? ");
        String absFileName = scan.nextLine();

        try {
            soper.uploadBlobToBucket(bucketName, blobName, absFileName);
            soper.makeBlobPublic(bucketName, blobName);
        } catch (Exception e) {
            System.out.println("Error uploading blob to bucket: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Successfully uploaded blob " + blobName + " to bucket " + bucketName);
    }

    /**
     * Publishes a message on a topic.
     */
    static void publishMessage() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your user name: ");
        String userName = scan.nextLine();
        System.out.print("Enter the topic name: ");
        String topicName = scan.nextLine();
        System.out.print("Enter the text for the message: "); // [<texto>[;<bucketName>;<blobName>]
        String message = scan.nextLine();
        System.out.print("Notify about a blob upload? (Y/n): ");
        String notifyBlob = scan.nextLine();

        if (notifyBlob.equals("Y") || notifyBlob.equals("y")) {
            System.out.print("Is the blob already uploaded? (Y/n): ");
            String blobAlreadyUploaded = scan.nextLine();
            System.out.println("Enter the name of the Bucket? ");
            String bucketName = scan.nextLine();
            System.out.println("Enter the name of the Blob? ");
            String blobName = scan.nextLine();

            if (blobAlreadyUploaded.equals("Y") || blobAlreadyUploaded.equals("y")) {
                try {
                    if (!soper.checkBlobExists(bucketName, blobName)) {
                        System.out.println("Blob does not exist!");
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("Error checking blob existence: " + e.getMessage());
                    e.printStackTrace();
                    return;
                }
            } else {
                System.out.println("Enter the pathname of the file to upload? ");
                String absFileName = scan.nextLine();

                try {
                    soper.uploadBlobToBucket(bucketName, blobName, absFileName);
                    soper.makeBlobPublic(bucketName, blobName);
                } catch (Exception e) {
                    System.out.println("Error uploading blob to bucket: " + e.getMessage());
                    e.printStackTrace();
                    return;
                }
            }

            message += "[;" + bucketName + ";" + blobName + "]";
        }

        blockingStub.publishMessage(
                ForumMessage.newBuilder()
                        .setFromUser(userName)
                        .setTopicName(topicName)
                        .setTxtMsg(message)
                        .build()
        );
        System.out.println("Message published on topic " + topicName);
    }
}
