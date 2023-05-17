package pt.isel.cn.landmarks.storage.data;

public class CloudStorageService implements DataStorage {

    @Override
    public String getImageLocation(String bucketName, String blobName) {
        return "gs://" + bucketName + "/" + blobName;
    }
}
