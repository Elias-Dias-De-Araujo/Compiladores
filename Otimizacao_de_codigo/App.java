import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        File file = new File("input.txt");
        Scanner scan = new Scanner(file);
        Print_tree pt = new Print_tree();

        while (scan.hasNextLine()) {
            pt.setRow(scan.nextLine());
            pt.creatingTree();
        }

        scan.close();

    }
}
