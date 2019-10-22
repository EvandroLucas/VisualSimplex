package lpp;


/*
*  This class represents a linear programming problem
*
* */

import numbers.Value;

import java.util.ArrayList;

public class LPP {


    private boolean solved = true;
    private Value[] solution;
    private Result result = Result.UNKNOWN;

    private ProblemType problemType;
    private ArrayList<Restriction> restrictions;
    private ArrayList<VariableRestriction> varRestrictions;
    private Value[] objFunction;
    private String objVar;


    public LPP (ProblemType problemType, ArrayList<Restriction> restrictions, ArrayList<VariableRestriction> varRestrictions, Value[] objFunction, String objVar){
        this.problemType = problemType;
        this.restrictions = restrictions;
        this.varRestrictions = varRestrictions;
        this.objFunction = objFunction;
        this.objVar = objVar;
    }

    // This will remove any redundancy between variable restrictions
    public void simplify(){
        for(){

        }
    }

    public RestrictionMatch matchCondition(VariableRestriction generalRestriction, VariableRestriction varRt){
        // Devemos montar Maps constantes dentro do enum, e usar a a tabela para comparar
        // É feio mas pelo menos é O(1)
    }



}
