package ComponentCollaboration.TemplateMethod;



/*
模块方法模式，也就是样板方法的意思，也就是晚绑定
1.条件，要某一个算法骨架是相对稳定，而将骨架中某一些步骤延迟到子类中y

2.灵活的应对子步骤的变化

3.在具体实现中，被templateMethod调用的虚方法可以实现，也可以没有任何实现
一般设置为protected，因为他是放在某个流程骨架的才有意义的，所以，一般要子类实现所有才有意义
而不应该设置为public，让每个人都可以调用实现他。
 */

//假设这部分由高级程序员写的部分功能，他的部分功能要加上下级的部分功能才可以完成一个完整的流程
public abstract class TemplateMethodUp {
    //流程的步骤(由高级程序员实现的部分)
    protected void step1(){
        System.out.println("step1");
    }
    protected void step3(){
        System.out.println("step3");
    }

    //比较次要的步骤，由下一级程序员实现，高级程序员先写一个抽象，下一级抽象员只需要实现即可
    abstract void step2();

    //这是一个相对稳定的骨架，也就是流程
    protected void run(){
        //按123的步骤运行下去
        step1();
        step2();
        step3();
    }

}
