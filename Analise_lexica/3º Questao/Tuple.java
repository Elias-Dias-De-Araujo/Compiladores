public class Tuple {
    private int s;
    private String chara;
    private int valueReference;

    public Tuple(int s, String chara, int valueReference) {
        setS(s);
        setChara(chara);
        setValueReference(valueReference);
    }

    private int getValueReference() {
        return this.valueReference;
    }

    private void setValueReference(int valueReference) {
        this.valueReference = valueReference;
    }

    private int getS() {
        return this.s;
    }

    private void setS(int s) {
        this.s = s;
    }

    private String getChara() {
        return this.chara;
    }

    private void setChara(String chara) {
        this.chara = chara;
    }

    @Override
    public String toString() {
        if(this.getValueReference() == 0) {
            return "Nenhum";
        }else {
            return "( index de estado: " + this.getS() + ", lendo o caractere:  " + this.getChara() + " ) -> index de estado alcançável: " + this.getValueReference() + " ";
        }
        
    }
}
