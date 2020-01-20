public class Test {

    public static void main(String[] args) {
        Graph graph=new Graph(5,true);
        int [][] test=graph.createTest();
        graph.output();
        graph.dfs(1);
    }
}
