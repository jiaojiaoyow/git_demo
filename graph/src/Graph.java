import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

/**
 * 最周围的一圈是无用的值，所以每一次传进来的值是5，但是创建数组时会加1大小
 */
public class Graph {
    //顶点数
    int numV;

    //边数
    int numE;

    //邻接矩阵
    int[][] matrix;

    //是否有向
    boolean isDirected;

    //获得一个输入函数
    Scanner scn=new Scanner(System.in);

    //构造方法
    Graph(int numV,boolean isDirected){
        numV++;
        this.numV=numV;
        this.isDirected=isDirected;
        matrix=new int[numV][numV];
    }

    int[][] createGraph(){
        System.out.println("输入边数：");
        this.numE=scn.nextInt();
        int k=0,j=0;
        for(int i=0;i<numE;i++){
            System.out.print("输入起点： ");
            k=scn.nextInt();
            System.out.println();
            System.out.print("输入终点：  ");
            j=scn.nextInt();
            if(isDirected){
                //有向
                matrix[k][j]=1;
            }else {
                matrix[k][j]=matrix[j][k]=1;
            }
        }
        return matrix;
    }

    int [][] createTest(){
        this.numE=5;
        matrix[1][2]=1;
        matrix[1][3]=1;
        matrix[2][3]=1;
        matrix[1][4]=1;
        matrix[4][5]=1;

        return matrix;
    }

    void output(){
        for(int i=0;i<numV;i++){
            for (int j = 0; j <numV ; j++) {
                System.out.print(matrix[i][j]+"    ");
            }
            System.out.println();
        }
    }

    //用栈来实现深度优先遍历,(从root顶点开始遍历)
    void dfs(int root){
        //这个数组用来标记是否遍历过
        boolean []is_visit=new boolean[numV+1];
        Stack<Integer> stack=new Stack<>();

        stack.push(root);
        while (!stack.isEmpty()){
            //获取当前元素
            int temp=stack.pop();
            if(is_visit[temp]!=true){
                System.out.println("节点"+temp);
                //标记当前元素
                is_visit[temp]=true;
            }
            //找到当前元素的邻接元素
            for (int i = 1; i <numV ; i++) {
                if(matrix[temp][i]==1&&is_visit[i]==false){
                    stack.push(i);
                }
            }
        }
    }

    int find(int i){
        for (int j = 1; j < numV; j++) {
            if(matrix[i][j]==0){
                return j;
            }
        }
        return 0;
    }

}
