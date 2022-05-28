import java.util.ArrayList;

public class Transitions {
    private String transition;
    private ArrayList<Integer> states = new ArrayList<Integer>(); 

    public Transitions(String transition, int[] states) {
        this.setTransition(transition);
        this.setStates(states);
    }

    public String getTransition() {
        return this.transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public ArrayList<Integer> getStates() {
        return this.states;
    }

    public void setStates(int[] states) {
        for(int i = 0; i < states.length; i++) {
            this.states.add(states[i]);
        }
    }
}
