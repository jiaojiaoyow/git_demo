package FatherOrSon;

public class Son extends Father {
    int a=11;
    int b=12;

    Son(){
        System.out.println("son"+a+b);
    }

    @Override
    void run() {
        System.out.println("son");
    }

    void run2(){
        System.out.println("son run2");
    }

    void run3(){
        System.out.println("son run3");
    }
}
