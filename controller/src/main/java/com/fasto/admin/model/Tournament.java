package com.fasto.admin.model;

/**
 * Tournament
 */
public class Tournament {

    private Long id;
    private String name;
    private Long templateId;
    private Long slateId;
    private Long payOutId;
    private String type;
    private String structure;
    private Integer duration;
    private Long startTime;
    private Long visibleInLobbyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getSlateId() {
        return slateId;
    }

    public void setSlateId(Long slateId) {
        this.slateId = slateId;
    }

    public Long getPayOutId() {
        return payOutId;
    }

    public void setPayOutId(Long payOutId) {
        this.payOutId = payOutId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getVisibleInLobbyTime() {
        return visibleInLobbyTime;
    }

    public void setVisibleInLobbyTime(Long visibleInLobbyTime) {
        this.visibleInLobbyTime = visibleInLobbyTime;
    }
}
