package com.demo.ruleengine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Condition {
    private String id;
    private String operator;
    private String dataType;
    private String value;

    public Condition(String id, String operator, String dataType, String value) {
        this.id = id;
        this.operator = operator;
        this.dataType = dataType;
        this.value = value.toLowerCase();
    }

    public boolean apply(Signal signal) {
        if (!dataType.equals(signal.getValueType())) {
            return true;
        }
        switch (dataType) {
            case "String":
                return evaluateStringCondition(signal);
            case "Datetime":
                return  evaluateDateCondition(signal);
            case "Integer":
                return evaluateNumericCondition(signal);
        }
        return false;
    }

    private boolean evaluateStringCondition(Signal signal) {
        switch(operator) {
            case "equals":
                    return value.equals(signal.getValue());
            case "not equals":
                return !value.equals(signal.getValue());
        }
        return false;
    }

    private boolean evaluateDateCondition(Signal signal) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss");
        LocalDateTime signalValue = LocalDateTime.parse(signal.getValue(), formatter);
        LocalDateTime conditionValue = LocalDateTime.parse(value, formatter);
        switch(operator) {
            case "equals":
                return signalValue.equals(conditionValue);
            case "not equals":
                return !signalValue.equals(conditionValue);
            case "greater than":
                return signalValue.isAfter(conditionValue);
            case "less than":
                return signalValue.isBefore(conditionValue);
        }
        return false;
    }

    private boolean evaluateNumericCondition(Signal signal) {
        int conditionValue = Integer.parseInt(value);
        int signalValue = Integer.parseInt(value);
        switch(operator) {
            case "equals":
                return conditionValue == signalValue;
            case "not equals":
                return conditionValue != signalValue;
            case "greater than":
                return signalValue >= conditionValue;
            case "less than":
                return signalValue <= conditionValue;
        }
        return false;
    }
}
