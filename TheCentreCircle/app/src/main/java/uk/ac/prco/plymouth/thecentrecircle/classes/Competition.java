package uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;

/**
 * Represents a competition provided by the external API
 * @author Charles Waite
 * @see java.io.Serializable
 **/
public class Competition implements Serializable {

    private String id;
    private String name;
    private String region;
    private String fdId; //Football data ID for statistics from a second API if needed
    private int coefficientPoints;

    /**
     * Empty constructor for Firebase
     */
    public Competition() {

    }

    /**
     * Constructor taking five parameters to build a competition manually if needed
     * @param id ID of the competition
     * @param name Name of the competition
     * @param region The country the competition is based in
     * @param fdId The ID for the competition on http://football-data.org - This was used before paid API access
     * @param coefficientPoints The number of Coefficient points the competition's region has
     */
    public Competition(String id, String name, String region, String fdId, int coefficientPoints) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.fdId = fdId;
        this.coefficientPoints = coefficientPoints;
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

    public int getCoefficientPoints() {
        return coefficientPoints;
    }
}
