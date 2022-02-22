package nz.ac.wgtn.ecs.mapbox.models;

import java.util.List;

public class Data {
    String type;
    List<Features> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Features> getFeatures() {
        return features;
    }

    public void setFeatures(List<Features> features) {
        this.features = features;
    }
}
