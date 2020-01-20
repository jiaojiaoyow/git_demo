package ComponentCollaboration.Strategy;

/*
策略这个词应该怎么理解，可以说成一个方法，打个比方说，我们出门的时候会选择不同的出行方式，
比如骑自行车、坐公交、坐火车、坐飞机、坐火箭等等，这些出行方式，每一种都是一个策略。

策略模式（Strategy），定义了一组算法，将每个算法都封装起来，并且使它们之间可以互换（支持变化），
使算法可以独立客户程序而变化。
 */

//抽象出来的角色，例如收税是一个抽象的类，而他的子类（中国税法，美国税法）有各自不同的具体实现
public abstract class StrategyMethod {

    //算法方法（此处是抽象的）
    abstract public void algorithmInterface();

}
