package pt.isel.cn.storage_operations;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.util.Scanner;

/**
 * Test of Google Storage Operations.
 */
public class TestStorageOperations {

    /**
     * Entry point for the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Service Accounts:
        // Use the environment variable GOOGLE_APPLICATION_CREDENTIALS
        // to point to the file name with the service account private key
        // Exemplo Windows: set GOOGLE_APPLICATION_CREDENTIALS=c:\keys\my-service-account.json
        // Exemplo Linux: export GOOGLE_APPLICATION_CREDENTIALS=/.../my-service-account.json

        StorageOptions storageOptions = StorageOptions.getDefaultInstance();
        Storage storage = storageOptions.getService();
        String projID = storageOptions.getProjectId();
        if (projID != null)
            System.out.println("Current Project ID:" + projID);
        else {
            System.out.println("The environment variable GOOGLE_APPLICATION_CREDENTIALS isn't well defined!!");
            System.exit(-1);
        }
        StorageOperations soper = new StorageOperations(storage);
        boolean end = false;
        while (!end) {
            try {
                int option = menu();
                switch (option) {
                    case 0:
                        soper.listBuckets(projID);
                        break;
                    case 1:
                        soper.createBucket();
                        break;
                    case 2:
                        soper.uploadBlobToBucket();
                        break;
                    case 3:
                        soper.downloadBlobFromBucket();
                        break;
                    case 4:
                        soper.deleteBucket();
                        break;
                    case 5:
                        soper.makeBlobPublic();
                        break;
                    default:
                        System.out.println("Invalid Option!");
                        break;
                    case 99:
                        end = true;
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Error executing operations!");
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
            System.out.println("######## MENU ##########");
            System.out.println("Options for Google Storage Operations:");
            System.out.println(" 0: List Buckets in Project");
            System.out.println(" 1: Create a new Bucket");
            System.out.println(" 2: Upload Blob to Bucket");
            System.out.println(" 3: Download Blob from Bucket");
            System.out.println(" 4: Delete a Bucket");
            System.out.println(" 5: Make Blob Public");
            System.out.println("..........");
            System.out.println("99: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 5) || option == 99));
        return option;
    }
}
