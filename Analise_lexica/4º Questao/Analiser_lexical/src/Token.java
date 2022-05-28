public class Token {
    public static final String ID = "ID";
    public static final String INT = "INT";
    public static final String STRING = "STRING";
    public static final String NUM = "NUM";
    public static final String WORD = "WORD";
    public static final String OP_ARITHMETIC = "OP_ARITHMETIC";
    public static final String OP_RELATIONAL = "OP_RELATIONAL";
    public static final String L_PAREN = "L_PAREN";
    public static final String R_PAREN = "R_PAREN";
    public static final String L_BRACE = "L_BRACE";
    public static final String R_BRACE = "R_BRACE";
    public static final String SEMICOLON = "SEMICOLON";
    public static final String COMMA = "COMMA";
    public static final String IF = "IF";
    public static final String ELSE = "ELSE";
    public static final String RETURN = "RETURN";
    public static final String ERROR = "ERROR";

    private String type;
    private String text;

    public Token(String type, String text) {
        super();
        this.type = type;
        this.text = text;
    }

    public Token() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return type;
    }

}
