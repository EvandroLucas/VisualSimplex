package simplex;

import numbers.Value;

public class Tableau {

    private int numVar  = 0;
    private int numCons = 0;

    public Value[] c;
    public Value z;
    public Value[] b;
    public Value[] certOtim;
    public Value[] certIlim;
    public Value[] certInv;
    public boolean[] basis;
    public Value[][] A;
    public Value[][] relax;

    public int pivotRowIndex;
    public int pivotColumnIndex;

    public Tableau(int numVar, int numCons){

        this.numVar = numVar;
        this.numCons = numCons;
        //Recebe o total de linhas e colunas

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
        z = new Value(0);

        basis = new boolean[A[0].length];
        for (int i = 0; i < basis.length; i++) {
            basis[i] = false;
        }

    }

    public Tableau (Value[][] tableauInput, boolean generateBasis){

        this( tableauInput[0].length -1, tableauInput.length -1);

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

        if(generateBasis) {
            Value[] zeroArray = new Value[relax.length];
            for(int i =0; i < relax.length; i++) {
                zeroArray[i] = new Value(0);
            }
            A = concatenateTwoMatrixesSideBySide(A, relax);
            c = concatenateTwoArraysSideBySide(c, zeroArray);
            //Simple basis checking, would be different if relax had different format
            basis = new boolean[A[0].length];
            for (int i = 0; i < basis.length; i++) {
                basis[i] = i >= numVar;
            }
        }
    }

    public Tableau (Tableau tableauInput, boolean generateBasis, boolean aux){

        this( tableauInput.A[0].length, tableauInput.A.length);

        //monta o tableau
        for (int i = 0; i < tableauInput.A.length; i++){
            for(int j = 0; j < tableauInput.A[i].length;j++){
               A[i][j].assign(tableauInput.A[i][j]);
            }
        }
        for (int i = 0; i < b.length; i++){
            b[i].assign(tableauInput.b[i]);
        }

        if(generateBasis) {
            Value[] zeroArray = new Value[relax.length];
            for(int i =0; i < relax.length; i++) {
                zeroArray[i] = new Value(0);
            }
            A = concatenateTwoMatrixesSideBySide(A, relax);
            c = concatenateTwoArraysSideBySide(c, zeroArray);
            //Simple basis checking, would be different if relax had different format
            basis = new boolean[A[0].length];
            for (int i = 0; i < basis.length; i++) {
                basis[i] = i >= numVar;
            }
        }

        if(!aux) {
            for (int i = 0; i < c.length; i++) {
                c[i].assign(tableauInput.c[i]);
            }
        }
        else{
            for (int i = 0; i < c.length; i++) {
                if(i < A[0].length - tableauInput.A.length)
                    c[i].assign(0);
                else
                    c[i].assign(1);
            }
        }


    }

    private Value[][] concatenateTwoMatrixesSideBySide(Value[][] A,Value[][] B){
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
    private Value[] concatenateTwoArraysSideBySide(Value[] a,Value[] b){
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


    public void round(){
        //Recebe o total de linhas e colunas
        for(int i =0; i < b.length; i++){
            b[i].round();
        }
        for(int i =0; i < numVar; i++){
            c[i].round();
            certOtim[i].round();
            certIlim[i].round();
            certInv[i].round();
        }
        //Received matrix
        for(int i =0; i < A.length; i++){
            for(int j =0; j < A[i].length; j++){
                A[i][j].round();
            }
        }
        z.round();
    }




}
