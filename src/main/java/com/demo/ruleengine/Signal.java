package com.demo.ruleengine;

public class Signal {
    private String id;
    private String valueType;
    private String value;

    public Signal(String signalId, String valueType, String value) {
        this.id = signalId;
        this.valueType = valueType;
        this.value = value.toLowerCase();
    }

    public String getId() {
        return id;
    }

    public void setId(String signalId) {
        this.id = signalId;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value.toLowerCase();
    }
}
