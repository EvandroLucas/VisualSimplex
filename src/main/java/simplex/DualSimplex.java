package simplex;

import logging.Logger;
import numbers.Fraction;
import numbers.Value;
import print.ColorPrint;

public class DualSimplex extends Simplex{


    public DualSimplex(Value[][] tableauInput) {

        super(tableauInput);
        for (Value value : c) {
            value.assign(value.mult(-1));
        }
    }


    @Override
    public void run(){
        Logger.println("info","Running dual simplex");

        printer.setDesc("Before anything");
        printer.printTableau(A,c,b,z,basis);

        while (!done){
            updatePivotRow();
            if(done){
                //nesse caso, o simplex  eh  viavel e limitado
                Logger.println("info","Simplex pronto");
                isOptimal = true;
                return;
            }
            updatePivotColumn();
            if(done){
                Logger.println("warning","Simplex Ilimitado: nao foi encontrada nenhuma ratio negativa");
                isUnbounded = true;
                return;
            }

            pivot();

        }
        isOptimal = true;
        return;
    }

    @Override
    protected void updatePivotColumn() {
        Logger.println("debug","Updating pivot collumn");
        pivotColumnIndex = 0;
        boolean first = false;
        boolean foundNegative = false;
        Fraction ratio = new Fraction(0,1,0);
        Fraction ratioMin = new Fraction(0,1,0);;

        for(int i=0;i<A[0].length;i++){
            ratio.num.assign(c[i]);
            ratio.den.assign(A[pivotRowIndex][i]);
            Logger.println("debug","Lendo num = " + ratio.num.doubleValue() + ", den = " + ratio.den.doubleValue());

            if((ratio.num.isZero()) && (ratio.den.isNegative())){ //regra de bland
                Logger.println("debug","   - Achou por regra de bland");
                pivotColumnIndex = i;
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
                    pivotColumnIndex = i;
                    ratioMin.val.assign(ratio.val);
                    ratioMin.num.assign(ratio.num);
                    ratioMin.den .assign(ratio.den);
                    if(!first) first = true;
                }
                foundNegative = true;
            }
        }
        if(!foundNegative){
            Logger.print("warning" , "Is unlimited");
            done = true;
            System.exit(1);
        }
        else {
            printer.setPivotalColumn(pivotColumnIndex);
            Logger.println("debug", "Pivot column updated to: " + pivotColumnIndex + " with c[pc] = " + c[pivotRowIndex]);
        }
    }

    @Override
    protected void updatePivotRow(){
        Logger.println("debug","Updating pivot row");
        pivotRowIndex = 0;
        for(int i=0;i<b.length;i++)
            if(b[i].isSmallerThan(b[pivotRowIndex]))
                pivotRowIndex = i;
        if(b[pivotRowIndex].isPositive()){
            this.done = true;
        }
        Logger.println("debug","Pivot row updated to: " + pivotRowIndex + " with b[pr] = " + b[pivotRowIndex]);
        printer.setPivotalRow(pivotRowIndex);
    }



}
