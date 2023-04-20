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

    private static final String svcIP = "34.76.2.185";
    private static final int svcPort = 8000;
    static StorageOperations soper;
    private static ManagedChannel channel;
    private static ForumGrpc.ForumStub nonBlockingStub;
    private static ForumGrpc.ForumBlockingStub blockingStub;

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

    static void topicUnSubscribe() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your user name: ");
        String userName = scan.nextLine();
        System.out.print("Enter the topic name: ");
        String topicName = scan.nextLine();

        Empty res = blockingStub.topicUnSubscribe(
                SubscribeUnSubscribe.newBuilder()
                        .setUsrName(userName)
                        .setTopicName(topicName)
                        .build()
        );
        System.out.println("Unsubscribed from topic " + topicName);
    }

    static void getAllTopics() {
        ExistingTopics topics = blockingStub.getAllTopics(
                Empty.newBuilder().build()
        );
        System.out.println("Existing topics: ");
        for (String topic : topics.getTopicNameList())
            System.out.println(topic);
    }

    static void publishMessage() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your user name: ");
        String userName = scan.nextLine();
        System.out.print("Enter the topic name: ");
        String topicName = scan.nextLine();
        System.out.print("Enter the message [<texto>[;<bucketName>;<blobName>]]: ");
        String message = scan.nextLine();

        Empty res = blockingStub.publishMessage(
                ForumMessage.newBuilder()
                        .setFromUser(userName)
                        .setTopicName(topicName)
                        .setTxtMsg(message)
                        .build()
        );
        System.out.println("Message published on topic " + topicName);
    }

    static int Menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("########## MENU ##########");
            System.out.println("Options for Forum Service:");
            System.out.println(" 0: Subscribe to a topic");
            System.out.println(" 1: Unsubscribe from a topic");
            System.out.println(" 2: Get all topics");
            System.out.println(" 3: Publish a message");
            System.out.println("..........");
            System.out.println("99: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 3) || option == 99));
        return option;
    }

    public static void main(String[] args) {
        channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
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
                int option = Menu();
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
}
