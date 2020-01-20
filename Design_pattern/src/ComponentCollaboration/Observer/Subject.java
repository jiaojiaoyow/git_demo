package ComponentCollaboration.Observer;

import java.util.Vector;

//具体主题
public class Subject {

    //首先设立一个观察者数组
    private Vector<Observer> list=new Vector<>();

    //增加观察者
    void addObserver(Observer observer){
        list.add(observer);
    }

    //删除观察者
    void removeObserver(){
        if(list.size()>0){
            list.remove(list.size());
        }
    }

    //通知所有观察者
    public void notifyObserver(){
        for (Observer observer:list
             ) {
            observer.run();
        }
    }


}
