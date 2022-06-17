import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        File file = new File("input.txt");
        Scanner scan = new Scanner(file);
        Binary_tree bt = new Binary_tree();

        System.out.println("Print da árvore");
        while (scan.hasNextLine()) {
            bt.setRow(scan.nextLine());
            bt.creatingTree();
            bt.root.printTreeGraph();
        }

        scan.close();

    }
}
