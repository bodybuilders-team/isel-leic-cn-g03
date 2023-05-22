package pt.isel.cn.landmarks.app;

/**
 * Configuration class for the app.
 */
public class Config {
    public static final String PROJECT_ID = "cn2223-t1-g03";
    public static final String SUBSCRIPTION_ID = "landmarks-sub";
    public static final String MAPS_BUCKET_NAME = "landmarks-maps";
    public static final String FIRESTORE_COLLECTION_NAME = "landmarks";
    public static final String GOOGLE_MAPS_API_KEY = System.getenv("GOOGLE_MAPS_API_KEY");
}
