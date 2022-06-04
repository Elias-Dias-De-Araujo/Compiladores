import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        File file = new File("input.txt");
        Scanner scan = new Scanner(file);
    
        while(scan.hasNextLine()) {
            Sintatical_analiser sa = new Sintatical_analiser();
            sa.setRow(scan.nextLine());
            sa.analiser(); 
        }

        scan.close();
        
    }
}
