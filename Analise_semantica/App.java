import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        File file = new File("input.txt");
        Scanner scan = new Scanner(file);
        boolean error = false;
        Semantical_analiser sa = new Semantical_analiser();
    
        while(scan.hasNextLine()) {
            sa.setRow(scan.nextLine());
            error = sa.analiser(); 
            if(!error) {
                break;
            }
        }

        if(!error == false) {
            System.out.println("Sucesso");
        }else {
            System.out.println("Erro Semântico");
        }

        scan.close();
        
    }
}
