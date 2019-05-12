package simplex;

import numbers.Value;

public class Tableau {

    public int numVar  = 0;
    public int numCons = 0;

    public Value[] c;
    public Value z;
    public Value[] b;
    public Value[] solution;
    public Value[] certOpt;
    public Value[] certUnb;
    public Value[] certInf;
    public boolean[] basis;
    public Value[][] A;
    public Value[][] relax;

    public int pivotRowIndex;
    public int pivotColumnIndex;
    public int problematicColumnIndex;


    public Tableau(int numVar, int numCons){

        this.numVar = numVar;
        this.numCons = numCons;
        //Recebe o total de linhas e colunas

        c = new Value[numVar];
        b = new Value[numCons];

        certOpt = new Value[numCons];
        certUnb = new Value[numVar+numCons];
        certInf  = new Value[numCons];
        solution = new Value[numVar];

        for(int i =0; i < numVar; i++){
            c[i] = new Value(0);
            solution[i] = new Value(0);
        }
        for(int i =0; i < numCons; i++){
            b[i] = new Value(0);
            certOpt[i] = new Value(0);
            certInf[i]  = new Value(0);
        }
        for(int i =0; i < numCons+numVar; i++){
            certUnb[i] = new Value(0);
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


            for(int j = 0; j < A[0].length; j++){
                boolean collumIsBasis = true;
                boolean onlyOneOne = false;
                for(int i =0; i < A.length; i++){
                   if(A[i][j].isEqualTo(1)){
                       if(!onlyOneOne){
                           onlyOneOne = true;
                       }
                       else {
                           collumIsBasis = false;
                       }
                   }
                   else if(!A[i][j].isEqualTo(0)){
                       collumIsBasis = false;
                   }
                }
                //for c
                    if(c[j].isEqualTo(1)){
                        if(!onlyOneOne){
                            onlyOneOne = true;
                        }
                        else {
                            collumIsBasis = false;
                        }
                    }
                    else if(!c[j].isEqualTo(0)){
                        collumIsBasis = false;
                    }

                basis[j] = collumIsBasis;
            }

            for (int i = 0; i < basis.length; i++) {
                if(!basis[i]) {
                    basis[i] = i >= numVar;
                }
                else {
                    basis[i] = true;
                }
            }
        }
    }

    public Tableau (Tableau tableauInput, boolean generateBasis, boolean aux){

        this( tableauInput.A[0].length, tableauInput.A.length);
        this.numVar = tableauInput.numVar;
        this.numCons = tableauInput.numCons;

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
            //Simple basis checking, would be different if relax had different format
            basis = new boolean[A[0].length + relax.length];
            for (int i = 0; i < basis.length; i++) {
                basis[i] = i >= A[0].length;
            }
            A = concatenateTwoMatrixesSideBySide(A, relax);
            c = concatenateTwoArraysSideBySide(c, zeroArray);
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
            certUnb[i].round();
        }
        for(int i =0; i < numCons; i++){
            certOpt[i].round();
            certInf[i].round();
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
