package pt.isel.cn.landmarks.storage.data;

public interface DataStorage {
    String getImageLocation(String bucketName, String blobName);
}
