package lpp;

public class Variable implements Comparable{

    private String group;
    private Integer index = 0;

    public Variable(String group, Integer index){
        this.group = group;
        this.index += index;
    }
    public Variable(Variable var){
        this.group = var.group;
        this.index += var.index;
    }

    public String getName() {
        return group + index;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Variable){
            Variable v = (Variable) o;
            if(this.getIndex() > ((Variable) o).getIndex()){
                return 1;
            }
            else if(this.getIndex() < ((Variable) o).getIndex()){
                return -1;
            }
        }
        return 0;
    }
}
