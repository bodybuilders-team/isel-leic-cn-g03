package pt.isel.cn.landmarks.server.service.dtos;

public class IdentifiedPhotoOutput {

    private final String photoName;
    private final String landmarkName;
    private final float confidence;

    public IdentifiedPhotoOutput(String photoName, String landmarkName, float confidence) {
        this.photoName = photoName;
        this.landmarkName = landmarkName;
        this.confidence = confidence;
    }

    public String getPhotoName() {
        return photoName;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public float getConfidence() {
        return confidence;
    }
}
