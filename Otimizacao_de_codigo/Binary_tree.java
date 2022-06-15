import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Binary_tree {
    private Node root;
    private int plus, less, mult, div, move, mem = 0;
    private String row;
    private int current = 1;
    private ArrayList<String> cutRow = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> tablePatterns = new HashMap<String, ArrayList<String>>();
    private ArrayList<String> scopes = new ArrayList<String>();
    Pattern patternID = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*");

    public Binary_tree() {
        this.root = null;
    }

    public void setRow(String currentRow) {
        String[] cut;
        cut = currentRow.split(" ");
        this.row = currentRow;
        this.cutRow.clear();

        this.cutRow.add(" ");

        for (int i = 0; i < cut.length; i++) {
            this.cutRow.add(cut[i]);
        }
        this.cutRow.add(" ");
    }

    public void creatingTree() {
        while (!cutRow.get(current).equals(" ")) {
            String reserved = cutRow.get(current);
            ArrayList<String> newArrayHash = new ArrayList<String>();
            if (cutRow.get(current + 1).equals("(")) {
                reserved = changeReservedName(reserved);
                Node newNode = new Node(reserved);
                if (!scopes.isEmpty()) {
                    tablePatterns.get(scopes.get(scopes.size() - 1)).add(cutRow.get(current));
                }
                tablePatterns.put(cutRow.get(current), newArrayHash);
                scopes.add(cutRow.get(current));
                current += 1;
            } else if (cutRow.get(current).equals(")")) {
                scopes.remove(scopes.size() - 1);
            } else if (!cutRow.get(current).equals(",") && !cutRow.get(current).equals("(")
                    && !cutRow.get(current).equals(")")) {
                if (cutRow.get(current + 1).equals(",") || cutRow.get(current + 1).equals(")")) {
                    tablePatterns.get(scopes.get(scopes.size() - 1)).add(cutRow.get(current));
                } else {
                    String getTogether = cutRow.get(current) + " " + cutRow.get(current + 1);
                    tablePatterns.get(scopes.get(scopes.size() - 1)).add(getTogether);
                    current += 1;
                }
            }
            current += 1;
        }

        printing();
    }

    public void printing() {
        String Tree = "";
        for (String key : tablePatterns.keySet()) {
            if (tablePatterns.get(key).size() == 1) {
                Tree += key + "--|" + tablePatterns.get(key).get(0);
            } else if (tablePatterns.get(key).size() > 1) {
                Tree += "---|";
                Tree += tablePatterns.get(key).get(0); // Esquerda
                Tree += tablePatterns.get(key).get(1); // Direita
            }
        }
        System.out.println(Tree);
    }

    public String changeReservedName(String reserved) {
        if (reserved.equalsIgnoreCase("+")) {
            reserved = reserved + " " + plus;
            plus += 1;
        } else if (reserved.equalsIgnoreCase("-")) {
            reserved = reserved + " " + less;
            less += 1;
        } else if (reserved.equalsIgnoreCase("*")) {
            reserved = reserved + " " + mult;
            mult += 1;
        } else if (reserved.equalsIgnoreCase("/")) {
            reserved = reserved + " " + div;
            div += 1;
        } else if (reserved.equalsIgnoreCase("move")) {
            reserved = reserved + " " + move;
            move += 1;
        } else if (reserved.equalsIgnoreCase("mem")) {
            reserved = reserved + " " + mem;
            mem += 1;
        }

        return reserved;
    }

}