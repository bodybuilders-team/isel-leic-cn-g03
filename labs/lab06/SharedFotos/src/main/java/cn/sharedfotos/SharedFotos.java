package cn.sharedfotos;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.storage.StorageOptions;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Subscription;
import com.google.pubsub.v1.SubscriptionName;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class SharedFotos {

    private static final String COLLECTION_ID = "sharefotos";
    private static final String TOPIC_ID = "sharefotos";
    private static final String PROJECT_ID = "cn2223-t1-g03";
    private static final String downloadedPhotosDestination = "downloadedPhotos/";

    private static String username;
    static StorageOperations soper;
    private static MySubscriber mySubscriber;
    static Firestore db;

    public SharedFotos() {
        // Empty constructor
    }

    public static void main(String[] args) throws Exception {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        // Setup Firestore
        FirestoreOptions options = FirestoreOptions.newBuilder().setCredentials(credentials).build();
        db = options.getService();

        // Setup Storage
        StorageOptions storageOptions = StorageOptions.getDefaultInstance();
        soper = new StorageOperations(storageOptions.getService());

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your username: ");
        username = scan.nextLine();

        while (true) {
            try {
                int option = menu();
                switch (option) {
                    case 0:
                        subscribeTopic();
                        break;
                    case 1:
                        uploadPhoto();
                        break;
                    case 2:
                        publishMessage();
                        break;
                    case 3:
                        unsubscribeTopic();
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

    static class MySubscriber {
        Subscription subscription;
        Subscriber subscriber;

        private static class MessageReceiveHandler implements MessageReceiver {
            @Override
            public void receiveMessage(PubsubMessage msg, AckReplyConsumer ackReply) {
                String strMsg = msg.getData().toStringUtf8();
                String text = strMsg.split(";")[0];
                String bucketName = strMsg.split(";")[1];
                String blobName = strMsg.split(";")[2];

                System.out.println("Text: " + text);

                // Download photo
                try {
                    soper.downloadBlobFromBucket(bucketName, blobName,
                            downloadedPhotosDestination + blobName);
                } catch (Exception e) {
                    System.out.println("Error downloading blob from bucket: " + e.getMessage());
                }

                // Update Firestore
                CollectionReference collection = db.collection(COLLECTION_ID);
                String docId = msg.getAttributesOrThrow("firestoreDocumentID");
                DocumentReference docRef = collection.document(docId);
                try {
                    Map<String, Object> data = docRef.get().get().getData();
                    data.put("DateReceivedBy-" + username, LocalDateTime.now().toString());
                    ApiFuture<WriteResult> result = docRef.update(data);
                    System.out.println("Update time : " + result.get().getUpdateTime());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

                ackReply.ack();
            }
        }

        public MySubscriber(Subscription subscription) {
            this.subscription = subscription;

            ExecutorProvider executorProvider = InstantiatingExecutorProvider
                    .newBuilder()
                    .setExecutorThreadCount(1) // um só thread no handler
                    .build();

            this.subscriber = Subscriber
                    .newBuilder(subscription.getName(), new MessageReceiveHandler())
                    .setExecutorProvider(executorProvider)
                    .build();

            this.subscriber.startAsync().awaitRunning();
        }

        public void endSubscription() throws IOException {
            this.subscriber.stopAsync().awaitTerminated();
            SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create();
            subscriptionAdminClient.deleteSubscription(subscription.getName());
            subscriptionAdminClient.close();
        }
    }

    private static void subscribeTopic() throws IOException {
        if (mySubscriber != null) {
            System.out.println("Already subscribed to the topic");
            return;
        }

        TopicName topicName = TopicName.ofProjectTopicName(PROJECT_ID, TOPIC_ID);
        SubscriptionName subscriptionName = SubscriptionName.of(PROJECT_ID, username);
        SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create();
        PushConfig pconfig = PushConfig.getDefaultInstance();

        //PushConfig.newBuilder().setPushEndpoint(ConsumerURL).build();
        Subscription subscription = subscriptionAdminClient.createSubscription(
                subscriptionName, topicName, pconfig, 0
        );

        subscriptionAdminClient.close();
        mySubscriber = new MySubscriber(subscription);
        System.out.println("Subscribed to the topic");
    }

    private static void unsubscribeTopic() throws IOException {
        mySubscriber.endSubscription();
        mySubscriber = null;
    }

    private static void uploadPhoto() {
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

    // Message: <texto da mensagem>;<nome de bucket com fotos>;<nome do blob da foto>;
    private static void publishMessage() throws ExecutionException, InterruptedException, IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the message to publish? ");
        String msgTxt = scan.nextLine();
        System.out.println("Enter the name of the bucket with photos? ");
        String bucketName = scan.nextLine();
        System.out.println("Enter the name of the blob with photo? ");
        String blobName = scan.nextLine();

        CollectionReference collection = db.collection(COLLECTION_ID);
        DocumentReference docRef = collection.document(blobName);
        HashMap<String, String> data = new HashMap<>();
        data.put("publisher", username);
        data.put("datePublished", java.time.LocalDateTime.now().toString());

        try {
            WriteResult result = docRef.set(data).get();
            System.out.println("Document written with ID: " + result.getUpdateTime());
        } catch (Exception e) {
            System.out.println("Error writing document to Firestore: " + e.getMessage());
            e.printStackTrace();
        }

        TopicName tName = TopicName.ofProjectTopicName(PROJECT_ID, TOPIC_ID);
        Publisher publisher = Publisher.newBuilder(tName).build();
        // Por cada mensagem
        ByteString msgData = ByteString.copyFromUtf8(msgTxt + ";" + bucketName + ";" + blobName);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(msgData)
                .putAttributes("publisher", username)
                .putAttributes("firestoreDocumentID", docRef.getId())
                .build();
        ApiFuture<String> future = publisher.publish(pubsubMessage);
        String msgID = future.get();
        System.out.println("Message Published with ID=" + msgID);
        // No fim de enviar as mensagens
        publisher.shutdown();
    }

    static int menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("########## MENU ##########");
            System.out.println("Options for SharedFotos App:");
            System.out.println(" 0: Subscribe to sharefotos topic");
            System.out.println(" 1: Upload a photo");
            System.out.println(" 2: Publish a message");
            System.out.println(" 3: Unsubscribe from sharefotos topic");
            System.out.println("..........");
            System.out.println("99: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 3) || option == 99));
        return option;
    }
}
