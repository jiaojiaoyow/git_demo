package SingleDuty.Decorator;

public class Client {
    public static void main(String[] args) {
        Compent compent=new ConretetComponent();
        compent=new ConcreteDecorator(compent);
        compent=new ConcreteDecorator(compent);
        compent=new ConcreteDecorator(compent);
        compent.operation();
    }
}
