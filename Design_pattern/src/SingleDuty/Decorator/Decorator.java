package SingleDuty.Decorator;


//作为一个抽象类,主要用来约束具体实现类
public abstract class Decorator extends Compent {
    //必然需要有一个指向compent的类（被修饰者）
    Compent compent=null;

    //通过构造函数传递给被修饰者
    public Decorator(Compent compent){
        this.compent=compent;
    }

    @Override
    public void operation() {
        if(compent!=null){
            compent.operation();
        }
    }
}
