package com.demo.ruleengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RuleEngine {
    List rules;
    Map<String, List<Rule>> rulesForSignal;
    private static final String CONDITIONS = "conditions";
    private static final String RULE_ID = "ruleId";
    private static final String CONDITION_ID = "conditionId";
    private static final String CONDITION_OPERATOR = "operator";
    private static final String CONDITION_DATATYPE = "datatype";
    private static final String SIGNAL_ID = "signal";
    private static final String SIGNAL_VALUETYPE = "value_type";
    private static final String VALUE = "value";

    public RuleEngine() {
        rules = new ArrayList();
        rulesForSignal = new HashMap<>();
    }

    public RuleEngine loadRules() {
        JSONParser parser = new JSONParser();
        try
        {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("data/rule.json").getFile());
            JSONArray rulesJson = (JSONArray)parser.parse(new FileReader(file));
            Iterator<JSONObject> rulesIterator = rulesJson.iterator();
            while (rulesIterator.hasNext()) {
                JSONObject ruleObject = rulesIterator.next();
                rules.add(this.createRule(ruleObject));
            }

        } catch(FileNotFoundException fe) {
            fe.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private Rule createRule(JSONObject ruleObject) {
        String ruleStrId = (String) ruleObject.get(RULE_ID);
        String signalId = (String) ruleObject.get(SIGNAL_ID);
        JSONArray ruleConditionsObj = (JSONArray) ruleObject.get(CONDITIONS);
        List<Condition> conditions = createConditions(ruleConditionsObj);
        Rule rule = new Rule(ruleStrId, signalId, conditions);
        this.updateRulesForSignal(rule);
        return rule;
    }

    private List<Condition> createConditions(JSONArray ruleConditions) {
        List<Condition> conditions = new ArrayList<>();
        Iterator<JSONObject> conditionsIterator = ruleConditions.iterator();
        while (conditionsIterator.hasNext()) {
            JSONObject ruleObject = conditionsIterator.next();
            conditions.add(this.createCondition(ruleObject));
        }
        return conditions;
    }

    private Condition createCondition(JSONObject conditionObject) {
        String operator = (String) conditionObject.get(CONDITION_OPERATOR);
        String dataType = (String) conditionObject.get(CONDITION_DATATYPE);
        String id = (String) conditionObject.get(CONDITION_ID);
        String value = (String) conditionObject.get(VALUE);
        Condition condition = new Condition(id, operator, dataType, value);
        return condition;
    }

    private void updateRulesForSignal(Rule rule) {
        if (rulesForSignal.containsKey(rule.getSignalId())) {
            rulesForSignal.get(rule.getSignalId()).add(rule);
        } else {
            List<Rule> rules = new ArrayList<>();
            rules.add(rule);
            rulesForSignal.put(rule.getSignalId(), rules);
        }
    }

    public void startStream() {
        JSONArray rulesJson = null;
        JSONParser parser = new JSONParser();
        try {

            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("data/raw_data.json").getFile());
            rulesJson = (JSONArray)parser.parse(new FileReader(file));
            Iterator<JSONObject> signalsIterator = rulesJson.iterator();
            while (signalsIterator.hasNext()) {
                JSONObject ruleObject = signalsIterator.next();
                String signalId = (String) ruleObject.get(SIGNAL_ID);
                String valueType = (String) ruleObject.get(SIGNAL_VALUETYPE);
                String value = (String) ruleObject.get(VALUE);
                Signal signal = new Signal(signalId, valueType, value);
                this.process(signal);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void process(Signal signal) {
        if (rulesForSignal.containsKey(signal.getId())) {
            List<Rule> rules = rulesForSignal.get(signal.getId());
            for (Rule rule: rules) {
                boolean succeed = rule.process(signal);
                if (!succeed) {
                    System.out.println("signal "+signal.getId()+" blocked by rule "+rule.getId());
                    break;
                }
            }
        }
    }
}
