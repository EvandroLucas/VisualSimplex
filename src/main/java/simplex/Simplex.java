package simplex;


import logging.Logger;
import numbers.Value;
import print.CustomPrinter;

import java.time.LocalTime;

public abstract class Simplex {

    protected Value[] c;
    protected Value z;
    protected Value[] b;
    protected Value[] certOtim;
    protected Value[] certIlim;
    protected Value[] certInv;
    protected boolean[] basis;
    protected Value[][] A;
    protected Value[][] relax;
    protected Value[][] Reg;

    protected int pivotRowIndex;
    protected int pivotColumnIndex;

    protected CustomPrinter printer = new CustomPrinter();

    public Simplex (Value[][] tableauInput){

        int numVar = tableauInput[0].length -1;
        int numCons = tableauInput.length -1;

        c = new Value[numVar];
        b = new Value[numCons];
            for(int i =0; i < b.length; i++){
                b[i] = new Value(0);
            }
        certOtim = new Value[numVar];
        certIlim = new Value[numVar];
        certInv  = new Value[numVar];

        for(int i =0; i < numVar; i++){
            c[i] = new Value(0);
            certOtim[i] = new Value(0);
            certIlim[i] = new Value(0);
            certInv[i]  = new Value(0);
        }
        //Received matrix
        A  = new Value[numCons][numVar];
        for(int i =0; i < A.length; i++){
            for(int j =0; j < A[i].length; j++){
                A[i][j] = new Value(0);
            }
        }
        //Identity matrix to concatenate into new one
        //and corresponding C array
        relax  = new Value[A.length][A.length];
        Value[] zeroArray = new Value[relax.length];
        for(int i =0; i < relax.length; i++){
            zeroArray[i] = new Value(0);
            for(int j =0; j < relax[i].length; j++){
                if(i==j) relax[i][j] = new Value(1);
                else relax[i][j] = new Value(0);
            }
        }

        //monta o tableau
        for (int i = 0; i < tableauInput.length; i++){
            for(int j = 0; j < tableauInput[i].length;j++){
                if(i == 0 && j < numVar){
                    c[j].assign(tableauInput[i][j]);
                }
                if(i > 0 && j < numVar ){
                    A[i-1][j].assign(tableauInput[i][j]);
                }
                if(i < numCons && j==numVar){
                    b[i].assign(tableauInput[i+1][j]);
                }
            }
        }
        A = concatenateTwoMatrixesSideBySide(A,relax);
        c = concatenateTwoArraysSideBySide(c,zeroArray);

        //Simple basis checking, would be different if relax had different format
        basis = new boolean[A[0].length];
        for(int i = 0; i < basis.length;i++){
            basis[i] = i >= A.length;
        }

        //TODO: trocar referencia
        z = new Value(0);
        printer.setState("Setup");
        printer.setDesc("Received simplex to solve");
        printer.printTableau(A,c,b,z,basis);

    }

    public abstract void run();


    protected void pivot(){
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

        for (int j = 0; j < A[pivotRowIndex].length; j++){
            A[pivotRowIndex][j].assign(A[pivotRowIndex][j].div(pivot)); //divide a linha pivotal inteira pelo pivot
        }
        if(A[pivotRowIndex][pivotColumnIndex].isNotEqualTo(1) ){ //erro!
            Logger.println("severe","Erro no pivoteamento!");
            System.exit(1);
        }
        for (int i = 0; i < A.length; i++){ // fazemos a conta com todos os elementos da matriz
            mult = A[i][pivotColumnIndex];
            if (i != pivotRowIndex){ //ignoramos a linha pivotal, ela ja foi
                //Para a matriz A e concatenada
                for (int j = 0; j < A[i].length; j++){
                    A[i][j].assign(A[i][j].sub(A[pivotRowIndex][j].mult(mult)));
                }
                //Para o vetor b
                b[i].assign(b[i].sub(b[pivotRowIndex].mult(mult)));
                //Para a primeira linha do tableau
            }
            if(i==0){
                mult = c[pivotColumnIndex];
                //Para z
                z.assign(z.sub(b[pivotRowIndex].mult(mult)));
                //z.assign(z.sub(mult));
                //Para o vetor c
                for (int j = 0; j < c.length; j++){
                    mult = c[pivotColumnIndex];
                    c[j].assign(c[j].sub(A[pivotRowIndex][j].mult(mult)));
                }
            }
        }
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


    protected abstract void updatePivotRow();
    protected abstract void updatePivotColumn();

}
