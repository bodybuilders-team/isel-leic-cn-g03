package cn.sharedfotos;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.StorageOptions;

import java.util.Scanner;

public class SharedFotos {

    static StorageOperations soper;

    public SharedFotos() {
        // Empty constructor
    }

    public static void main(String[] args) throws Exception {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        // Setup Firestore
        FirestoreOptions options = FirestoreOptions
                .newBuilder().setCredentials(credentials).build();
        Firestore db = options.getService();

        // Setup Storage
        StorageOptions storageOptions = StorageOptions.getDefaultInstance();
        soper = new StorageOperations(storageOptions.getService());

        // Setup PubSub


        while (true) {
            try {
                int option = Menu();
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
        // TODO
    }

    private static void uploadPhoto() {
        // TODO
    }

    private static void publishMessage() {
        // TODO
    }

    static int Menu() {
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
