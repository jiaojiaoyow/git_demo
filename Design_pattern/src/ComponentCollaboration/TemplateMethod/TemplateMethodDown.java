package ComponentCollaboration.TemplateMethod;

//继承上级的方法，并且实现其中未实现的部分流程方法，使用流程只需要调用上级已经固定好的run方法（骨架）
public class TemplateMethodDown extends TemplateMethodUp {
    @Override
    void step2() {
        System.out.println("step2");
        //流程2的具体方法代码
    }

    /*
    如果是使用非模块方法，则会在下级的时候，将所有的步骤都实现了，再来写相关的流程骨架
    也就是在子类再写run方法
     */
//    void run(){
//        step1();
//        step2();
//        step3();
//    }

    public static void main(String[] args) {
        TemplateMethodUp templateMethodUp=new TemplateMethodDown();
        TemplateMethodDown templateMethodDown=new TemplateMethodDown();

        //实现流程，只需要调用父类的run方法，就可以完整的使用流程了
        templateMethodUp.run();
        templateMethodDown.run();
    }
}
