public class App {
    public static void main(String[] args) throws Exception {
        try {
            String tokens = "";
            Lexical_analiser la = new Lexical_analiser("src/input.txt");
            Token token = null;
            do {
                token = la.nextToken();
                if(token != null) {
                    tokens += token + " ";
                }
            } while (token != null);
            System.out.println(tokens);
        } catch (Lexical_exception e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
       
    }
}
