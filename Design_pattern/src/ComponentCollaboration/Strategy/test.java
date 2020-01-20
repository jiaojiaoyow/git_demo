package ComponentCollaboration.Strategy;

public class test {
    public static void main(String[] args) {

        Context context;

        //使用第一种调用
        StrategyMethod strategyMethod1=new StrategyMethod1();
        context=new Context(strategyMethod1);
        context.run();

        //使用第二种调用
        StrategyMethod strategyMethod2=new StrategyMethod2();
        context=new Context(strategyMethod2);
        context.run();
    }
}
