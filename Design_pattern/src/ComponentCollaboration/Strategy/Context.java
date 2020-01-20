package ComponentCollaboration.Strategy;

public class Context {

    StrategyMethod strategyMethod;

    public Context(StrategyMethod strategyMethod){
        this.strategyMethod=strategyMethod;
    }

    void run(){
        strategyMethod.algorithmInterface();
    }


}
