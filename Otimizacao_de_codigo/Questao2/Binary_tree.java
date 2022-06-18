import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;

public class Binary_tree {
    private boolean runAgain = false;
    public Node root;
    private Node nodeFinded = null;
    private int plus, less, mult, div, move, mem = 0;
    private int TEMP, ADD, MUL, SUB, DIV, ADDI, SUBI, LOAD, STORE, MOVEM = 0;
    private String row;
    private int current = 1;
    private ArrayList<String> cutRow = new ArrayList<String>();
    private ArrayList<String> scopes = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> patterns = new HashMap<String, ArrayList<String>>();

    public Binary_tree() {
        this.root = null;

        ArrayList<String> currentPattern = new ArrayList<String>();
        currentPattern.add("TEMP");
        patterns.put("TEMP", currentPattern);

        ArrayList<String> currentPattern1 = new ArrayList<String>();
        currentPattern1.add("+");
        patterns.put("ADD", currentPattern1);

        ArrayList<String> currentPattern2 = new ArrayList<String>();
        currentPattern2.add("-");
        patterns.put("SUB", currentPattern2);

        ArrayList<String> currentPattern3 = new ArrayList<String>();
        currentPattern3.add("*");
        patterns.put("MUL", currentPattern3);

        ArrayList<String> currentPattern4 = new ArrayList<String>();
        currentPattern4.add("/");
        patterns.put("DIV", currentPattern4);

        ArrayList<String> currentPattern5 = new ArrayList<String>();
        currentPattern5.add("CONST +");
        currentPattern5.add("CONST");
        patterns.put("ADDI", currentPattern5);

        ArrayList<String> currentPattern6 = new ArrayList<String>();
        currentPattern6.add("CONST -");
        patterns.put("SUBI", currentPattern6);

        ArrayList<String> currentPattern7 = new ArrayList<String>();
        currentPattern7.add("CONST + MEM");
        currentPattern7.add("CONST MEM");
        currentPattern7.add("MEM");
        patterns.put("LOAD", currentPattern7);

        ArrayList<String> currentPattern8 = new ArrayList<String>();
        currentPattern8.add("CONST + MEM MOVE");
        currentPattern8.add("CONST MEM MOVE");
        currentPattern8.add("MEM MOVE");
        patterns.put("STORE", currentPattern8);

        ArrayList<String> currentPattern9 = new ArrayList<String>();
        currentPattern9.add("MEM MOVE MEM");
        patterns.put("MOVEM", currentPattern9);

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
            int sumNumNodes = 0;
            Node nodeAux = node;

            HashMap<String, ArrayList<Integer>> nodesThrough = new HashMap<String, ArrayList<Integer>>();
            String[] splited = node.getValue().split(" ");
            String keyFromNode = splited[0];

            if (keyFromNode.equalsIgnoreCase("TEMP")) {
                node.setPattern("TEMP " + TEMP);
                TEMP += 1;
            } else if (keyFromNode.equalsIgnoreCase("+")) {
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
                // ADDI
                {
                    ArrayList<Integer> numOfNodes = new ArrayList<Integer>();
                    for (int i = 0; i < patterns.get("ADDI").size(); i++) {
                        splited = patterns.get("ADDI").get(i).split(" ");
                        for (int j = 0; j < splited.length; j++) {
                            String[] splitedNodeKey = nodeAux.getValue().split(" ");
                            String nodeKey = splitedNodeKey[0];

                            if (nodeKey.equalsIgnoreCase(splited[j])) {
                                sumNumNodes += 1;
                            } else {
                                sumNumNodes = -1;
                                break;
                            }
                            nodeAux = nodeAux.getParent();
                        }
                        numOfNodes.add(sumNumNodes);
                        sumNumNodes = 0;
                        nodeAux = node;
                    }
                    nodesThrough.put("ADDI", numOfNodes);
                }
                // Fim do ADDI
                sumNumNodes = 0;
                // SUBI
                {
                    ArrayList<Integer> numOfNodes = new ArrayList<Integer>();
                    splited = patterns.get("SUBI").get(0).split(" ");
                    for (int j = 0; j < splited.length; j++) {
                        String[] splitedNodeKey = nodeAux.getValue().split(" ");
                        String nodeKey = splitedNodeKey[0];
                        if (j == 0) {
                            if (nodeAux.getParent().getRight() != nodeAux) {
                                sumNumNodes = -1;
                                break;
                            }
                        }
                        if (nodeKey.equalsIgnoreCase(splited[j])) {
                            sumNumNodes += 1;
                        } else {
                            sumNumNodes = -1;
                            break;
                        }
                        nodeAux = nodeAux.getParent();
                    }
                    numOfNodes.add(sumNumNodes);
                    nodesThrough.put("SUBI", numOfNodes);
                    nodeAux = node;
                }
                // Fim do SUBI
                sumNumNodes = 0;
                // LOAD
                {
                    ArrayList<Integer> numOfNodes = new ArrayList<Integer>();
                    for (int i = 0; i < patterns.get("LOAD").size() - 1; i++) {
                        splited = patterns.get("LOAD").get(i).split(" ");
                        for (int j = 0; j < splited.length; j++) {
                            String[] splitedNodeKey = nodeAux.getValue().split(" ");
                            String nodeKey = splitedNodeKey[0];
                            if (nodeKey.equalsIgnoreCase(splited[j])) {
                                sumNumNodes += 1;
                            } else {
                                sumNumNodes = -1;
                                break;
                            }
                            nodeAux = nodeAux.getParent();
                        }
                        numOfNodes.add(sumNumNodes);
                        sumNumNodes = 0;
                        nodeAux = node;
                    }
                    nodesThrough.put("LOAD", numOfNodes);

                }
                // Fim do LOAD
                sumNumNodes = 0;
                // STORE
                {
                    ArrayList<Integer> numOfNodes = new ArrayList<Integer>();
                    for (int i = 0; i < patterns.get("STORE").size() - 1; i++) {
                        splited = patterns.get("STORE").get(i).split(" ");
                        for (int j = 0; j < splited.length; j++) {
                            String[] splitedNodeKey = nodeAux.getValue().split(" ");
                            String nodeKey = splitedNodeKey[0];
                            if (nodeKey.equalsIgnoreCase(splited[j])) {
                                if (nodeKey.equalsIgnoreCase("MEM")) {
                                    if (nodeAux.getParent().getLeft() != nodeAux) {
                                        sumNumNodes = -1;
                                        break;
                                    }
                                }
                                sumNumNodes += 1;
                            } else {
                                sumNumNodes = -1;
                                break;
                            }
                            nodeAux = nodeAux.getParent();
                        }
                        numOfNodes.add(sumNumNodes);
                        sumNumNodes = 0;
                        nodeAux = node;
                    }
                    nodesThrough.put("STORE", numOfNodes);
                }
                // Fim do STORE

                String choosedKey = "";
                int indexNumMax = -1;
                int maxNumNode = 0;
                HashMap<String, ArrayList<Integer>> indexCostHash = new HashMap<String, ArrayList<Integer>>();

                // setando melhor index de cada padrão com seu respectivo número de nós
                for (String key : nodesThrough.keySet()) {
                    ArrayList<Integer> indexCostArr = new ArrayList<Integer>();
                    {
                        for (int i = 0; i < nodesThrough.get(key).size(); i++) {
                            if (nodesThrough.get(key).get(i) != -1) {
                                if (nodesThrough.get(key).get(i) >= maxNumNode) {
                                    indexNumMax = i;
                                    maxNumNode = nodesThrough.get(key).get(i);
                                }
                            }
                        }
                        indexCostArr.add(indexNumMax);
                        indexCostArr.add(maxNumNode);
                        indexCostHash.put(key, indexCostArr);

                        indexNumMax = -1;
                        maxNumNode = 0;
                    }
                }

                // Pegando o melhor padrão para aplicar
                for (String key : indexCostHash.keySet()) {
                    if (indexCostHash.get(key).get(1) >= maxNumNode) {
                        choosedKey = key;
                        maxNumNode = indexCostHash.get(key).get(1);
                    }
                }

                for (int i = 0; i < maxNumNode; i++) {
                    if (choosedKey.equalsIgnoreCase("ADDI")) {
                        nodeAux.setPattern("ADDI " + ADDI);
                    } else if (choosedKey.equalsIgnoreCase("SUBI")) {
                        nodeAux.setPattern("SUBI " + SUBI);
                    } else if (choosedKey.equalsIgnoreCase("LOAD")) {
                        nodeAux.setPattern("LOAD " + LOAD);
                    } else if (choosedKey.equalsIgnoreCase("STORE")) {
                        nodeAux.setPattern("STORE " + STORE);
                    }
                    nodeAux = nodeAux.getParent();
                }

                if (choosedKey.equalsIgnoreCase("ADDI")) {
                    ADDI += 1;
                } else if (choosedKey.equalsIgnoreCase("SUBI")) {
                    SUBI += 1;
                } else if (choosedKey.equalsIgnoreCase("LOAD")) {
                    LOAD += 1;
                } else if (choosedKey.equalsIgnoreCase("STORE")) {
                    STORE += 1;
                }

            } else if (keyFromNode.equalsIgnoreCase("MEM")) {

                // LOAD
                {
                    ArrayList<Integer> numOfNodes = new ArrayList<Integer>();
                    splited = patterns.get("LOAD").get(patterns.get("LOAD").size() - 1).split(" ");
                    for (int j = 0; j < splited.length; j++) {
                        String[] splitedNodeKey = nodeAux.getValue().split(" ");
                        String nodeKey = splitedNodeKey[0];
                        if (nodeKey.equalsIgnoreCase(splited[j])) {
                            sumNumNodes += 1;
                        } else {
                            sumNumNodes = -1;
                            break;
                        }
                        nodeAux = nodeAux.getParent();
                    }
                    numOfNodes.add(sumNumNodes);
                    nodesThrough.put("LOAD", numOfNodes);
                    sumNumNodes = 0;
                    nodeAux = node;
                }
                // Fim do LOAD
                sumNumNodes = 0;
                // STORE
                {
                    ArrayList<Integer> numOfNodes = new ArrayList<Integer>();
                    if (nodeAux.getParent() != null) {
                        if (nodeAux.getParent().getLeft() == nodeAux) {
                            sumNumNodes += 2;
                        } else {
                            sumNumNodes = -1;
                        }
                    }
                    numOfNodes.add(sumNumNodes);
                    nodesThrough.put("STORE", numOfNodes);
                    sumNumNodes = 0;
                    nodeAux = node;
                }
                // Fim do STORE

                String choosedKey = "";
                int indexNumMax = -1;
                int maxNumNode = 0;
                HashMap<String, ArrayList<Integer>> indexCostHash = new HashMap<String, ArrayList<Integer>>();

                // setando melhor index de cada padrão com seu respectivo número de nós
                for (String key : nodesThrough.keySet()) {
                    ArrayList<Integer> indexCostArr = new ArrayList<Integer>();
                    {
                        for (int i = 0; i < nodesThrough.get(key).size(); i++) {
                            if (nodesThrough.get(key).get(i) != -1) {
                                if (nodesThrough.get(key).get(i) >= maxNumNode) {
                                    indexNumMax = i;
                                    maxNumNode = nodesThrough.get(key).get(i);
                                }
                            }
                        }
                        indexCostArr.add(indexNumMax);
                        indexCostArr.add(maxNumNode);
                        indexCostHash.put(key, indexCostArr);

                        indexNumMax = -1;
                        maxNumNode = 0;
                    }
                }

                // Pegando o melhor padrão para aplicar
                for (String key : indexCostHash.keySet()) {
                    if (indexCostHash.get(key).get(1) >= maxNumNode) {
                        choosedKey = key;
                        maxNumNode = indexCostHash.get(key).get(1);
                    }
                }

                for (int i = 0; i < maxNumNode; i++) {
                    if (choosedKey.equalsIgnoreCase("LOAD")) {
                        nodeAux.setPattern("LOAD " + LOAD);
                    } else if (choosedKey.equalsIgnoreCase("STORE")) {
                        nodeAux.setPattern("STORE " + STORE);
                    }
                    nodeAux = nodeAux.getParent();
                }

                if (choosedKey.equalsIgnoreCase("LOAD")) {
                    LOAD += 1;
                } else if (choosedKey.equalsIgnoreCase("STORE")) {
                    STORE += 1;
                }
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