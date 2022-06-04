import java.util.HashMap;

public class firstFollow {
    public static String initSymbol = "S"; // Simbolo inicial da gramática( alterar conforme gramática )
    public static String emptySymbol = "^"; // Simbolo de vazio
    public static HashMap<String, HashMap<String, Boolean>> groupFirst = new HashMap<String, HashMap<String, Boolean>>();
    public static HashMap<String, HashMap<String, Boolean>> groupFollow = new HashMap<String, HashMap<String, Boolean>>();
    public static HashMap<Integer, String> grammar = new HashMap<Integer, String>();

    public static void main(String[] args) {
        // Aqui insere-se as produções da gramática.
        grammar.put(0, "S->B $");
        grammar.put(1, "B->id P");
        grammar.put(2, "B->id ( E ]");
        grammar.put(3, "P->^");
        grammar.put(4, "P->( E )");
        grammar.put(5, "E->B");
        grammar.put(6, "E->B , E");
        
        for(int key = 0; key < grammar.size(); key++) {
            String[] splited = grammar.get(key).split("->");
            String s = splited[0].replace(" ", "");
            calcFirst(s, 0);
        }

        for(int key = 0; key < grammar.size(); key++) {
            String[] splited = grammar.get(key).split("->");
            String s = splited[0].replace(" ", "");
            calcFollow(s, 0);
        }
        
        System.out.println("First: ");
        int count = 0;
        for ( String key : groupFirst.keySet()) {
            if (!checkTerminal(key)) {
                String s = "{ ";
                for ( String keySubset : groupFirst.get(key).keySet()) {
                    if(count == 0) {
                        s += keySubset;
                    }else {
                        s += "  " + keySubset;
                    }
                    
                    count += 1;
                }
                s += " }";
                System.out.println(key + " : " + s);
                count = 0;
            }
        }

        System.out.println("Follow: ");
        int countFollow = 0;
        for ( String key : groupFollow.keySet()) {
            if (!checkTerminal(key)) {
                String s = "{ ";
                for ( String keySubset : groupFollow.get(key).keySet()) {
                    if(countFollow == 0) {
                        s += keySubset;
                    }else {
                        s += "  " + keySubset;
                    }
                    
                    countFollow += 1;
                }
                s += " }";
                System.out.println(key + " : " + s);
                countFollow = 0;
            }
        }
    }

    public static String fullWord(String s, int index) {
        String lastSide = lastSideProduction(grammar.get(index));
        char[] contentLastSide = lastSide.toCharArray();
        int currentIndex = 0;
        char current;
        for(int i = 0; i < contentLastSide.length; i++) {
            if(contentLastSide[i] == s.charAt(0)){
                currentIndex = i;
                break;
            }
        }
        String response = s;

        for(int x = currentIndex; x < contentLastSide.length; x++){
            try {
                current = contentLastSide[x+1];
            } catch (Exception e) {
                return response;
            }
            if(!Character.isUpperCase(current)){
                response+=current;
            }else {
                break;
            }
        }
        return response;
    }

    public static HashMap<String, Boolean> calcFirst(String s, int index) {
        if(groupFirst.containsKey(s)) {
            return groupFirst.get(s);
        }

        HashMap<String, Boolean> first = new HashMap<String, Boolean>();
        groupFirst.put(s, first);

        if(checkTerminal(s)) {
            String sym = fullWord(s, index);
            first.put(sym, true);
            groupFirst.put(s, first);
            return groupFirst.get(s);
        }

        HashMap<Integer, String> pf = pfs(s);
        for ( Integer key : pf.keySet()) {
            String prod = lastSideProduction(pf.get(key));
            char[] contentProd = prod.toCharArray();

            for(int i = 0; i < contentProd.length; i++) {
                String ps = String.valueOf(contentProd[i]);
                if(ps.equals(emptySymbol)) {
                    first.put(emptySymbol, true);
                    groupFirst.put(s, first);
                    break;
                }

                HashMap<String, Boolean> firstOfNonTerminal = calcFirst(ps, key);
                if(!firstOfNonTerminal.containsKey(emptySymbol)) {
                    union(first, firstOfNonTerminal , "");
                    groupFirst.put(s, first);
                    break;
                }

                union(first, firstOfNonTerminal, emptySymbol);
                groupFirst.put(s, first);
            }
        
        }
        return first;
    }

    public static HashMap<String, Boolean> calcFollow(String s, int index) {
        if(groupFollow.containsKey(s)) {
            return groupFollow.get(s);
        }

        HashMap<String, Boolean> follow = new HashMap<String, Boolean>();
        groupFollow.put(s, follow);

        if(s.equals(initSymbol)) {
            follow.put("$", true);
            groupFollow.put(s, follow);
        }
        
        HashMap<Integer, String> pw = pws(s);
        for ( Integer p : pw.keySet()) {
            String prod = lastSideProduction(pw.get(p));
            char[] lastSide = prod.toCharArray();
            int sIndex = 0;
            int fIndex = 0;
            for(int i = 0; i < lastSide.length; i++) {
                if(String.valueOf(lastSide[i]).equals(s)) {
                    sIndex = i;
                    break;
                }
            }
            fIndex = sIndex + 1;

            while(true) {
                if(fIndex == lastSide.length) {
                    String lhs = firstSideProduction(pw.get(p));
                    if(!lhs.equals(s)) {
                        union(follow, calcFollow(lhs, index), "");
                        groupFollow.put(s, follow);
                    }
                    break;
                }

                String fs = String.valueOf(lastSide[fIndex]);
                HashMap<String, Boolean> firstFollow = calcFirst(fs, p);
                
                if(!firstFollow.containsKey(emptySymbol)) {
                    union(follow, firstFollow, "");
                    groupFollow.put(s, follow);
                    break;
                }

                union(follow, firstFollow, emptySymbol);
                groupFollow.put(s, follow);
                fIndex += 1;
            }
        }
        return follow;
    }

    public static HashMap<Integer, String> pfs(String s) {
        String[] splitDerivation;
        HashMap<Integer, String> pf = new HashMap<Integer, String>();
        for(int i = 0; i < grammar.size(); i++) {
            splitDerivation = grammar.get(i).split("->");
            if(splitDerivation[0].equals(s)) {
                pf.put(i, grammar.get(i));
            }
        }
        return pf;
    }

    public static HashMap<Integer, String> pws(String s) {
        Boolean exist = false;
        String prod;
        HashMap<Integer, String> pw = new HashMap<Integer, String>();
        for(int i = 0; i < grammar.size(); i++) {
            prod = grammar.get(i);
            String lastSide = lastSideProduction(prod);
            char[] contentLastSide = lastSide.toCharArray();
            for(int j = 0; j < contentLastSide.length; j++) {
                if(contentLastSide[j] == s.charAt(0)){
                    exist = true;
                    break;
                }
            }
            if(exist) {
                pw.put(i, prod);
            }
            exist = false;
        }
        return pw;
    }

    public static String firstSideProduction(String p) {
        String[] splited = p.split("->");
        String line = splited[0].replace(" ", "");
        return line;
    }

    public static String lastSideProduction(String p) {
        String[] splited = p.split("->");
        String line = splited[1].replace(" ", "");
        return line;
    }

    public static Boolean checkTerminal(String s) {
        if(Character.isUpperCase(s.charAt(0))){
            return false;
        }else {
            return true;
        }
    }

    public static void union(HashMap<String, Boolean> last, HashMap<String, Boolean> initial, String notIn) {
        for ( String key : initial.keySet()) {
            if(!key.equals(notIn)) {
                last.put(key, initial.get(key));
            }
        }
    }


}