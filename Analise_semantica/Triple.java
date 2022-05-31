public class Triple {
    private boolean exist;
    private String type;
    private String caracteristic;

    public Triple(boolean exist, String type, String caracteristic) {
        this.exist = exist;
        this.type = type;
        this.caracteristic = caracteristic;
    }

    public boolean getExist() {
        return this.exist;
    }

    public String getType() {
        return this.type;
    }

    public String getCaracteristic() {
        return this.caracteristic;
    }

}
