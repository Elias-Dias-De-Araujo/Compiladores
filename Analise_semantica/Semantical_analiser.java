import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Semantical_analiser {
    private int max;
    private int current = 1;
    private int s, subs = 0;
    private String row;
    private int currentScope = 0;
    private boolean openedFunction = false;
    private boolean functionWasOpenned = false;
    private int cp = 0;
    private int cb = 0;
    private ArrayList<String>cutRow = new ArrayList<String>();
    private String currentFunction = "";
    private boolean scopeFunction = false;
    private boolean scopeScope = false;
    private HashMap<String, ArrayList<String>> tableFunction = new HashMap<String, ArrayList<String>>();
    private ArrayList<HashMap<String, ArrayList<String>>> scopes = new ArrayList<HashMap<String, ArrayList<String>>>();
    private HashMap<String, ArrayList<String>> tableVarScope = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> tableVarFunction = new HashMap<String, ArrayList<String>>();
    private HashMap<String, ArrayList<String>> tableVar = new HashMap<String, ArrayList<String>>();
    Pattern patternInt = Pattern.compile("[0-9]");
    Pattern patternID = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*");

    public Semantical_analiser() {
        scopes.add(tableVar);
    }

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

    public boolean analiser() {
        while(true) {
            if(checkEndOfRow()) {
                return true;
            }

            if(cutRow.get(current).equals(" ")) {
                return false;
            }

            if(!checkVariable(currentScope)) {
                return false;
            }
        }
    }

    public Boolean checkFunction() {
        return true;
    }

    public Boolean checkVariable(int scope) {
        boolean existVarReturn = false;
        HashMap<String, ArrayList<String>> nTVS = new HashMap<String, ArrayList<String>>();
        ArrayList<String> newSymbol = new ArrayList<String>();
        if(cutRow.get(current).equals("")) {
            current+=1;
        }else if(cutRow.get(current).equals("return")){
            while(true) {
                current+=1;
                if(cutRow.get(current).equals(" ")) {
                    break;
                }
                if(functionWasOpenned) {
                    if(cutRow.get(current).equals("(")) {
                        cp+=1;
                    }else if(cutRow.get(current).equals(")")) {
                        cp-=1;
                    }

                    if(cp == 0) {
                        functionWasOpenned = false;
                    }

                }else if(
                    !cutRow.get(current).equals("+") &&
                    !cutRow.get(current).equals("-") &&
                    !cutRow.get(current).equals("*") &&
                    !cutRow.get(current).equals("/") &&
                    !cutRow.get(current).equals(",") &&
                    !cutRow.get(current).equals("(") &&
                    !cutRow.get(current).equals(")") &&
                    !cutRow.get(current).equals(";") 
                    ) {
                        Matcher matcherInt = patternInt.matcher(cutRow.get(current));
                        boolean matchFoundInt = matcherInt.find();

                        Matcher matcherId = patternID.matcher(cutRow.get(current));
                        boolean matchFoundId = matcherId.find();

                        char[] charBegin = cutRow.get(current).toCharArray();

                        if(matchFoundId) {
                            for (int i = scope; i >= 0; i--) {
                                if(scopes.get(i).containsKey(cutRow.get(current))) {
                                    if(scopes.get(i).get(cutRow.get(current)).get(1).equals("int") 
                                        || scopes.get(i).get(cutRow.get(current)).get(1).equals("string") ) {
                                        existVarReturn = true;
                                        break;
                                    }
                                }
                            }
                        }else if (matchFoundInt) {
                            existVarReturn = true;
                        }else if (charBegin[0] == '"') {
                            existVarReturn = true;
                        }

                        if(!existVarReturn) {
                            return false;
                        }
                        if(cutRow.get(current+1).equals("(")) {
                            cp+=1;
                            current+=1;
                            functionWasOpenned = true;
                        }   
                        existVarReturn = false;
                }
            }
        }else if(cutRow.get(current).equals("{") && cutRow.get(current-1).equals(")")) {
            current+=1;
        }else if(cutRow.get(current).equals(")") && cutRow.get(current+1).equals("{")) {
            current+=2;
        }else if(cutRow.get(current).equals("int") && cutRow.get(current+2).equals("(")) {
            if(openedFunction) {
                return false;
            }else if(currentScope > 0) {
                return false;
            }else {
                for ( String key : scopes.get(0).keySet()) {
                    if(key.equals(cutRow.get(current+1)) && scopes.get(0).get(key).get(3).equals(Integer.toString(0))){
                        return false;
                    }
                }
                newSymbol.add(cutRow.get(current+1));
                newSymbol.add(cutRow.get(current));
                newSymbol.add(Integer.toString(scope));
                newSymbol.add("funcao");
                scopes.get(0).put(cutRow.get(current+1), newSymbol);

                cb+=1;
                openedFunction = true;
                scopes.add(nTVS);
                currentScope+=1;
                current+=3;
            }

        }else if(cutRow.get(current).equals("string") && cutRow.get(current+2).equals("(")) {
            if(openedFunction) {
                return false;
            }else if(currentScope > 0) {
                return false;
            }else {
                for ( String key : scopes.get(0).keySet()) {
                    if(key.equals(cutRow.get(current+1)) && scopes.get(0).get(key).get(3).equals(Integer.toString(0))){
                        return false;
                    }
                }

                newSymbol.add(cutRow.get(current+1));
                newSymbol.add(cutRow.get(current));
                newSymbol.add(Integer.toString(scope));
                newSymbol.add("funcao");
                scopes.get(0).put(cutRow.get(current+1), newSymbol);

                cb+=1;
                openedFunction = true;
                scopes.add(nTVS);
                currentScope+=1;
                current+=3;
            }

        }else if(cutRow.get(current).equals("}")){
            cb-=1;

            if(cb < 0) {
                return false;
            }
            if(currentScope == 1) {
                openedFunction = false;
            }

            scopes.remove(scopes.size() - 1);
            currentScope = currentScope-1;
            current+=1;
            
        }else if(cutRow.get(current).equals("{")) {
            current+=1;
            cb+=1;
            scopes.add(nTVS);
            currentScope+=1;
        }
        else if(cutRow.get(current).equals("if")) {
            current+=2;
            if(!getCheckVariableAlone(scope)) {
                return false;
            }
            current+=2;
            if(!getCheckVariableAlone(scope)) {
                return false;
            }
            current+=3;
            cb+=1;
            scopes.add(nTVS);
            currentScope+=1;
        }else if(cutRow.get(current).equals("else") && cutRow.get(current).equals("if")){
            current+=2;
            if(!getCheckVariableAlone(scope)) {
                return false;
            }
            current+=2;
            if(!getCheckVariableAlone(scope)) {
                return false;
            }
            current+=3;
            cb+=1;
            scopes.add(nTVS);
            currentScope+=1;
        }else if(cutRow.get(current).equals("else")) {
            current+=3;
            cb+=1;
            scopes.add(nTVS);
            currentScope+=1;
        }else if(cutRow.get(current).equals("int") && (cutRow.get(current+2).equals(";") || cutRow.get(current+2).equals("=") || cutRow.get(current+2).equals(",") || cutRow.get(current+2).equals(")"))) {
            for ( String key : scopes.get(scope).keySet()) {
                if(key.equals(cutRow.get(current+1)) && scopes.get(scope).get(key).get(3).equals(Integer.toString(scope))){
                    return false;
                }
            }
            if(cutRow.get(current+2).equals("=")) {
                boolean existInTableWithType = false;
                int aux = current;
                aux += 3;
                while(true) {
                    if(functionWasOpenned) {
                        if(cutRow.get(aux).equals("(")) {
                            cp+=1;
                        }else if(cutRow.get(aux).equals(")")) {
                            cp-=1;
                        }
    
                        if(cp == 0) {
                            functionWasOpenned = false;
                        }
                        aux+=1;
                    }else if(
                        !cutRow.get(aux).equals("+") &&
                        !cutRow.get(aux).equals("-") &&
                        !cutRow.get(aux).equals("*") &&
                        !cutRow.get(aux).equals("/") &&
                        !cutRow.get(aux).equals(",") &&
                        !cutRow.get(aux).equals("(") &&
                        !cutRow.get(aux).equals(")") 
                        ) {
                            Matcher matcher = patternInt.matcher(cutRow.get(aux));
                            boolean matchFound = matcher.find();
                            if(matchFound) {
                                if(cutRow.get(aux+1).equals(";") || cutRow.get(aux+1).equals(" ")) {
                                    newSymbol.add(cutRow.get(current+1));
                                    newSymbol.add(cutRow.get(current));
                                    newSymbol.add(Integer.toString(scope));
                                    newSymbol.add("variavel");

                                    scopes.get(scope).put(cutRow.get(current+1), newSymbol);

                                    break;
                                }else {
                                    aux+=1;
                                }
                                
                            }else {
                                Matcher matcher1 = patternID.matcher(cutRow.get(aux));
                                boolean matchFound1 = matcher1.find();
                                if(matchFound1) {
                                    for (int i = scope; i >= 0; i--) {
                                        if(scopes.get(i).containsKey(cutRow.get(aux))) {
                                            if(scopes.get(i).get(cutRow.get(aux)).get(1).equals("int")) {
                                                existInTableWithType = true;
                                                break;
                                            }else {
                                                existInTableWithType = false;
                                            }
                                        }else {
                                            existInTableWithType = false;
                                        }
                                    }
                                }
                                if(existInTableWithType) {
                                    if(cutRow.get(aux+1).equals(";") || cutRow.get(aux+1).equals(" ")) {
                                        newSymbol.add(cutRow.get(current+1));
                                        newSymbol.add(cutRow.get(current));
                                        newSymbol.add(Integer.toString(scope));
                                        newSymbol.add("variavel");
    
                                        scopes.get(scope).put(cutRow.get(current+1), newSymbol);
    
                                        break;
                                    }else {
                                        if(cutRow.get(aux+1).equals("(")) {
                                            functionWasOpenned = true;
                                        }   
                                        aux+=1;
                                    }
                                }else {
                                    return false;
                                }
                            }
                    }else {
                        if(cutRow.get(aux).equals("(")) {
                            cp+=1;
                        }else if(cutRow.get(aux).equals(")")) {
                            cp-=1;
                        }
                        aux+=1;
                    }
                }
                current = aux+2;
            }else if(cutRow.get(current+2).equals(";") 
                        || cutRow.get(current+2).equals(",")
                        || cutRow.get(current+2).equals(")")
                        || cutRow.get(current+2).equals(" ")) {
                newSymbol.add(cutRow.get(current+1));
                newSymbol.add(cutRow.get(current));
                newSymbol.add(Integer.toString(scope));
                newSymbol.add("variavel");

                scopes.get(scope).put(cutRow.get(current+1), newSymbol);

                current+=3;
            }else {
                return false;
            } 
        }else if(cutRow.get(current).equals("string") && (cutRow.get(current+2).equals(";") || cutRow.get(current+2).equals("=") || cutRow.get(current+2).equals(",") || cutRow.get(current+2).equals(")"))) {
            for ( String keySub : scopes.get(scope).keySet()) {
                if(keySub.equals(cutRow.get(current+1)) && scopes.get(scope).get(keySub).get(3).equals(Integer.toString(scope))){
                    return false;
                }
            }
            if(cutRow.get(current+2).equals("=")) {
                char[] charBegin = cutRow.get(current+3).toCharArray();
                if(charBegin[0] == '"' && charBegin[charBegin.length-1] == '"') {
                    newSymbol.add(cutRow.get(current+1));
                    newSymbol.add(cutRow.get(current));
                    newSymbol.add(Integer.toString(scope));
                    newSymbol.add("variavel");

                    scopes.get(scope).put(cutRow.get(current+1), newSymbol);

                    
                    current+=5;
                }else {
                    boolean existInTableWithType = false;
                    for (int i = scope; i >= 0; i--) {
                        if(scopes.get(i).containsKey(cutRow.get(current+3))) {
                            if(scopes.get(i).get(cutRow.get(current+3)).get(1).equals("string")) {
                                existInTableWithType = true;
                                break;
                            }
                        }
                    }
                    if(existInTableWithType) {
                        int aux = current;
                        if(cutRow.get(current+4).equals("(")) {
                            functionWasOpenned = true;
                            aux+=4;
                            while(true) {
                                if(functionWasOpenned) {
                                    if(cutRow.get(aux).equals("(")) {
                                        cp+=1;
                                    }else if(cutRow.get(aux).equals(")")) {
                                        cp-=1;
                                    }
                
                                    if(cp == 0) {
                                        functionWasOpenned = false;
                                    }
                                    aux+=1;
                                }else {
                                    break;
                                }
                            }
                        }
                        if(cutRow.get(aux).equals(";") || cutRow.get(aux).equals(" ")) {
                            newSymbol.add(cutRow.get(current+1));
                            newSymbol.add(cutRow.get(current));
                            newSymbol.add(Integer.toString(scope));
                            newSymbol.add("variavel");

                            scopes.get(scope).put(cutRow.get(current+1), newSymbol);

                            current+= aux + 1;
                        }else {
                            current+= aux + 1;
                            return false;
                        }
                    }else {
                        return false;
                    }
                }
            }else if(cutRow.get(current+2).equals(";") 
                    || cutRow.get(current+2).equals(",")
                    || cutRow.get(current+2).equals(")")
                    || cutRow.get(current+2).equals(" ")) {
                newSymbol.add(cutRow.get(current+1));
                newSymbol.add(cutRow.get(current));
                newSymbol.add(Integer.toString(scope));
                newSymbol.add("variavel");

                scopes.get(scope).put(cutRow.get(current+1), newSymbol);

                
                current+=3;
            }else {
                return false;
            }
        }else {
            if(!getCheckVariableAlone(scope)) {
                return false;
            }
        } 
        
        return true;
    }

    public boolean getCheckVariableAlone(int scope) {
        String keyToAnalise = "";
        String typeToAnalise = "";
        boolean existTableVar = false;
        for (int i = scope; i >= 0; i--) {
            for ( String key : scopes.get(i).keySet()) {
                for ( String keySub : scopes.get(i).keySet()) {
                    if(keySub.equals(cutRow.get(current)) && scopes.get(i).get(keySub).get(2).equals(Integer.toString(i))){
                        keyToAnalise = key;
                        typeToAnalise = scopes.get(i).get(keySub).get(1);
                        scope = i;
                        existTableVar = true;
                        break;
                    }
                }
            }
        }
        if(existTableVar) {
            if(!checkVariableAlone(keyToAnalise, typeToAnalise, scope)) {
                return false;
            }
        }else {
            return false;
        }
        return true;
    }

    public boolean checkVariableAlone(String keyToAnalise, String typeToAnalise, int scope) {
        if(typeToAnalise.equals("int")){
            if(cutRow.get(current+1).equals("=")) {
                boolean existInTableWithType = false;
                int aux = current;
                aux += 2;
                while(true) {
                    if(
                        !cutRow.get(aux).equals("+") &&
                        !cutRow.get(aux).equals("-") &&
                        !cutRow.get(aux).equals("*") &&
                        !cutRow.get(aux).equals("/") &&
                        !cutRow.get(aux).equals("(") &&
                        !cutRow.get(aux).equals(")") 
                        ) {
                            Matcher matcher = patternInt.matcher(cutRow.get(aux));
                            boolean matchFound = matcher.find();
                            if(matchFound) {
                                if(cutRow.get(aux+1).equals(";")) {
                                    break;
                                }else {
                                    aux+=1;
                                }
                                
                            }else {
                                if(scopes.get(scope).containsKey(cutRow.get(aux))) {
                                    if(scopes.get(scope).get(cutRow.get(aux)).get(1).equals("int")) {
                                        existInTableWithType = true;
                                        break;
                                    }
                                }
                                if(existInTableWithType) {
                                    if(cutRow.get(aux+1).equals(";")) {
                                        break;
                                    }else {
                                        aux+=1;
                                    }
                                }else {
                                    return false;
                                }
                            }
                    }else {
                        if(cutRow.get(aux).equals("(")) {
                            cp+=1;
                        }else if(cutRow.get(aux).equals(")")) {
                            cp-=1;
                        }
                        aux+=1;
                    }
                }
                current = aux+2;
            }
        }else if(typeToAnalise.equals("string")) {
            if(cutRow.get(current+1).equals("=")) {
                char[] charBegin = cutRow.get(current+2).toCharArray();
                if(charBegin[0] == '"' && charBegin[charBegin.length-1] == '"') {
                    current+=4;
                }else {
                    boolean existInTableWithType = false;
                    if(scopes.get(scope).containsKey(cutRow.get(current+2))) {
                        if(scopes.get(scope).get(cutRow.get(current+2)).get(1).equals("string")) {
                            existInTableWithType = true;
                        }
                    }
                    
                    if(existInTableWithType) {
                        if(cutRow.get(current+4).equals(";")) {
                            current+=4;
                        }else {
                            current+=4;
                            return false;
                        }
                    }else {
                        return false;
                    }
                }
            }else if(cutRow.get(current+2).equals(";")) {
                current+=2;
            }else {
                return false;
            }
        }

        return true;
    }

    public boolean checkEndOfRow() {
        if((current > max)) {
            current = 1;
            return true;
        }else {
            return false;
        }
    }

}


// Ordem na tabela de funções 
// Nome(0) | tipo(2) | escopo(3) | hasReturn(4) | typeReturn(5)

// Tabela da função atual
//

// Ordem na tabela de variáveis e funções 
// Nome(0) | tipo(1) | escopo(2) | caracteristica(3)