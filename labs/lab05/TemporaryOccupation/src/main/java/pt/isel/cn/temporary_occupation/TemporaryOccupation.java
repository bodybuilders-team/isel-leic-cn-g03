package pt.isel.cn.temporary_occupation;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.GeoPoint;
import com.google.cloud.firestore.WriteResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Temporary occupation.
 */
public class TemporaryOccupation {
    public int id;
    public Location location;
    public Event event;

    private static final String COLECTION_NAME = "ocupacao-temporaria";

    public TemporaryOccupation() {
        // Empty constructor
    }

    /**
     * Converts a line from the CSV file to an object.
     *
     * @param line Line from the CSV file
     * @throws ParseException If the line is not in the expected format
     */
    public TemporaryOccupation(String line) throws ParseException {
        String[] cols = line.split(",");
        this.id = Integer.parseInt(cols[0]);
        this.location = new Location();
        this.location.point = new GeoPoint(Double.parseDouble(cols[1]), Double.parseDouble(cols[2]));
        this.location.coord = new Coordinates();
        this.location.coord.x = Double.parseDouble(cols[1]);
        this.location.coord.y = Double.parseDouble(cols[2]);
        this.location.parish = cols[3];
        this.location.local = cols[4];
        this.event = new Event();
        this.event.eventId = Integer.parseInt(cols[5]);
        this.event.name = cols[6];
        this.event.type = cols[7];
        this.event.details = new HashMap<>();
        if (!cols[8].isEmpty())
            this.event.details.put("Participantes", cols[8]);
        if (!cols[9].isEmpty())
            this.event.details.put("Custo", cols[9]);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.event.startDate = formatter.parse(cols[10]);
        this.event.endDate = formatter.parse(cols[11]);
        this.event.licensing = new Licensing();
        this.event.licensing.code = cols[12];
        this.event.licensing.date = formatter.parse(cols[13]);
    }

    /**
     * Entry point for the application.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) throws Exception {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        FirestoreOptions options = FirestoreOptions.newBuilder().setCredentials(credentials).build();
        Firestore db = options.getService();

        boolean end = false;
        while (!end) {
            try {
                int option = menu();
                switch (option) {
                    case 0:
                        insertDocuments("OcupacaoEspacosPublicos.csv", db, COLECTION_NAME);
                        break;
                    case 1:
                        getDocument(COLECTION_NAME, "Lab52014", db);
                        break;
                    case 2:
                        deleteField(COLECTION_NAME, "Lab52014", "location", db);
                        break;
                    case 3:
                        queryByParish(COLECTION_NAME, "Belém", db);
                        break;
                    case 4:
                        queryBy(COLECTION_NAME, 2014, "Belém", "Desportivo", db);
                        break;
                    case 5:
                        queryByStartDate(
                                COLECTION_NAME,
                                Timestamp.of(new Date(2017, 1, 31)),
                                Timestamp.of(new Date(2017, 3, 1)),
                                db
                        );
                        break;
                    case 6:
                        queryByDate(
                                COLECTION_NAME,
                                Timestamp.of(new Date(2017, 1, 31)),
                                Timestamp.of(new Date(2017, 3, 1)),
                                db
                        );
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
     * Insert documents from a CSV file.
     *
     * @param pathnameCSV    Pathname of the CSV file
     * @param db             Firestore instance
     * @param collectionName Name of the collection
     * @throws IOException          if an I/O error occurs.
     * @throws ParseException       if the line is not in the expected format
     * @throws ExecutionException   if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    public static void insertDocuments(
            String pathnameCSV,
            Firestore db,
            String collectionName
    ) throws IOException, ParseException, ExecutionException, InterruptedException {
        BufferedReader reader = new BufferedReader(new FileReader(pathnameCSV));
        CollectionReference colRef = db.collection(collectionName);
        String line;
        while ((line = reader.readLine()) != null) {
            TemporaryOccupation ocup = new TemporaryOccupation(line);
            DocumentReference docRef = colRef.document("Lab5" + ocup.id);
            ApiFuture<WriteResult> resultFut = docRef.set(ocup);
            WriteResult result = resultFut.get();
            System.out.println("Update time : " + result.getUpdateTime());
        }
    }

    /**
     * Present the content of a document from its identifier.
     *
     * @param collectionName the name of the collection
     * @param documentName   the name of the document
     * @param db             Firestore instance
     * @throws ExecutionException   if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    public static void getDocument(
            String collectionName,
            String documentName,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collectionName).document(documentName);
        ApiFuture<com.google.cloud.firestore.DocumentSnapshot> future = docRef.get();
        System.out.println("Query time : " + future.get().getReadTime());
        System.out.println(future.get().getData());
    }

    /**
     * Delete a field from a document.
     *
     * @param collectionName the name of the collection
     * @param documentName   the name of the document
     * @param fieldName      the name of the field
     * @param db             Firestore instance
     * @throws ExecutionException   if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    public static void deleteField(
            String collectionName,
            String documentName,
            String fieldName,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collectionName).document(documentName);
        ApiFuture<WriteResult> update = docRef.update(fieldName, FieldValue.delete());
        System.out.println("Update time : " + update.get().getUpdateTime());
    }

    /**
     * Query all documents from a parish.
     *
     * @param collectionName the name of the collection
     * @param parish         the name of the parish
     * @param db             Firestore instance
     * @throws ExecutionException   if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    public static void queryByParish(
            String collectionName,
            String parish,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        CollectionReference colRef = db.collection(collectionName);
        ApiFuture<com.google.cloud.firestore.QuerySnapshot> query = colRef.whereEqualTo("location.parish", parish).get();
        System.out.println("Query time : " + query.get().getReadTime());
        query.get().getDocuments().forEach(doc -> System.out.println(doc.getId()));
    }

    /**
     * Query all documents with the following criteria:
     * - id greater than a value
     * - From a parish
     * - From a type of event
     *
     * @param collectionName the name of the collection
     * @param id             the id
     * @param parish         the parish
     * @param type           the type of event
     * @param db             Firestore instance
     * @throws ExecutionException   if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    public static void queryBy(
            String collectionName,
            int id,
            String parish,
            String type,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        CollectionReference colRef = db.collection(collectionName);
        ApiFuture<com.google.cloud.firestore.QuerySnapshot> query = colRef
                .whereGreaterThan("id", id)
                .whereEqualTo("location.parish", parish)
                .whereEqualTo("event.type", type)
                .get();
        System.out.println("Query time : " + query.get().getReadTime());
        query.get().getDocuments().forEach(doc -> System.out.println(doc.getId()));
    }

    /**
     * Query all documents from a collection that start between two dates.
     *
     * @param collectionName the name of the collection
     * @param startDate      the start date
     * @param endDate        the end date
     * @param db             Firestore instance
     * @throws ExecutionException   if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    public static void queryByStartDate(
            String collectionName,
            Timestamp startDate,
            Timestamp endDate,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        CollectionReference colRef = db.collection(collectionName);
        ApiFuture<com.google.cloud.firestore.QuerySnapshot> query = colRef
                .whereGreaterThan("event.startDate", startDate)
                .whereLessThan("event.startDate", endDate)
                .get();
        System.out.println("Query time : " + query.get().getReadTime());
        query.get().getDocuments().forEach(doc -> System.out.println(doc.getId()));
    }

    /**
     * Query all documents from a collection with a date between two values.
     *
     * @param collectionName the name of the collection
     * @param db             Firestore instance
     * @param startDate      the start date
     * @param endDate        the end date
     * @throws ExecutionException   if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    public static void queryByDate(
            String collectionName,
            Timestamp startDate,
            Timestamp endDate,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        CollectionReference colRef = db.collection(collectionName);
        ApiFuture<com.google.cloud.firestore.QuerySnapshot> query = colRef
                .whereGreaterThan("event.startDate", startDate)
                .get();
        query.get().getDocuments().stream()
                .filter(doc -> doc.getTimestamp("event.endDate").compareTo(endDate) < 0)
                .forEach(doc -> System.out.println(doc.getId()));

        System.out.println("Query time : " + query.get().getReadTime());
    }

    /**
     * Prints the menu.
     *
     * @return the option selected
     */
    public static int menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("######## MENU ##########");
            System.out.println("Options for Google Storage Operations:");
            System.out.println(" 0: Insert a document");
            System.out.println(" 1: Update a document");
            System.out.println(" 2: Delete a field");
            System.out.println(" 3: Query by parish");
            System.out.println(" 4: Query by id, parish and type");
            System.out.println(" 5: Query by start date");
            System.out.println(" 6: Query by date");
            System.out.println("..........");
            System.out.println("99: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 5) || option == 99));
        return option;
    }
}