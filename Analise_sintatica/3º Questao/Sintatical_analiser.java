import java.util.ArrayList; // import the ArrayList class

public class Sintatical_analiser {
    private int max;
    private int current = 1;
    private int preview = 0;
    private int lookahead = 0;
    private String row;
    private int cPB = 0;
    private ArrayList<String>cutRow = new ArrayList<String>();

    public void setRow(String currentRow) {
        String[] cut;
        cut = currentRow.split(" ");
        this.row = currentRow;
        this.cutRow.clear();

        this.cutRow.add(" ");

        for(int i = 0; i < cut.length; i++) {
            this.cutRow.add(cut[i]);
        }
        
        this.max = cutRow.size() - 1; 
        this.cutRow.add(" ");
        
    }

    public void analiser() {
        if(checkEndOfRow()) {
            System.out.println("Sucesso");
            return;
        }

        if(cutRow.get(current).equals(" ")) {
            System.out.println("Erro Sintático");
            return;
        }

        preview = current - 1;
        lookahead = current + 1;

        String onlyOne = cutRow.get(current);
        String onlyTwo = cutRow.get(current) + cutRow.get(lookahead);
        
        switch (onlyTwo) {
            case "id(" :
                if((current+2) > max) {
                    System.out.println("Erro sintático");
                }else {
                    cPB += 1;
                    current += 2;
                    analiser();
                }
                break;
            case "],":
                if((current+2) > max) {
                    System.out.println("Erro sintático");
                }else {
                    cPB -= 1;
                    current += 2;
                    analiser();
                }
                break;

            case ")]":
                cPB -= 2;
                current += 2;
                analiser();
                break;
        
            default:
                switch(onlyOne) {
                    case "id":
                        if ((current == 1)) { 
                            current += 1;
                            analiser();
                        }else if (cutRow.get(preview).equals(",") || cutRow.get(preview).equals("(")) {
                            current += 1;
                            analiser();
                        }    
                        break;

                    case ")":
                        if(cutRow.get(preview).equals("id") || cutRow.get(preview).equals("]") || cutRow.get(preview).equals(")")) {
                            cPB -= 1;
                            current += 1;
                            analiser();
                        }else {
                            System.out.println("Erro sintático");
                        }
                        break;

                    case "]":
                        if (cutRow.get(preview).equals(",") || cutRow.get(preview).equals("(")){
                            System.out.println("Erro sintático");
                        }else {
                            cPB -= 1;
                            current += 1;
                            analiser();
                        }
                        break;

                    case ",":
                        current += 1;
                        analiser();
                        break;

                    default:
                        System.out.println("Erro sintático");
                        break;
                } 
                   
        }
    }

    public boolean checkEndOfRow() {
        if((current > max) && (cPB == 0)) {
            return true;
        }else {
            return false;
        }
    }

}
