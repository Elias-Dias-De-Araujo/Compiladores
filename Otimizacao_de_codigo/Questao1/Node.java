public class Node {
    private String value;
    private Node left;
    private Node right;

    public Node(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getLeft() {
        return this.left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return this.right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void printTreeGraph() {
        printTreeGraph(this, false, "");
    }

    private void printTreeGraph(Node node, boolean inLeft, String space) {
        if (node != null) {
            String[] splited = node.value.split(" ");
            if (splited[0].equalsIgnoreCase("CONST") || splited[0].equalsIgnoreCase("TEMP")) {
                System.out.println(space + (inLeft ? "|-- " : "\\-- ") + node.value);
            } else {
                System.out.println(space + (inLeft ? "|-- " : "\\-- ") + splited[0]);
            }
            printTreeGraph(node.left, true, space + (inLeft ? "|   " : "    "));
            printTreeGraph(node.right, false, space + (inLeft ? "|   " : "    "));
        }
    }
}
