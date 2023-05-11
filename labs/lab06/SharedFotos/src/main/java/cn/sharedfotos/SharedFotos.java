package cn.sharedfotos;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.storage.StorageOptions;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class SharedFotos {

    static final String PROJECT_ID = "sharedfotos-1";
    static final String TOPIC_ID = "sharefotos";
    static final String COLLECTION_ID = "sharefotos";
    static String username;
    static StorageOperations soper;
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

        // Setup PubSub

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
                    case 99:
                        System.exit(0);
                }
            } catch (Exception ex) {
                System.out.println("Error on call:" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private static void subscribeTopic() {

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
        data.put("datePublished", java.time.LocalDate.now().toString());

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
            System.out.println("..........");
            System.out.println("99: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 2) || option == 99));
        return option;
    }
}
