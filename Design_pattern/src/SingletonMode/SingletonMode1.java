package SingletonMode;

public class SingletonMode1 {

    //1.首先设置构造方法为私有
    private SingletonMode1(){

    }

    //2.饿汉式单例，直接创建
    static private SingletonMode1 singletonMode1=new SingletonMode1();

    //3.创建一个方法可以获取该对象
    public SingletonMode1 getInstance(){
        //4.懒汉式，调用时生成对象
        if(singletonMode1==null){
            singletonMode1=new SingletonMode1();
        }
        return singletonMode1;
    }
}
