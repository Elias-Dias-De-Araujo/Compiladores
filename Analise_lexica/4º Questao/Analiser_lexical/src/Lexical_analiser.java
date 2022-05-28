import java.nio.file.Paths;
import java.util.Scanner;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Lexical_analiser {
    private char[] content;
    private int estado;
    private int pos;
    public static final char INITWORD = '"';

    public Lexical_analiser(String filename) {
        try {
            String txtConteudo = "";
            File file = new File(filename);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) {
                txtConteudo = txtConteudo.concat(scan.nextLine() + "\n");
            }
            scan.close();
            System.out.println("Conteúdo do txt");
            System.out.println(txtConteudo);
            System.out.println("------------");
            content = txtConteudo.toCharArray();
            pos = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Token nextToken() {
        Token token;
        char currentChar;
        String term = "";
        if(isEOF()) {
            return null;
        }
        estado = 0;
        while(true) {
            currentChar = nextChar();
            if(currentChar == 'ε') {
                return null;
            }
            switch(estado) {
                case 0:
                    if(currentChar == 'r'){
                        term += currentChar;
                        estado = 21;
                    }else if(currentChar == 'e'){
                        term += currentChar;
                        estado = 17;
                    }else if(currentChar == 's'){
                        term += currentChar;
                        estado = 8;
                    }else if(currentChar == 'i'){
                        term += currentChar;
                        estado = 27;
                    }else if(currentChar == '<' || currentChar == '>'){
                        term += currentChar;
                        estado = 40;
                    }
                    else if(currentChar == '!'){
                        term += currentChar;
                        estado = 37;
                    }else if(currentChar == '='){
                        term += currentChar;
                        estado = 41;
                    }
                    else if(isID(currentChar)) {
                        term += currentChar;
                        estado = 1;
                    }
                    else if (isNUM(currentChar)) {
                        term += currentChar;
                        estado = 3;
                    }else if(isSpace(currentChar)) {
                        estado = 0;
                    }else if(isOP_ARITHMETIC(currentChar)) {
                        term += currentChar;
                        token = new Token();
                        token.setType(Token.OP_ARITHMETIC);
                        token.setText(term);
                        return token;
                    }else if(isL_PAREN(currentChar)) {
                        term += currentChar;
                        estado = 31;
                    }else if(isR_PAREN(currentChar)) {
                        term += currentChar;
                        estado = 32;
                    }else if(isL_BRACE(currentChar)) {
                        term += currentChar;
                        estado = 33;
                    }else if(isR_BRACE(currentChar)) {
                        term += currentChar;
                        estado = 34;
                    }else if(isSEMICOLON(currentChar)) {
                        term += currentChar;
                        estado = 35;
                    }else if(isCOMMA(currentChar)) {
                        term += currentChar;
                        estado = 36;
                    }else if(currentChar == '"') {
                        term += currentChar;
                        estado = 14;
                    }
                    else {
                        back();
                        token = new Token();
                        token.setType(Token.ERROR);
                        token.setText(term);
                        return token;
                    }
                    break;
                case 1: 
                    if(isID(currentChar) || isNUM(currentChar)) {
                        term += currentChar;
                        estado = 1;
                    }else if (isSpace(currentChar) 
                                || isOP_ARITHMETIC(currentChar)
                                || isCOMMA(currentChar)
                                || isSEMICOLON(currentChar)
                                || isL_BRACE(currentChar)
                                || isR_BRACE(currentChar)
                                || isL_PAREN(currentChar)
                                || isR_PAREN(currentChar)) {
                        back();
                        token = new Token();
                        token.setType(Token.ID);
                        token.setText(term);
                        return token;
                    }else {
                        back();
                        token = new Token();
                        token.setType(Token.ERROR);
                        token.setText(term);
                        return token;
                    }
                    break; 
                case 3:
                    if(isNUM(currentChar)) {
                        term += currentChar;
                        estado = 3;
                    }else if(isID(currentChar) == false) {
                        back();
                        token = new Token();
                        token.setType(Token.NUM);
                        token.setText(term);
                        return token;
                    }else {
                        back();
                        token = new Token();
                        token.setType(Token.ERROR);
                        token.setText(term);
                        return token;
                    }
                    break;

                case 8:
                    if(currentChar == 't') {
                        term += currentChar;
                        estado = 9;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 9:
                    if(currentChar == 'r') {
                        term += currentChar;
                        estado = 10;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 10:
                    if(currentChar == 'i') {
                        term += currentChar;
                        estado = 11;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 11:
                    if(currentChar == 'n') {
                        term += currentChar;
                        estado = 12;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 12:
                    if(currentChar == 'g') {
                        term += currentChar;
                        estado = 13;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 13:
                    if (isSpace(currentChar) 
                        || isOP_ARITHMETIC(currentChar)
                        || isCOMMA(currentChar)
                        || isSEMICOLON(currentChar)
                        || isL_BRACE(currentChar)
                        || isR_BRACE(currentChar)
                        || isL_PAREN(currentChar)
                        || isR_PAREN(currentChar)) {
                        back();
                        token = new Token();
                        token.setType(Token.STRING);
                        token.setText(term);
                        return token;
                    }else {
                        term += currentChar;
                        estado = 1;
                    }
                    break;
                
                case 14: 
                    if(isID(currentChar) || isNUM(currentChar) || isSpace(currentChar)) {
                        term += currentChar;
                        estado = 14;
                    }else if(currentChar == '"') {
                        term += currentChar;
                        estado = 15;
                    }else {
                        back();
                        token = new Token();
                        token.setType(Token.ERROR);
                        token.setText(term);
                        return token;
                    }
                    break;
                case 15: 
                    if (isSpace(currentChar) 
                        || isOP_ARITHMETIC(currentChar)
                        || isCOMMA(currentChar)
                        || isSEMICOLON(currentChar)
                        || isL_BRACE(currentChar)
                        || isR_BRACE(currentChar)
                        || isL_PAREN(currentChar)
                        || isR_PAREN(currentChar)) {
                    back();
                    token = new Token();
                    token.setType(Token.WORD);
                    token.setText(term);
                    return token;
                    }else {
                        back();
                        token = new Token();
                        token.setType(Token.ERROR);
                        token.setText(term);
                        return token;
                    }

                case 17:
                    if(currentChar == 'l') {
                        term += currentChar;
                        estado = 18;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 18:
                    if(currentChar == 's') {
                        term += currentChar;
                        estado = 19;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;
                case 19:
                    if(currentChar == 'e') {
                        term += currentChar;
                        estado = 20;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 20:
                    if (isSpace(currentChar) 
                        || isOP_ARITHMETIC(currentChar)
                        || isCOMMA(currentChar)
                        || isSEMICOLON(currentChar)
                        || isL_BRACE(currentChar)
                        || isR_BRACE(currentChar)
                        || isL_PAREN(currentChar)
                        || isR_PAREN(currentChar)) {
                        back();
                        token = new Token();
                        token.setType(Token.ELSE);
                        token.setText(term);
                        return token;
                    }else {
                        term += currentChar;
                        estado = 1;
                    }
                    break;
                    
                case 21:
                    if(currentChar == 'e') {
                        term += currentChar;
                        estado = 22;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 22: 
                    if(currentChar == 't') {
                        term += currentChar;
                        estado = 23;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;
                case 23: 
                    if(currentChar == 'u') {
                        term += currentChar;
                        estado = 24;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;
                case 24:
                    if(currentChar == 'r') {
                        term += currentChar;
                        estado = 25;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;
                case 25:
                    if(currentChar == 'n') {
                        term += currentChar;
                        estado = 26;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;
                case 26:
                    if (isSpace(currentChar) 
                        || isOP_ARITHMETIC(currentChar)
                        || isCOMMA(currentChar)
                        || isSEMICOLON(currentChar)
                        || isL_BRACE(currentChar)
                        || isR_BRACE(currentChar)
                        || isL_PAREN(currentChar)
                        || isR_PAREN(currentChar)) {
                        back();
                        token = new Token();
                        token.setType(Token.RETURN);
                        token.setText(term);
                        return token;
                    }else {
                        term += currentChar;
                        estado = 1;
                    }
                    break;
                
                case 27:
                    if(currentChar == 'f') {
                        term += currentChar;
                        estado = 28;
                    }else if(currentChar == 'n') {
                        term += currentChar;
                        estado = 29;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 28:
                    if (isSpace(currentChar) 
                        || isOP_ARITHMETIC(currentChar)
                        || isCOMMA(currentChar)
                        || isSEMICOLON(currentChar)
                        || isL_BRACE(currentChar)
                        || isR_BRACE(currentChar)
                        || isL_PAREN(currentChar)
                        || isR_PAREN(currentChar)) {
                        back();
                        token = new Token();
                        token.setType(Token.IF);
                        token.setText(term);
                        return token;
                    }else {
                        term += currentChar;
                        estado = 1;
                    }
                    break;

                case 29:
                    if(currentChar == 't') {
                        term += currentChar;
                        estado = 30;
                    }else {
                        if(checkID(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.ID);
                            token.setText(term);
                            return token;
                        }else {
                            term += currentChar;
                            estado = 1;
                        }
                    }
                    break;

                case 30:
                    if (isSpace(currentChar) 
                        || isOP_ARITHMETIC(currentChar)
                        || isCOMMA(currentChar)
                        || isSEMICOLON(currentChar)
                        || isL_BRACE(currentChar)
                        || isR_BRACE(currentChar)
                        || isL_PAREN(currentChar)
                        || isR_PAREN(currentChar)) {
                        back();
                        token = new Token();
                        token.setType(Token.INT);
                        token.setText(term);
                        return token;
                    }else {
                        term += currentChar;
                        estado = 1;
                    }
                    break;
                    
                case 31:
                    back();
                    token = new Token();
                    token.setType(Token.L_PAREN);
                    token.setText(term);
                    return token;

                case 32:
                    back();
                    token = new Token();
                    token.setType(Token.R_PAREN);
                    token.setText(term);
                    return token;    
                
                case 33:
                    back();
                    token = new Token();
                    token.setType(Token.L_BRACE);
                    token.setText(term);
                    return token;
                
                case 34:
                    back();
                    token = new Token();
                    token.setType(Token.R_BRACE);
                    token.setText(term);
                    return token;
                    
                case 35:
                    back();
                    token = new Token();
                    token.setType(Token.SEMICOLON);
                    token.setText(term);
                    return token;

                case 36:
                    back();
                    token = new Token();
                    token.setType(Token.COMMA);
                    token.setText(term);
                    return token;
                
                case 37:
                    if(currentChar == '=') {
                        term += currentChar;
                        estado = 38;
                    }
                    else {
                        back();
                        token = new Token();
                        token.setType(Token.ERROR);
                        token.setText(term);
                        return token;
                    }
                    break;

                case 38:
                    if (isSpace(currentChar) 
                        || isOP_ARITHMETIC(currentChar)
                        || isCOMMA(currentChar)
                        || isSEMICOLON(currentChar)
                        || isL_BRACE(currentChar)
                        || isR_BRACE(currentChar)
                        || isL_PAREN(currentChar)
                        || isR_PAREN(currentChar)) {
                        back();
                        token = new Token();
                        token.setType(Token.OP_RELATIONAL);
                        token.setText(term);
                        return token;
                    }else {
                        back();
                        token = new Token();
                        token.setType(Token.ERROR);
                        token.setText(term);
                        return token;
                    }
                
                case 40:
                    if(currentChar == '=') {
                        term += currentChar;
                        estado = 38;
                    }else {
                        if (isSpace(currentChar)) {
                            back();
                            token = new Token();
                            token.setType(Token.OP_RELATIONAL);
                            token.setText(term);
                            return token;
                        }else {
                            back();
                            token = new Token();
                            token.setType(Token.ERROR);
                            token.setText(term);
                            return token;
                        }
                    }
                
                case 41:
                if(currentChar == '=') {
                    term += currentChar;
                    estado = 38;
                }else {
                    if (isSpace(currentChar) || isID(currentChar)) {
                        back();
                        token = new Token();
                        token.setType(Token.OP_ARITHMETIC);
                        token.setText(term);
                        return token;
                    }else {
                        back();
                        token = new Token();
                        token.setType(Token.ERROR);
                        token.setText(term);
                        return token;
                    }
                }

            }   
        }
    }

    private boolean isNUM(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isID(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isOP_ARITHMETIC(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '=';
    }

    private boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }
    
    private boolean isCOMMA(char c) {
        return c == ',';
    }

    private boolean isSEMICOLON(char c) {
        return c == ';';
    }

    private boolean isR_BRACE(char c) {
        return c == '}';
    }

    private boolean isL_BRACE(char c) {
        return c == '{';
    }

    private boolean isR_PAREN(char c) {
        return c == ')';
    }

    private boolean isL_PAREN(char c) {
        return c == '(';
    }

    private char nextChar() {
        if(isEOF() == true) {
            return 'ε';
        }
        return content[pos++];
    }

    private boolean isEOF() {
        return pos == content.length;
    }

    private void back() {
        pos--;
    }

    public boolean checkID(char currentChar) {
        if (isSpace(currentChar) 
            || isOP_ARITHMETIC(currentChar)
            || isCOMMA(currentChar)
            || isSEMICOLON(currentChar)
            || isL_BRACE(currentChar)
            || isR_BRACE(currentChar)
            || isL_PAREN(currentChar)
            || isR_PAREN(currentChar)) {
            return true;
            
        }else {
            return false;
        }        
    }
}  

