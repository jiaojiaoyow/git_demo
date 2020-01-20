package SingleDuty.Decorator;

public class ConcreteDecorator1 extends Decorator {
    public ConcreteDecorator1(Compent compent) {
        super(compent);
    }

    public void method1(){
        System.out.println("method2");
    }

    @Override
    public void operation() {
        method1();
        super.operation();
}
}
