import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;

public class Binary_tree {
    private Node root;
    private Node nodeFinded = null;
    private int plus, less, mult, div, move, mem = 0;
    private int TEMP, ADD, MUL, SUB, DIV, ADDI, SUBI, LOAD, STORE, MOVEM = 0;
    private String row;
    private int current = 1;
    private ArrayList<String> cutRow = new ArrayList<String>();
    private ArrayList<String> scopes = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> patterns = new HashMap<String, ArrayList<String>>();

    public Binary_tree() {
        ArrayList<String> currentPattern = new ArrayList<String>();
        this.root = null;
        currentPattern.add("TEMP ");
        patterns.put("TEMP", currentPattern);
        currentPattern.clear();

        currentPattern.add("+ ");
        patterns.put("ADD", currentPattern);
        currentPattern.clear();

        currentPattern.add("- ");
        patterns.put("SUB", currentPattern);
        currentPattern.clear();

        currentPattern.add("* ");
        patterns.put("MUL", currentPattern);
        currentPattern.clear();

        currentPattern.add("/ ");
        patterns.put("DIV", currentPattern);
        currentPattern.clear();

        currentPattern.add("CONST +");
        currentPattern.add("CONST ");
        patterns.put("ADDI", currentPattern);
        currentPattern.clear();

        currentPattern.add("CONST -");
        patterns.put("SUBI", currentPattern);
        currentPattern.clear();

        currentPattern.add("CONST + MEM");
        currentPattern.add("CONST MEM");
        currentPattern.add("MEM ");
        patterns.put("LOAD", currentPattern);
        currentPattern.clear();

        currentPattern.add("CONST + MEM MOVE");
        currentPattern.add("CONST MEM MOVE");
        currentPattern.add("MEM MOVE");
        patterns.put("STORE", currentPattern);
        currentPattern.clear();

        currentPattern.add("MEM MOVE MEM");
        patterns.put("MOVEM", currentPattern);
        currentPattern.clear();

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
                    newNode.setParent(nodeFinded);

                    sumQuantSides = Integer.parseInt(splited[2]) + 1;
                    aux = aux + " " + sumQuantSides;
                    nodeFinded = null;
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

                // Nó folha sem identificador de CONST ou TEMP
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

                    newNode.setParent(nodeFinded);

                    sumQuantSides = Integer.parseInt(splited[2]) + 1;
                    aux = aux + " " + sumQuantSides;
                    if (!scopes.isEmpty()) {
                        scopes.set(scopes.size() - 1, aux); // Aqui é para atualizar o valor de side disponivel no nó
                                                            // atual
                    }
                    nodeFinded = null;

                } else { // Nó folha com identificador de CONST ou TEMP
                    String getTogether = cutRow.get(current) + " " + cutRow.get(current + 1);

                    Node newNode = new Node(getTogether);

                    splited = scopes.get(scopes.size() - 1).split(" ");
                    aux = splited[0] + " " + splited[1];
                    inOrderFind(root, aux);

                    /*
                     * Aqui é verificado se o valor de verificação do side é 0 ou 1
                     * se for 0 então será atribuído o nó atual ao lado esquerdo do pai
                     * e se for 1 então será atribuído ao lado direito.
                     */
                    if (splited[2].equalsIgnoreCase("0")) {
                        nodeFinded.setLeft(newNode);
                    } else if (splited[2].equalsIgnoreCase("1")) {
                        nodeFinded.setRight(newNode);
                    }
                    // Fim da verificação de esquerda ou direita

                    newNode.setParent(nodeFinded); // setando pai

                    sumQuantSides = Integer.parseInt(splited[2]) + 1; // pega o valor do side e soma mais 1

                    aux = aux + " " + sumQuantSides;

                    if (!scopes.isEmpty()) {
                        scopes.set(scopes.size() - 1, aux); // Aqui é para atualizar o valor de side disponivel no nó
                                                            // atual
                    }
                    nodeFinded = null;

                    current += 1;
                }
            }
            current += 1;
        }

        posOrder(root);
    }

    public void selectPatterns(Node node) {
        if (node.getPattern() != null) {
            return;
        }

        if (node.getValue().equals("FP")) {
            node.setPattern("TEMP " + TEMP);
            TEMP += 1;
        } else {
            ArrayList<String> nodesThrough = new ArrayList<String>();
            ArrayList<String> patternsToLook = new ArrayList<String>();
            String[] splited = node.getValue().split(" ");
            String keyFromNode = splited[0];

            if (keyFromNode.equalsIgnoreCase("+")) {
                node.setPattern("ADD " + ADD);
                ADD += 1;
            } else if (keyFromNode.equalsIgnoreCase("*")) {
                node.setPattern("MUL " + MUL);
                MUL += 1;
            } else if (keyFromNode.equalsIgnoreCase("-")) {
                node.setPattern("SUB " + SUB);
                SUB += 1;
            } else if (keyFromNode.equalsIgnoreCase("/")) {
                node.setPattern("DIV " + DIV);
                DIV += 1;
            } else if (keyFromNode.equalsIgnoreCase("CONST")) {

            }
        }
    }

    public void posOrder(Node current) { // Função responsável por printar a árvore em pre-ordem
        if (current != null) {
            posOrder(current.getLeft());
            posOrder(current.getRight());
            selectPatterns(current);
        }
    }

    // Função responsável por encontrar um determinado nó na árvore e retorná-lo
    public void inOrderFind(Node current, String valueToFind) {
        if (current != null) {
            if (current.getValue().equalsIgnoreCase(valueToFind)) {
                nodeFinded = current; // O nó encontrado será salvo nessa variável que é global
            } else {
                inOrderFind(current.getLeft(), valueToFind);
                if (nodeFinded == null) {
                    inOrderFind(current.getRight(), valueToFind);
                }
            }
        }
    }

    // Função responsável por definir o identificador de cada palavra reservada
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