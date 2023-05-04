package cn.operations;

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
import java.util.concurrent.ExecutionException;

public class OcupacaoTemporaria {
    public int ID;
    public Localizacao location;
    public Evento event;

    public OcupacaoTemporaria() {

    }

    public static void main(String[] args) throws Exception {
        // Com variável GOOGLE_APPLICATION_CREDENTIALS
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        // Alternativa não disponível em todas as API
        //InputStream serviceAccount = new FileInputStream(KEY_JSON);
        //GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirestoreOptions options = FirestoreOptions
                .newBuilder().setCredentials(credentials).build();

        Firestore db = options.getService();

        //insertDocuments("OcupacaoEspacosPublicos.csv", db, "ocupacao-temporaria");
        //getDocument("ocupacao-temporaria", "Lab52014", db);
        //deleteField("ocupacao-temporaria", "Lab52014", "location", db);
        //queryFreguesia("ocupacao-temporaria", "Belém", db);
        //queryComposta("ocupacao-temporaria", 2013, "Belém", "Desportivo", db);
        //queryData("ocupacao-temporaria", db);
        queryData2("ocupacao-temporaria", db);
    }

    public static OcupacaoTemporaria convertLineToObject(String line) throws ParseException {
        String[] cols = line.split(",");
        OcupacaoTemporaria ocup = new OcupacaoTemporaria();
        ocup.ID = Integer.parseInt(cols[0]);
        ocup.location = new Localizacao();
        ocup.location.point = new GeoPoint(Double.parseDouble(cols[1]), Double.parseDouble(cols[2]));
        ocup.location.coord = new Coordenadas();
        ocup.location.coord.X = Double.parseDouble(cols[1]);
        ocup.location.coord.Y = Double.parseDouble(cols[2]);
        ocup.location.freguesia = cols[3];
        ocup.location.local = cols[4];
        ocup.event = new Evento();
        ocup.event.evtID = Integer.parseInt(cols[5]);
        ocup.event.nome = cols[6];
        ocup.event.tipo = cols[7];
        ocup.event.details = new HashMap<>();
        if (!cols[8].isEmpty())
            ocup.event.details.put("Participantes", cols[8]);
        if (!cols[9].isEmpty())
            ocup.event.details.put("Custo", cols[9]);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        ocup.event.dtInicio = formatter.parse(cols[10]);
        ocup.event.dtFinal = formatter.parse(cols[11]);
        ocup.event.licenciamento = new Licenciamento();
        ocup.event.licenciamento.code = cols[12];
        ocup.event.licenciamento.dtLicenc = formatter.parse(cols[13]);
        return ocup;
    }

    public static void insertDocuments(
            String pathnameCSV,
            Firestore db,
            String collectionName
    ) throws IOException, ParseException, ExecutionException, InterruptedException {
        BufferedReader reader = new BufferedReader(new FileReader(pathnameCSV));
        CollectionReference colRef = db.collection(collectionName);
        String line;
        while ((line = reader.readLine()) != null) {
            OcupacaoTemporaria ocup = convertLineToObject(line);
            DocumentReference docRef = colRef.document("Lab5" + ocup.ID);
            ApiFuture<WriteResult> resultFut = docRef.set(ocup);
            WriteResult result = resultFut.get();
            System.out.println("Update time : " + result.getUpdateTime());
        }
    }

    // Apresentar o conteúdo de um documento a partir do seu identificador (ex: Lab52017).
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

    // Apagar um campo de um documento, dado o seu identificador e o nome do campo a
    //eliminar.
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

    // Realizar uma interrogação simples para obter todos os documentos de uma determinada
    //freguesia.
    public static void queryFreguesia(
            String collectionName,
            String freguesia,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        CollectionReference colRef = db.collection(collectionName);
        ApiFuture<com.google.cloud.firestore.QuerySnapshot> query = colRef.whereEqualTo("location.freguesia", freguesia).get();
        System.out.println("Query time : " + query.get().getReadTime());
        query.get().getDocuments().forEach(doc -> System.out.println(doc.getId()));
    }

    // Realizar uma interrogação composta para obter os documentos com os seguintes critérios:
    //■ Com o campo ID maior que um valor
    //■ De uma determinada freguesia
    //■ De um determinado tipo de evento
    public static void queryComposta(
            String collectionName,
            int ID,
            String freguesia,
            String tipo,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        CollectionReference colRef = db.collection(collectionName);
        ApiFuture<com.google.cloud.firestore.QuerySnapshot> query = colRef
                .whereGreaterThan("ID", ID)
                .whereEqualTo("location.freguesia", freguesia)
                .whereEqualTo("event.tipo", tipo)
                .get();
        System.out.println("Query time : " + query.get().getReadTime());
        query.get().getDocuments().forEach(doc -> System.out.println(doc.getId()));
    }

    // Realizar uma interrogação para obter os documentos com eventos que iniciaram no mês
    //de fevereiro de 2017 (data de início (dtInicio) maior que 31/01/2017 e menor que
    //01/03/2017). Dates are timestamps
    public static void queryData(
            String collectionName,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        CollectionReference colRef = db.collection(collectionName);
        ApiFuture<com.google.cloud.firestore.QuerySnapshot> query = colRef
                .whereGreaterThan("event.dtInicio", Timestamp.of(new Date(2017, 1, 31)))
                .whereLessThan("event.dtInicio", Timestamp.of(new Date(2017, 3, 1)))
                .get();
        System.out.println("Query time : " + query.get().getReadTime());
        query.get().getDocuments().forEach(doc -> System.out.println(doc.getId()));
    }

    //f. Realizar uma interrogação para obter os documentos com eventos integralmente
    //realizados no mês de fevereiro de 2017 (data de início (dtInicio) maior que 31/01/2017
    //e data final (dtFinal) menor que 01/03/2017).
    public static void queryData2(
            String collectionName,
            Firestore db
    ) throws ExecutionException, InterruptedException {
        CollectionReference colRef = db.collection(collectionName);
        ApiFuture<com.google.cloud.firestore.QuerySnapshot> query = colRef
                .whereGreaterThan("event.dtInicio", Timestamp.of(new Date(2017, 1, 31)))
                .get();
        query.get().getDocuments().stream()
                .filter(doc -> doc.getTimestamp("event.dtFinal").compareTo(Timestamp.of(new Date(2017, 3, 1))) < 0)
                .forEach(doc -> System.out.println(doc.getId()));

        System.out.println("Query time : " + query.get().getReadTime());
    }
}
