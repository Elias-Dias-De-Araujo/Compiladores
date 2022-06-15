import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Binary_tree {
    private Node root;
    private Node nodeFinded = null;
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
            int sumQuantSides = 0;
            String[] splited = null;
            String aux = "";
            String reserved = cutRow.get(current);
            ArrayList<String> newArrayHash = new ArrayList<String>();
            if (cutRow.get(current + 1).equals("(")) {
                reserved = changeReservedName(reserved);
                Node newNode = new Node(reserved);
                if (scopes.isEmpty()) {
                    this.root = newNode;
                } else {
                    splited = scopes.get(scopes.size() - 1).split(" ");
                    aux = splited[0] + " " + splited[1];
                    inOrderFind(root, aux);
                    if (splited[2].equalsIgnoreCase("0")) {
                        nodeFinded.setLeft(newNode);
                    } else if (splited[2].equalsIgnoreCase("1")) {
                        nodeFinded.setRight(newNode);
                    }
                    sumQuantSides = Integer.parseInt(splited[2]) + 1;
                    aux = aux + " " + sumQuantSides;
                    nodeFinded = null;
                    // tablePatterns.get(scopes.get(scopes.size() - 1)).add(cutRow.get(current));
                }
                if (!scopes.isEmpty()) {
                    scopes.set(scopes.size() - 1, aux); // Aqui é para atualizar o valor de side disponivel no nó atual
                }
                scopes.add(reserved + " 0"); // adicionando novo nó
                current += 1;
            } else if (cutRow.get(current).equals(")")) {
                scopes.remove(scopes.size() - 1);
            } else if (!cutRow.get(current).equals(",") && !cutRow.get(current).equals("(")
                    && !cutRow.get(current).equals(")")) {
                if (cutRow.get(current + 1).equals(",") || cutRow.get(current + 1).equals(")")) {
                    reserved = changeReservedName(reserved);
                    Node newNode = new Node(reserved);

                    splited = scopes.get(scopes.size() - 1).split(" ");
                    aux = splited[0] + " " + splited[1];
                    inOrderFind(root, aux);
                    if (splited[2].equalsIgnoreCase("0")) {
                        nodeFinded.setLeft(newNode);
                    } else if (splited[2].equalsIgnoreCase("1")) {
                        nodeFinded.setRight(newNode);
                    }
                    sumQuantSides = Integer.parseInt(splited[2]) + 1;
                    aux = aux + " " + sumQuantSides;
                    nodeFinded = null;

                    // tablePatterns.get(scopes.get(scopes.size() - 1)).add(cutRow.get(current));
                } else {
                    String getTogether = cutRow.get(current) + " " + cutRow.get(current + 1);

                    Node newNode = new Node(getTogether);

                    splited = scopes.get(scopes.size() - 1).split(" ");
                    aux = splited[0] + " " + splited[1];
                    inOrderFind(root, aux);
                    if (splited[2].equalsIgnoreCase("0")) {
                        nodeFinded.setLeft(newNode);
                    } else if (splited[2].equalsIgnoreCase("1")) {
                        nodeFinded.setRight(newNode);
                    }
                    sumQuantSides = Integer.parseInt(splited[2]) + 1;
                    aux = aux + " " + sumQuantSides;
                    if (!scopes.isEmpty()) {
                        scopes.set(scopes.size() - 1, aux); // Aqui é para atualizar o valor de side disponivel no nó
                                                            // atual
                    }
                    nodeFinded = null;

                    // tablePatterns.get(scopes.get(scopes.size() - 1)).add(getTogether);
                    current += 1;
                }
            }
            current += 1;
        }

        printing(root);
    }

    public void printing(Node current) {
        if (current != null) {
            printing(current.getLeft());
            System.out.println(current.getValue());
            printing(current.getRight());
        }
    }

    public void inOrderFind(Node current, String valueToFind) {
        if (current.getValue().equalsIgnoreCase(valueToFind)) {
            nodeFinded = current;
        } else {
            inOrderFind(current.getLeft(), valueToFind);
            if (nodeFinded == null) {
                inOrderFind(current.getRight(), valueToFind);
            }
        }
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

/*
 * Node Estrutura:
 * Nome do Node | identificador | Quantidade de nós
 */