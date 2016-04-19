package uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;

/**
 * Represents an event object which belongs to a match
 * @author Charles Waite
 */
public class Event implements Serializable {
    private String eventAssist;
    private String eventAssistId;
    private String eventExtraMin;
    private String eventId;
    private String eventMinute;
    private String eventPlayer;
    private String eventPlayerId;
    private String eventTeam;
    private String eventType;

    public Event(String eventAssist, String eventAssistId, String eventExtraMin,
                 String eventId, String eventMinute, String eventPlayer, String eventPlayerId,
                 String eventTeam, String eventType) {
        this.eventAssist = eventAssist;
        this.eventAssistId = eventAssistId;
        this.eventExtraMin = eventExtraMin;
        this.eventId = eventId;
        this.eventMinute = eventMinute;
        this.eventPlayer = eventPlayer;
        this.eventPlayerId = eventPlayerId;
        this.eventTeam = eventTeam;
        this.eventType = eventType;
    }

    public String getEventAssist() {
        return eventAssist;
    }

    public String getEventAssistId() {
        return eventAssistId;
    }

    public String getEventExtraMin() {
        return eventExtraMin;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventMinute() {
        return eventMinute;
    }

    public String getEventPlayer() {
        return eventPlayer;
    }

    public String getEventPlayerId() {
        return eventPlayerId;
    }

    public String getEventTeam() {
        return eventTeam;
    }

    public String getEventType() {
        return eventType;
    }


    //    private String eventId;
//    private String eventMatchId;
//    private String eventType;
//    private String eventMinute;
//    private String eventTeam;
//    private String eventPlayer;
//    private String eventPlayerId;
//    private String eventResult;
//
//    public Event() {
//
//    }
//
//    public Event(String eventId, String eventMatchId, String eventType, String eventMinute,
//                 String eventTeam, String eventPlayer, String eventPlayerId, String eventResult) {
//        this.eventId = eventId;
//        this.eventMatchId = eventMatchId;
//        this.eventType = eventType;
//        this.eventMinute = eventMinute;
//        this.eventTeam = eventTeam;
//        this.eventPlayer = eventPlayer;
//        this.eventPlayerId = eventPlayerId;
//        this.eventResult = eventResult;
//    }
//
//    public String getEventId() {
//        return eventId;
//    }
//
//    public void setEventId(String eventId) {
//        this.eventId = eventId;
//    }
//
//    public String getEventMatchId() {
//        return eventMatchId;
//    }
//
//    public void setEventMatchId(String eventMatchId) {
//        this.eventMatchId = eventMatchId;
//    }
//
//    public String getEventType() {
//        return eventType;
//    }
//
//    public void setEventType(String eventType) {
//        this.eventType = eventType;
//    }
//
//    public String getEventMinute() {
//        return eventMinute + "'";
//    }
//
//    public void setEventMinute(String eventMinute) {
//        this.eventMinute = eventMinute;
//    }
//
//    public String getEventTeam() {
//        return eventTeam;
//    }
//
//    public void setEventTeam(String eventTeam) {
//        this.eventTeam = eventTeam;
//    }
//
//    public String getEventPlayer() {
//        return eventPlayer;
//    }
//
//    public void setEventPlayer(String eventPlayer) {
//        this.eventPlayer = eventPlayer;
//    }
//
//    public String getEventPlayerId() {
//        return eventPlayerId;
//    }
//
//    public void setEventPlayerId(String eventPlayerId) {
//        this.eventPlayerId = eventPlayerId;
//    }
//
//    public String getEventResult() {
//        return eventResult;
//    }
//
//    public void setEventResult(String eventResult) {
//        this.eventResult = eventResult;
//    }
}