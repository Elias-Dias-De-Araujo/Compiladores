import java.util.ArrayList;

public class Collections_transitions {
    private ArrayList<Transitions> trans = new ArrayList<Transitions>(); 

    public Collections_transitions(Transitions[] trans) {
        this.setTran(trans);
    }

    public ArrayList<Transitions> getTran(){
        return this.trans;
    }

    public void setTran(Transitions[] trans){
        for(int i = 0; i < trans.length; i++) {
            this.trans.add(trans[i]);
        }
    }
}
