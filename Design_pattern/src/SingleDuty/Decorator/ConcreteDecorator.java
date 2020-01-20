package SingleDuty.Decorator;

//在这里可以定义被修饰者，并且给他加功能
public class ConcreteDecorator extends Decorator {
    //定义被修饰者
    public ConcreteDecorator(Compent compent) {
        super(compent);
    }

    //加自己的功能
    public void method1(){
        System.out.println("method1");
    }

    //完成最后的功能

    @Override
    public void operation() {
        //选择合适位置，加自己的功能
        method1();
        super.operation();
    }
}
