import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Print_tree {
    private int max;
    private String row;
    private int current = 1;
    private ArrayList<String> cutRow = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> tablePatterns = new HashMap<String, ArrayList<String>>();
    private ArrayList<String> scopes = new ArrayList<String>();
    Pattern patternID = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*");

    public Print_tree() {

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

        this.max = cutRow.size() - 1;
        this.cutRow.add(" ");
    }

    public void creatingTree() {
        while (!cutRow.get(current).equals(" ")) {
            Matcher matchId = patternID.matcher(cutRow.get(current));
            boolean matchFoundId = matchId.find();
            ArrayList<String> currentValues = new ArrayList<String>();

            if (cutRow.get(current + 1).equals("(")) {
                if (!scopes.isEmpty()) {
                    tablePatterns.get(scopes.get(scopes.size() - 1)).add(cutRow.get(current));
                }
                tablePatterns.put(cutRow.get(current), currentValues);
                scopes.add(cutRow.get(current));
            } else if (cutRow.get(current).equals(")")) {
                scopes.remove(scopes.size() - 1);
            } else if (!cutRow.get(current).equals(",") && !cutRow.get(current).equals("(")
                    && !cutRow.get(current).equals(")")) {
                if (cutRow.get(current + 1).equals(",") || cutRow.get(current + 1).equals(")")) {
                    tablePatterns.get(scopes.get(scopes.size() - 1)).add(cutRow.get(current));
                } else {
                    String getTogether = cutRow.get(current) + cutRow.get(current + 1);
                    tablePatterns.get(scopes.get(scopes.size() - 1)).add(getTogether);
                    current += 1;
                }
            }
            current += 1;
        }

        printing();
    }

    public void printing() {

    }

}