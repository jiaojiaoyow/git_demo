
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {

    }
    //比较当前节点是否大于子节点，是的话交换
    static void swap(int []array,int current,int size){
        if(current<size){
            int left=current*2+1;
            int right=current*2+2;
            int max=current;
            if(left<size&&array[max]<array[left]){
                max=left;
            }
            if(right<size&&array[max]<array[right]){
                max=right;
            }
            if(max!=current){
                int temp=array[max];
                array[max]=array[current];
                array[current]=temp;
            }
        }
    }
    //第一趟交换
    static void maxHeapify(int [] array,int size){
        for(int i=size-1;i>0;i--) {
            swap(array, i, size);
        }
    }
    //交换第一个和最后一个元素，则最后一个元素为最大值，最后一个元素为有序区，然后对无序区继续排序
    static void create(int []array){
        for(int i=0;i<array.length;i++){
            high(array,array.length-i);

            int temp=array[0];
            array[0]=array[array.length-1];
            array[array.length-1]=temp;
        }
    }

}