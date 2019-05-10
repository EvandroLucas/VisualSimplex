package simplex;


import logging.Logger;
import numbers.Value;
import print.CustomPrinter;


public abstract class Simplex {

    protected Tableau tableau;

    protected boolean isOptimal = false;
    protected boolean isUnbounded = false;
    protected boolean isInfeasible = false;

    protected int constraintNum = 0;
    protected int variableNum = 0;

    protected boolean done = false;

    protected CustomPrinter printer = new CustomPrinter();

    public Simplex (Value[][] tableauInput){

        constraintNum = tableauInput.length-1;
        variableNum = tableauInput[0].length-1;
        tableau = new Tableau(tableauInput,true);
        printer.setState("Setup");
        printer.setDesc("Received simplex to solve");
        printer.printTableau(tableau.A,tableau.c,tableau.b,tableau.z,tableau.basis);

    }

    public Simplex (Tableau tableau){
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

    protected void pivot(Tableau tableau){

        printer.setDesc("Before pivoting");
        printer.printTableau(tableau);


        int pivotRowIndex = tableau.pivotRowIndex;
        int pivotColumnIndex = tableau.pivotColumnIndex;

        Value[][] A = tableau.A;
        boolean[] basis = tableau.basis;

        Logger.println("info","Pivoting!");

        Value pivot = new Value(0); //guarda o pivot
        Value mult = new Value(0);  //multiplicador

        //updating basis
        pivot.assign(A[pivotRowIndex][pivotColumnIndex]);
        for(int j=0;j<A[pivotRowIndex].length;j++){
            if((A[pivotRowIndex][j].isEqualTo(1)) && (basis[j])){
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

    protected Value[][] concatenateTwoMatrixesSideBySide(Value[][] A,Value[][] B){
        Value[][] C = new Value[A.length][A[0].length+B[0].length];
        for(int i=0;i<C.length;i++){
            for (int j = 0; j < C[0].length;j++){
                C[i][j] = new Value(0);
            }
        }
        for(int i=0;i<C.length;i++){
            for (int j = 0; j < A[0].length;j++){
                C[i][j] = new Value(A[i][j]) ;
            }
            for (int j = 0; j < B[0].length;j++){
                C[i][j+A[0].length] = new Value(B[i][j]);
            }
        }
        return C;
    }
    protected Value[] concatenateTwoArraysSideBySide(Value[] a,Value[] b){
        Value[] c = new Value[a.length+b.length];
        for (int i = 0; i < c.length;i++){
            c[i] = new Value(0);
        }
        for (int j = 0; j < a.length;j++){
            c[j] = new Value(a[j]) ;
        }
        for (int j = 0; j < b.length;j++){
            c[j+a.length] = new Value(b[j]);
        }
        return c;
    }

    protected void printThisTableau(Tableau thisTableau){
        printer.printTableau(thisTableau);
    }
    protected void printThisTableau(){
        printer.printTableau(this.tableau);
    }


    protected abstract void updatePivotRow();
    protected abstract void updatePivotColumn();


}
