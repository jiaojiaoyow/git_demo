package ComponentCollaboration.Observer;

public class Client {
    public static void main(String[] args) {
        Subject subject=new ConcreateSubject();
        Observer observer=new Observer1();
        subject.addObserver(observer);
        ((ConcreateSubject) subject).doSome();

    }
}
