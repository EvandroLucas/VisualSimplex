package lpp;

import numbers.Value;
import print.ColorPrint;

import java.util.*;

/*
 * This class encapsulates the symbolic representation of a Restriction
 * For example, if we have:
 *      +(1.1)y0 +(1.2)y1 +(1.3)y2 +(1.4)y3 +(0)y4 +(0)y5  + (1)s1<= 1
 *
 * We'll have a list of components:
 *  [+(1.1)y0, +(1.2)y1, ..., +(1)s1]
 *
 *  A list of groups:
 *  [y,s]
 *
 * Our values would be:
 *   right = 1
 *   rtt = RestrictionType.LessOrEqualThan
 * */

public class Restriction {

    public TreeSet<Component> components = new TreeSet<>();
    public Value right;
    public RestrictionType rtt;
    public HashMap<String, HashSet<Integer>> groups = new HashMap<>();


    public Restriction(Value[] leftSide, Value right, RestrictionType rtt, String group) {
        this.right = right;
        this.rtt = rtt;
        for(int i = 0; i < leftSide.length;i++){
            Component component = new Component(group,i,leftSide[i]);
            addComponent(component);
        }
    }

    public Restriction (LinkedHashSet<Component> components, Value right, RestrictionType rtt) {
        this.right = right;
        this.rtt = rtt;
        for(Component component : components){
            addComponent(component);
        }
    }

    public Restriction (Restriction rt){
        this.right = new Value(rt.right);
        this.rtt = rt.rtt;
        for(Component comp : rt.components){
            addComponent(comp);
        }
    }

    public void mult(Value v){
        for(Component component : components){
            component.setMultiplier(component.getMultiplier().mult(v));
        }
        right.assign(right.mult(v));
    }

    public void addComponent(Component cp) {
        this.components.add(new Component(cp));
        addVariable(cp);
    }
    public boolean addVariable(Variable var) {
        boolean changed = false;
        if(!groups.containsKey(var.getGroup())){
            groups.put(var.getGroup(),new HashSet<>());
            changed = true;
        }
        if(! groups.get(var.getGroup()).contains(var.getIndex())){
            groups.get(var.getGroup()).add(var.getIndex());
            changed = true;
        }
        return changed;
    }
    public void removeComponent(Component cp) {
        this.components.add(new Component(cp));
        removeVariable(cp);
    }
    public void removeVariable(Variable var) {
        groups.get(var.getGroup()).remove(var.getIndex());
    }
    public boolean hasVariable(Variable var) {
        boolean has = false;
        if(!groups.containsKey(var.getGroup())) return false;
        return groups.get(var.getGroup()).contains(var.getIndex());
    }



    @Override
    public String toString() {
       return toString(false);
    }

    public String toString(boolean full) {
        if (components.isEmpty()) return "Empty!";
        StringBuilder stringBuilder = new StringBuilder();
        for(Component component : components){
            stringBuilder.append(component.toString());
        }
        stringBuilder.append(" ")
                .append(ColorPrint.purple(rtt.toString()))
                .append(" ")
                .append(ColorPrint.blue(right.toString()));
        if(full){
            stringBuilder.append("       ")
                    .append(ColorPrint.green(groups.toString()));
        }
        return stringBuilder.toString();
    }

}
