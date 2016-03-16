package uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;

/**
 * Created by charliewaite on 05/03/2016.
 * Competition class responsible for storing competitions retrieved by Firebase or API
 */
public class Competition implements Serializable {

    private String id;
    private String name;
    private String region;
    private String fdId; //Football data ID for statistics from a second API if needed

    public Competition() {

    }

    public Competition(String id, String name, String region, String fdId) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.fdId = fdId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getFdId() {
        return fdId;
    }
}
