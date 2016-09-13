package com.payler.paylergateapi.lib.model.request;

public class ActivateTemplateRequest extends GateRequest {
    private String recurrentTemplateId;

    private boolean active;

    private String key;

    public ActivateTemplateRequest setRecurrentTemplateId(String recurrentTemplateId) {
        this.recurrentTemplateId = recurrentTemplateId;
        return this;
    }

    public ActivateTemplateRequest setActive(boolean active) {
        this.active = active;
        return this;
    }

    public ActivateTemplateRequest setKey(String key) {
        this.key = key;
        return this;
    }
}
