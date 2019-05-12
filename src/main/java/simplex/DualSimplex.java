package simplex;

import logging.Logger;
import numbers.Fraction;
import numbers.Value;
import print.ColorPrint;

public class DualSimplex extends Simplex{


    public DualSimplex(Value[][] tableauInput) {

        super(tableauInput);
        for (Value value : tableau.c) {
            value.assign(value.mult(-1));
        }
    }


    @Override
    public void run(){
        Logger.println("info","Running dual simplex");

        printer.setDesc("Before anything");
        printer.printTableau(tableau.A,tableau.c,tableau.b,tableau.z,tableau.basis);

        while (!done){
            updatePivotRow();
            if(done){
                //nesse caso, o simplex  eh  viavel e limitado
                Logger.println("info","Simplex pronto");
                isOptimal = true;
                tableau.round();
                printOptStatus(tableau);
                return;
            }
            updatePivotColumn();
            if(done){
                Logger.println("warning","Simplex Unbitado: nao foi encontrada nenhuma ratio negativa");
                isUnbounded = true;
                tableau.round();
                printUnbStatus(tableau);
                return;
            }

            pivot();

        }
        isOptimal = true;
        tableau.round();
        printOptStatus(tableau);
        return;
    }

    @Override
    protected void updatePivotColumn() {
        Logger.println("debug","Updating pivot collumn");
        tableau.pivotColumnIndex = 0;
        boolean first = false;
        boolean foundNegative = false;
        Fraction ratio = new Fraction(0,1,0);
        Fraction ratioMin = new Fraction(0,1,0);;

        for(int i=0;i<tableau.A[0].length;i++){
            ratio.num.assign(tableau.c[i]);
            ratio.den.assign(tableau.A[tableau.pivotRowIndex][i]);
            Logger.println("debug","Lendo num = " + ratio.num.doubleValue() + ", den = " + ratio.den.doubleValue());

            if((ratio.num.isZero()) && (ratio.den.isNegative())){ //regra de bland
                Logger.println("debug","   - Achou por regra de bland");
                tableau.pivotColumnIndex = i;
            }
            if( (ratio.den.isNegative()) && (ratio.num.isPositive()) ){ //se o denominador e o numerador forem positivos, testamos
                ratio.val = ratio.num.div(ratio.den);
                Logger.println("debug" , ", valor = " + ratio.val.doubleValue() );
                //como convencao, a primeira ratio valida sera a minima
                //para o resto, a maior ratio negativa eh o que queremos
                if((ratio.val.isGreaterThan(ratioMin.val)) || (!first)){
                    Logger.println("debug" , "- Achou maior razao negativa na coluna ("+i+"): " +
                            "num = "+ratio.num.doubleValue() +
                            ", den= "+ ratio.den.doubleValue() +
                            ", valor = "+ ratio.val.doubleValue());
                    tableau.pivotColumnIndex = i;
                    ratioMin.val.assign(ratio.val);
                    ratioMin.num.assign(ratio.num);
                    ratioMin.den .assign(ratio.den);
                    if(!first) first = true;
                }
                foundNegative = true;
            }
        }
        if(!foundNegative){
            Logger.println("warning" , "Is unbounded");
            done = true;
            System.exit(1);
        }
        else {
            printer.setPivotalColumn(tableau.pivotColumnIndex);
            Logger.println("debug", "Pivot column updated to: " + tableau.pivotColumnIndex + " with c[pc] = " + tableau.c[tableau.pivotRowIndex]);
        }
    }

    @Override
    protected void updatePivotRow(){
        Logger.println("debug","Updating pivot row");
        tableau.pivotRowIndex = 0;
        for(int i=0;i<tableau.b.length;i++)
            if(tableau.b[i].isSmallerThan(tableau.b[tableau.pivotRowIndex]))
                tableau.pivotRowIndex = i;
        if(tableau.b[tableau.pivotRowIndex].isPositive()){
            this.done = true;
            tableau.pivotColumnIndex = -2;
            tableau.pivotRowIndex = -2;
        }
        else {
            Logger.println("debug", "Pivot row updated to: " + tableau.pivotRowIndex + " with b[pr] = " + tableau.b[tableau.pivotRowIndex]);
            printer.setPivotalRow(tableau.pivotRowIndex);
        }
    }



}
