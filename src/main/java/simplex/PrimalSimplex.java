package simplex;

import logging.Logger;
import numbers.Fraction;
import numbers.Value;


public class PrimalSimplex extends Simplex{


    public PrimalSimplex(Tableau tableauInput) {
        this(tableauInput,true);
    }
    public PrimalSimplex(Tableau tableauInput, boolean invertC) {
        super(tableauInput);
        //Inverting the signs for c vector
        if(invertC) {
            for (Value value : tableau.c) {
                value.assign(value.mult(-1));
            }
        }
        printer.setDesc("Primal simplex to solve");
        printThisTableau();
    }
    public PrimalSimplex(Value[][] tableauInput) {
        super(tableauInput);
        //Inverting the signs for c vector
        for (Value value : tableau.c) {
            value.assign(value.mult(-1));
        }
        printer.setDesc("Primal simplex to solve");
        printThisTableau();
    }


    @Override
    public void run() {
        Logger.println("info", "Running primal simplex");


        while (!done) {
            updatePivotColumn();
            if (done) {
                //nesse caso, o simplex  eh  viavel e limitado
                Logger.println("info", "Simplex done");
                isOptimal = true;
                tableau.round();
                return;
            }
            updatePivotRow();
            if (done) {
                //um valor negativo retornado indica que nao foi encontrada
                //nesse caso, o simplex  eh  ilimitado
                Logger.println("warning", "Simplex Unbounded: no positive ratio found");
                done = true;
                isUnbounded = true;
                tableau.round();
                return;
            }

            pivot();
        }
        isOptimal = true;
        tableau.round();
        return;
    }

    @Override
    protected void updatePivotRow(){
        Logger.println("debug","Updating pivot row");
        tableau.pivotRowIndex = 0;
        boolean first = false;
        boolean foundPositive = false;
        Fraction ratio = new Fraction(0,1,0);
        Fraction ratioMin = new Fraction(0,1,0);;

        for(int i=0;i<tableau.A.length;i++){
            ratio.num.assign(tableau.b[i]);
            //ratio.den.assign(A[tableau.pivotColumnIndex][i]);
            ratio.den.assign(tableau.A[i][tableau.pivotColumnIndex]);
            Logger.println("debug","Lendo num = " + ratio.num.doubleValue() + ", den = " + ratio.den.doubleValue());
            if((ratio.num.isZero()) && (ratio.den.isPositive())){ //regra de bland
                Logger.println("debug","   - Achou por regra de bland");
                tableau.pivotRowIndex = i;
            }
            if( (ratio.den.isPositive()) && (ratio.num.isPositive()) ){ //se o denominador e o numerador forem positivos, testamos
                ratio.val = ratio.num.div(ratio.den);
                Logger.println("debug" , ", valor = " + ratio.val.doubleValue() );
                //como convencao, a primeira ratio valida sera a minima
                //para o resto, a menor ratio positiva eh o que queremos
                if((ratio.val.isSmallerThan(ratioMin.val)) || (!first)){
                    Logger.println("debug" , "- Achou minima ratio positiva: " +
                            "num = "+ratio.num.doubleValue() +
                            ", den= "+ ratio.den.doubleValue() +
                            ", valor = "+ ratio.val.doubleValue());
                    tableau.pivotRowIndex = i;
                    ratioMin.val.assign(ratio.val);
                    ratioMin.num.assign(ratio.num);
                    ratioMin.den .assign(ratio.den);
                    if(!first) first = true;
                }
                foundPositive = true;
            }
        }
        if(!foundPositive){
            Logger.println("warning" , "Is unbounded");
            done = true;
            System.exit(1);
        }
        Logger.println("debug","Pivot row updated to: " + tableau.pivotRowIndex);
        printer.setPivotalRow(tableau.pivotRowIndex);
    }
    @Override
    protected void updatePivotColumn(){
        Logger.println("debug","Updating pivot column");
        tableau.pivotColumnIndex = 0;
        for(int i=0;i<tableau.c.length;i++)
            if(tableau.c[i].isSmallerThan(tableau.c[tableau.pivotColumnIndex]))
                tableau.pivotColumnIndex = i;
        if(!tableau.c[tableau.pivotColumnIndex].isNegative()){
            this.done = true;
        }
        Logger.println("debug","Pivot column updated to: " + tableau.pivotColumnIndex + " with c[pc] = " + tableau.c[tableau.pivotColumnIndex]);
        printer.setPivotalColumn(tableau.pivotColumnIndex);
    }



}
