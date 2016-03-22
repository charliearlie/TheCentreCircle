package uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;

/**
 * Created by charliewaite on 15/03/2016.
 */
public class Event implements Serializable {
    private String eventId;
    private String eventMatchId;
    private String eventType;
    private String eventMinute;
    private String eventTeam;
    private String eventPlayer;
    private String eventPlayerId;
    private String eventResult;

    public Event() {

    }

    public Event(String eventId, String eventMatchId, String eventType, String eventMinute,
                 String eventTeam, String eventPlayer, String eventPlayerId, String eventResult) {
        this.eventId = eventId;
        this.eventMatchId = eventMatchId;
        this.eventType = eventType;
        this.eventMinute = eventMinute;
        this.eventTeam = eventTeam;
        this.eventPlayer = eventPlayer;
        this.eventPlayerId = eventPlayerId;
        this.eventResult = eventResult;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventMatchId() {
        return eventMatchId;
    }

    public void setEventMatchId(String eventMatchId) {
        this.eventMatchId = eventMatchId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventMinute() {
        return eventMinute;
    }

    public void setEventMinute(String eventMinute) {
        this.eventMinute = eventMinute;
    }

    public String getEventTeam() {
        return eventTeam;
    }

    public void setEventTeam(String eventTeam) {
        this.eventTeam = eventTeam;
    }

    public String getEventPlayer() {
        return eventPlayer;
    }

    public void setEventPlayer(String eventPlayer) {
        this.eventPlayer = eventPlayer;
    }

    public String getEventPlayerId() {
        return eventPlayerId;
    }

    public void setEventPlayerId(String eventPlayerId) {
        this.eventPlayerId = eventPlayerId;
    }

    public String getEventResult() {
        return eventResult;
    }

    public void setEventResult(String eventResult) {
        this.eventResult = eventResult;
    }
}