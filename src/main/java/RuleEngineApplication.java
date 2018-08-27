import com.demo.ruleengine.RuleEngine;

public class RuleEngineApplication {
    public static void main(String args[]) {
        RuleEngine ruleEngine = new RuleEngine();
        ruleEngine.loadRules()
                .startStream();
    }
}
