package pt.isel.cn.landmarks.server.service.dtos;

import pt.isel.cn.landmarks.server.domain.LandmarkMetadata;

import java.util.List;

public class GetResultsOutput {

    private final List<LandmarkMetadata> landmarkMetadataList;
    private final byte[] mapImage;

    public GetResultsOutput(List<LandmarkMetadata> landmarkMetadataList, byte[] mapImage) {
        this.landmarkMetadataList = landmarkMetadataList;
        this.mapImage = mapImage;
    }

    public List<LandmarkMetadata> getLandmarkMetadataList() {
        return landmarkMetadataList;
    }

    public byte[] getMapImage() {
        return mapImage;
    }
}
