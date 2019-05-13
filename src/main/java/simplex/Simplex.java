package simplex;


import logging.Logger;
import numbers.Value;
import print.CustomPrinter;


public abstract class Simplex {

    public Tableau tableau;

    public boolean isOptimal = false;
    public boolean isUnbounded = false;
    public boolean isInfeasible = false;

    protected int constraintNum = 0;
    protected int variableNum = 0;

    protected boolean done = false;

    protected CustomPrinter printer = new CustomPrinter();

    protected Simplex (Value[][] tableauInput){

        constraintNum = tableauInput.length-1;
        variableNum = tableauInput[0].length-1;
        tableau = new Tableau(tableauInput,true);
        printer.setState("Setup");
        printer.setDesc("Received simplex to solve");
        printer.printTableau(tableau.A,tableau.c,tableau.b,tableau.z,tableau.basis);

    }

    protected Simplex (Tableau tableau){
        constraintNum = tableau.A.length;
        variableNum = tableau.A[0].length;
        this.tableau = tableau;
        printer.setState("Setup");
        printer.setDesc("Received simplex to solve");
        printer.printTableau(this.tableau .A,this.tableau .c,this.tableau .b,this.tableau .z,this.tableau .basis);
    }

    public abstract void run();


    protected void pivot(){
        //printer.setDesc("Before pivoting");
        //printer.printTableau(tableau.A,tableau.c,tableau.b,tableau.z,tableau.basis);
        pivot(this.tableau);
        //printer.setDesc("After pivoting");
        //printer.printTableau(tableau.A,tableau.c,tableau.b,tableau.z,tableau.basis);
    }

    protected void pivot(Tableau tableau, boolean conserveBasis){

        printer.setDesc("Before pivoting");
        printer.printTableau(tableau);


        int pivotRowIndex = tableau.pivotRowIndex;
        int pivotColumnIndex = tableau.pivotColumnIndex;

        Value[][] A = tableau.A;
        boolean[] basis;

        if(conserveBasis){
            basis = new boolean[tableau.basis.length];
            for(int i = 0; i < basis.length; i++){
                basis[i] = tableau.basis[i];
            }
        }else {
            basis = tableau.basis;
        }


        Logger.println("info","Pivoting!");

        Value pivot = new Value(0); //guarda o pivot
        Value mult = new Value(0);  //multiplicador


        //updating basis
        pivot.assign(A[pivotRowIndex][pivotColumnIndex]);
        for (int j = 0; j < A[pivotRowIndex].length; j++) {
            if ((A[pivotRowIndex][j].isEqualTo(1)) && (basis[j])) {
                basis[j] = false;
            }
        }
        basis[pivotColumnIndex] = true;


        //For the pivotal line
        for (int j = 0; j < A[pivotRowIndex].length; j++){
            A[pivotRowIndex][j].assign(A[pivotRowIndex][j].div(pivot));
        }
        tableau.b[pivotRowIndex].assign(tableau.b[pivotRowIndex].div(pivot));
        if(A[pivotRowIndex][pivotColumnIndex].isNotEqualTo(1) ){ //erro!
            Logger.println("severe","Erro no pivoteamento!");
            System.exit(1);
        }
        //No for the rest
        for (int i = 0; i < tableau.A.length; i++){ // fazemos a conta com todos os elementos da matriz
            mult.assign(tableau.A[i][pivotColumnIndex]);
            System.out.println("Mult is " + mult);
            if (i != pivotRowIndex){ //ignoramos a linha pivotal, ela ja foi
                //Para a matriz A e concatenada
                for (int j = 0; j < tableau.A[i].length; j++){
                    //System.out.print("" + A[i][j] + "is now "+ A[i][j] + " - " + A[pivotRowIndex][j] + "* " + mult + " = ");
                    tableau.A[i][j].assign(tableau.A[i][j].sub(tableau.A[pivotRowIndex][j].mult(mult)));
                    //System.out.println(" " + A[i][j]);
                }
                //Para o vetor b
                tableau.b[i].assign(tableau.b[i].sub(tableau.b[pivotRowIndex].mult(mult)));
                //Para a primeira linha do tableau
            }
            if(i==0){
                mult.assign(tableau.c[pivotColumnIndex]);
                //Para z
                tableau.z.assign(tableau.z.sub(tableau.b[pivotRowIndex].mult(mult)));
                //Para o vetor c
                for (int j = 0; j < tableau.c.length; j++){
                    //mult = c[pivotColumnIndex];
                    tableau.c[j].assign(tableau.c[j].sub(tableau.A[pivotRowIndex][j].mult(mult)));
                }
            }
        }

        printer.setDesc("After pivoting");
        printer.printTableau(tableau);


        Logger.println("info","Done pivoting!");
        //Logger.println("severe","Erro no pivoteamento!");
        //System.exit(1);

    }

    protected void pivot(Tableau tableau){
        pivot(tableau,false);
    }
    protected void printThisTableau(Tableau thisTableau){
        printer.printTableau(thisTableau);
    }
    protected void printThisTableau(){
        printer.printTableau(this.tableau);
    }


    protected abstract void updatePivotRow();
    protected abstract void updatePivotColumn();

    protected void printOptStatus(Tableau tableau){
        printer.setDesc("Certificate of optimalitty");
        generateCertOpt(tableau);
        printer.printCert(tableau.certOpt);
        printer.setDesc("Solution");
        generateSolution(tableau);
        printer.printCert(tableau.solution);
    }

    protected void printUnbStatus(Tableau tableau){
        printer.setDesc("Certificate of unboundness");
        generateCertUnb(tableau);
        printer.printCert(tableau.certUnb);
        printer.setDesc("Solution");
        generateSolution(tableau);
        printer.printCert(tableau.solution);
    }

    protected void printInfStatus(Tableau tableau){
        printer.setDesc("Certificate of infeasability");
        generateCertInf(tableau);
        printer.printCert(tableau.certInf);
    }

    protected void generateSolution(Tableau tableau){
        //preenche o vetor solucao
        for(int k=0; k<tableau.solution.length; k++){
            if(tableau.basis[k]){
                for(int i=0;i<tableau.b.length;i++){
                    if( tableau.A[i][k].isEqualTo(1)){
                        tableau.solution[k] = tableau.b[i];
                    }
                }
            }
        }
    }
    protected void generateCertOpt(Tableau tableau){
        for(int j = 0; j < tableau.certOpt.length; j++){
            tableau.certOpt[j].assign(tableau.c[j+tableau.numVar]);
        }
    }
    protected void generateCertUnb(Tableau tableau){
        for(int j=0;j < tableau.certUnb.length;j++){
            if(tableau.basis[j]){
                for(int i=0;i<tableau.A.length;i++){
                    if(tableau.A[i][j].isEqualTo(1)){
                        tableau.certUnb[j] = tableau.A[i][tableau.problematicColumnIndex].mult(-1);
                    }
                }
            }
        }
        tableau.certUnb[tableau.problematicColumnIndex].assign(1);
    }

    protected void generateCertInf(Tableau tableau){
        for(int j = 0; j < tableau.certInf.length ; j++){
            tableau.certInf[j].assign(tableau.c[j+tableau.numVar]);
        }
    }

}
