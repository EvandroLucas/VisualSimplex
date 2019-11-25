package lpp;


/*
*  This class represents a linear programming problem
*
* */

import logging.Logger;
import logging.LoggerProvider;
import numbers.Value;
import print.ColorPrint;

import java.util.*;

public class LPP {

    protected Logger logger = LoggerProvider.getInstance().provide("LPP");

    public String mainGroup = "x";
    public boolean solved = true;
    public Value[] solution = new Value[0];
    public ResultType result = ResultType.UNKNOWN;

    public ProblemType problemType;
    public HashMap<String, HashSet<Integer>> groups = new HashMap<>();
    public ArrayList<Restriction> restrictions = new ArrayList<>();
    public ArrayList<VariableRestriction> varRestrictions = new ArrayList<>();
    public ArrayList<Component> objFunction = new ArrayList<>();
    public Integer numVar = 0;

    public LPP(LPP lpp){
        this.problemType = lpp.problemType;
        for(Map.Entry<String, HashSet<Integer>> entry : lpp.groups.entrySet()){
            this.groups.put(entry.getKey(), new HashSet<>());
            for(Integer index : lpp.groups.get(entry.getKey())){
                this.groups.get(entry.getKey()).add(index);
            }
        }
        for(Restriction rt: lpp.restrictions){
            this.restrictions.add(new Restriction(rt));
        }
        for(VariableRestriction vrt: lpp.varRestrictions){
            this.varRestrictions.add(new VariableRestriction(vrt));
        }
        for(Component cp: lpp.objFunction){
            this.objFunction.add(new Component(cp));
        }
        this.mainGroup = lpp.mainGroup;
        this.numVar += lpp.objFunction.size();
        this.solved = lpp.solved;
        this.solution = lpp.solution.clone();
        this.result = lpp.result;
    }


    public LPP (ProblemType problemType, ArrayList<Restriction> restrictions, ArrayList<VariableRestriction> varRestrictions, ArrayList<Component> objFunction, String mainGroup){
        this.problemType = problemType;
        this.restrictions = restrictions;
        this.varRestrictions = varRestrictions;
        this.objFunction = objFunction;
        this.mainGroup = mainGroup;
        this.numVar = objFunction.size();
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean full) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("========================================================\n\n");
        stringBuilder.append(problemType).append(" ");

        for (int i = 0; i < numVar; i++) {
            stringBuilder.append(ColorPrint.blue(objFunction.get(i).toString()));
        }
        stringBuilder.append(" \n").append("Such that:").append(" \n");
        for (Restriction rt : restrictions) {
            if(full)
                stringBuilder.append(rt.toString(true)).append(" \n");
            else
                stringBuilder.append(rt).append(" \n");
        }
        for (VariableRestriction vrt : varRestrictions)
            stringBuilder.append(vrt).append(" \n");
        stringBuilder.append("\n========================================================\n");
        return stringBuilder.toString();
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

    public void removeVariable(Variable var) {
        groups.get(var.getGroup()).remove(var.getIndex());
    }


    public void updateRestrictions(){
        logger.println("debug","Checking for updates...");
        // Checking changes on restrictions
        boolean hasChanged = false;
        for(Restriction rt : restrictions){
            for(Map.Entry<String, HashSet<Integer>> entry : rt.groups.entrySet()){
                String group = entry.getKey();
                for(Integer index : rt.groups.get(group)){
                    Variable var = new Variable(group,index);
                    hasChanged = addVariable(var) || hasChanged;
                }
            }
        }
        // Checking the objective function
        for(Component component : objFunction){
            addVariable(component);
        }
        hasChanged = true;
        // Updating
        if(hasChanged) {
            logger.println("debug","Changes detected! Updating restrictions...");
            for (Restriction rt : restrictions) {
                for (Map.Entry<String, HashSet<Integer>> entry : groups.entrySet()) {
                    String group = entry.getKey();
                    for (Integer index : groups.get(group)) {
                        Component cp = new Component(group, index, new Value());
                        if (!rt.hasVariable(cp)) {
                            rt.addComponent(cp);
                        }
                    }
                }
            }
        }
    }

    public Integer numberOfVariables(){
        int count = 0;
        for (Map.Entry<String,HashSet<Integer>> entry : groups.entrySet()){
            count += entry.getValue().size();
        }
        return count;
    }
    public Integer numberOfRestrictions(){
        return restrictions.size();
    }


}
