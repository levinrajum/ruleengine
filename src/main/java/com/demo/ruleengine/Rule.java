package com.demo.ruleengine;

import java.util.List;

public class Rule {
    private String id;
    private String signalId;
    private List<Condition> conditions;

    public Rule(String id, String signalId, List<Condition> conditions) {
        this.id = id;
        this.signalId = signalId;
        this.conditions = conditions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public boolean process(Signal signal) {
        for (Condition condition: conditions) {
            boolean succeed = condition.apply(signal);
            if (!succeed) {
                return false;
            }
        }
        return true;
    }
}
