package FatherOrSon;


//对于方法，只要是使用子类对象new的值，则使用的是子类重载的方法
//但是对于字段而言，就算使用了new的子类对象，如果没有强转类型，则是使用父类的字段值
public class test {
    public static void main(String[] args) {
        Father father1=new Son();
        Father father2=new Father();
        Son son=new Son();

        father1.run();
        father2.run();
        //如果没有强转类型的话，因为使父类接收了子类的对象，所以还是父类原来的值，但是转为子类的话，则值为子类的值
        System.out.println("father of son :"+((Son) father1).a+((Son) father1).b+father1.a+father1.b);
        System.out.println("father :"+father2.b+father2.a);
        System.out.println("son :"+son.a+son.b);

        //父类能否实现子类对象的方法
        father1.run2();
        ((Son) father1).run3();
        ((Son) father1).run2();

    }
}
