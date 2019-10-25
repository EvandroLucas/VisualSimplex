package lpp;

public class Variable {

    private String name;
    private String group;
    private Integer index;

    public Variable(String group, Integer index){
        this.group = group;
        this.index = index;
        this.name = group + index;
    }
    public Variable(Variable var){
        this.name = var.name;
        this.group = var.group;
        this.index = var.index;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
        this.name = group + index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
        this.name = group + index;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
