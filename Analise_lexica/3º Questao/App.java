import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;


public class App {
    public static ArrayList<Integer> nfaInitialState = new ArrayList<Integer>();
    public static ArrayList<Integer> finalStates = new ArrayList<Integer>();
    public static Dictionary<Integer, Collections_transitions> nfa = new Hashtable<Integer, Collections_transitions>();

    public static void main(String[] args) throws Exception {

        // Aqui são inseridos os estados e transições do nfa
        nfa.put(0, new Collections_transitions(new Transitions[] {
            new Transitions("ε", new int[]{1,4})
        }));
        nfa.put(1, new Collections_transitions(new Transitions[] {
            new Transitions("i", new int[]{2}),
            //new Transitions("a", new int[]{2})
        }));
        nfa.put(2, new Collections_transitions(new Transitions[] {
            new Transitions("f", new int[]{3})
        }));
        nfa.put(3, new Collections_transitions(new Transitions[] {
            new Transitions("ε", new int[]{})
        }));
        nfa.put(4, new Collections_transitions(new Transitions[] {
            new Transitions("i", new int[]{5})
        }));
        nfa.put(5, new Collections_transitions(new Transitions[] {
            new Transitions("n", new int[]{6})
        }));
        nfa.put(6, new Collections_transitions(new Transitions[] {
            new Transitions("t", new int[]{7})
        }));
        nfa.put(7, new Collections_transitions(new Transitions[] {
            new Transitions("ε", new int[]{})
        }));

        // Aqui pode ser especificado o estado inicial do nfa
        nfaInitialState.add(0);
        
        // Aqui pode ser especificado o estado final do nfa, que pode ser mais de um
        finalStates.add(3);
        finalStates.add(7);

        convertToDfa();

    }   

    public static void convertToDfa() {
        int contTrans = 0;
        int j = 0;
        int p = 2;
        boolean cond = false;
        ArrayList<String> alphabet = new ArrayList<String>();
        alphabet.addAll(getAlphabet());
       
        Dictionary<Integer, ArrayList<Integer>> states = new Hashtable<Integer, ArrayList<Integer>>();
        Dictionary<Integer, Tuple> trans = new Hashtable<Integer, Tuple>();
        states.put(0, new ArrayList<Integer>());
        states.put(1, closure(nfaInitialState));

        while(j < p) {
            for(int c = 0; c < alphabet.size(); c++) {
                ArrayList<Integer> e = new ArrayList<Integer>();
                e.addAll(DFAEdge(states.get(j), alphabet.get(c)));
                cond = false;
                for(int i = 0; i < p; i++) {
                    if(e.equals(states.get(i))) {
                        trans.put(contTrans, new Tuple(j, alphabet.get(c), i));
                        contTrans += 1;
                        cond = true;
                        break;
                    }
                }
                if(cond == false) {
                    states.put(p, e);
                    trans.put(contTrans, new Tuple(j, alphabet.get(c), p));
                    contTrans += 1;
                    p += 1;
                }
            }
            j += 1;
        }


        System.out.println("----DFA----");
        System.out.println("Estado Inicial: " + states.get(1));
        String prinState = "";
        for(int i = 0; i< states.size(); i++) {
            for(int c = 0; c < finalStates.size(); c++) {
                if(states.get(i).contains(finalStates.get(c))) {
                    prinState += states.get(i) + " ";
                }
            }
        }
        System.out.println("Estados Finais: " + prinState + "\n");

        System.out.println("Estados do DFA: ");
        for(int i = 0; i< states.size(); i++) {
            if(i != 0) {
                System.out.println("index de estado: " + i + " - Estado  do DFA: " + states.get(i));
            }
        }

        System.out.println("Transições do DFA: ");
        for(int i = 0; i < trans.size(); i++) {
            if(i != 0) {
                if(trans.get(i).toString().equals("Nenhum") == false) {
                    System.out.println(trans.get(i).toString());
                }
            }
        }

        
    }

    public static ArrayList<Integer> DFAEdge(ArrayList<Integer> stateSet, String c) {
        ArrayList<Integer> listInt = new ArrayList<Integer>();
        for(int i = 0; i < stateSet.size(); i++) {
            ArrayList<Integer> ed = new ArrayList<Integer>();
            ArrayList<Integer> cl = new ArrayList<Integer>();
            ed.addAll(edge(stateSet.get(i), c));
            cl.addAll(closure(ed));
            for(int k = 0; k < cl.size(); k++) {
                listInt.add(cl.get(k));
            }
        }

        return listInt;
    }

    public static ArrayList<Integer> closure(ArrayList<Integer> stateSet) {
        ArrayList<Integer> listInt = new ArrayList<Integer>();
        listInt.addAll(stateSet);
        while(true) {
            ArrayList<Integer> listInt_aux = new ArrayList<Integer>();
            listInt_aux.addAll(listInt);
            listInt.clear();
            for(int i = 0; i < listInt_aux.size(); i++) {
                ArrayList<Integer> ed = new ArrayList<Integer>();
                ed.addAll(edge(listInt_aux.get(i), "ε"));
                if(listInt.size() <= listInt_aux.size()) {
                    listInt.clear();
                    listInt.addAll(listInt_aux);
                }
                for(int c = 0; c < ed.size(); c++) {
                    if(listInt.contains(ed.get(c)) == false) {
                        listInt.add(ed.get(c));
                    }
                }
            }
            
            if(listInt.equals(listInt_aux)) {
                break;
            }

        }

        return listInt;
    }

    public static ArrayList<Integer> edge(int state, String c) {
        String k;
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Transitions> t = new ArrayList<Transitions>();
        t.addAll(nfa.get(state).getTran());
        if(t.isEmpty() == false) {
            for(int i = 0; i < t.size(); i++) {
                k = t.get(i).getTransition();
                if(k.equals(c)) {
                    a.addAll(t.get(i).getStates());
                }
            }
        }

        return a;
    }


    public static ArrayList<String> getAlphabet() {
        String k;
        ArrayList<String> listString = new ArrayList<String>();
        for(int i = 0; i < nfa.size(); i++) {
            for(int c = 0; c < nfa.get(i).getTran().size(); c++) {
                k = nfa.get(i).getTran().get(c).getTransition();
                if(listString.contains(k) == false) {
                    listString.add(k);
                }
            }
        }
        if(listString.contains("ε")) {
            for(int i = 0; i < listString.size(); i++) {
                if(listString.get(i) == "ε") {
                    listString.remove(i);
                }
            }
        }
        return listString;
    }
}
