import java.util.ArrayList; // import the ArrayList class

public class Binary_tree {
    public Node root;
    private Node nodeFinded = null;
    private int plus, less, mult, div, move, mem = 0;
    private String row;
    private int current = 1;
    private ArrayList<String> cutRow = new ArrayList<String>();
    private ArrayList<String> scopes = new ArrayList<String>();

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
            if (cutRow.get(current + 1).equals("(")) {
                reserved = changeReservedName(reserved);
                Node newNode = new Node(reserved);
                if (scopes.isEmpty()) {
                    this.root = newNode;
                } else {
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

        // posOrder(root);
    }

    public void preOrder(Node current) { // Função responsável por printar a árvore em pre-ordem
        if (current != null) {
            String[] splited = current.getValue().split(" ");
            if (splited[0].equalsIgnoreCase("CONST") || splited[0].equalsIgnoreCase("TEMP")) {
                System.out.println(current.getValue());
            } else {
                System.out.println(splited[0]);
            }
            preOrder(current.getLeft());
            preOrder(current.getRight());
        }
    }

    public void inOrder(Node current) { // Função responsável por printar a árvore em pre-ordem
        if (current != null) {
            inOrder(current.getLeft());
            String[] splited = current.getValue().split(" ");
            if (splited[0].equalsIgnoreCase("CONST") || splited[0].equalsIgnoreCase("TEMP")) {
                System.out.println(current.getValue());
            } else {
                System.out.println(splited[0]);
            }
            inOrder(current.getRight());
        }
    }

    public void posOrder(Node current) { // Função responsável por printar a árvore em pre-ordem
        if (current != null) {
            posOrder(current.getLeft());
            posOrder(current.getRight());
            String[] splited = current.getValue().split(" ");
            if (splited[0].equalsIgnoreCase("CONST") || splited[0].equalsIgnoreCase("TEMP")) {
                System.out.println(current.getValue());
            } else {
                System.out.println(splited[0]);
            }
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