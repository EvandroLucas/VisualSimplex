package simplex;

import logging.Logger;
import numbers.Fraction;
import numbers.Value;
import org.omg.PortableInterceptor.LOCATION_FORWARD;

import java.time.LocalDate;

public class PrimalSimplex extends Simplex{

    private boolean done = false;
    public PrimalSimplex(Value[][] tableauInput) {
        super(tableauInput);
        //Inverting the signs for c vector
        for (Value value : c) {
            value.assign(value.mult(-1));
        }
    }


    @Override
    public void run(){
        Logger.println("info","Running primal simplex");
        boolean fim = false;
        int aux = 0;
        pivotRowIndex = 0;
        int limitCount = 10;
        int counter = 0;

        while (!done){
            if(counter < limitCount) {
                counter++;
            }
            else {
                Logger.println("severe", "Limit exceeded!");
                System.exit(1);
            }

            updatePivotColumn();
            if(done){
                //nesse caso, o simplex  eh  viavel e limitado
                Logger.println("info","Simplex pronto");
                return;
            }
            updatePivotRow();
            if(pivotRowIndex < 0){
                //um valor negativo retornado indica que nao foi encontrada
                //nesse caso, o simplex  eh  ilimitado
                Logger.println("warning","Simplex Ilimitado: nao foi encontrada nenhuma ratio positiva");
                done = true;
                return;
            }
            Logger.println("debug","Linha pivotal = " + pivotRowIndex );
            Logger.println("debug","Elemento = " + A[pivotRowIndex][pivotColumnIndex] );
            printer.setDesc("Before pivoting");
            printer.printTableau(A,c,b,z,basis);
            pivot();
            printer.setDesc("After pivoting");
            printer.printTableau(A,c,b,z,basis);
        }
    }

    @Override
    protected void updatePivotRow(){
        Logger.println("debug","Updating pivot row");
        pivotRowIndex = 0;
        boolean first = false;
        boolean foundPositive = false;
        Fraction ratio = new Fraction(0,1,0);
        Fraction ratioMin = new Fraction(0,1,0);;

        for(int i=0;i<A.length;i++){
            ratio.num.assign(b[i]);
            ratio.den.assign(A[pivotColumnIndex][i]);
            Logger.println("debug","Lendo num = " + ratio.num.doubleValue() + ", den = " + ratio.den.doubleValue());
            if((ratio.num.isZero()) && (ratio.den.isPositive())){ //regra de bland
                Logger.println("debug","   - Achou por regra de bland");
                pivotRowIndex = i;
            }
            if( (ratio.den.isPositive()) && (ratio.num.isPositive()) ){ //se o denominador e o numerador forem positivos, testamos
                ratio.val = ratio.num.div(ratio.den);
                Logger.print("debug" , ", valor = " + ratio.val.doubleValue() );
                //como convencao, a primeira ratio valida sera a minima
                //para o resto, a menor ratio positiva eh o que queremos
                if((ratio.val.isSmallerThan(ratioMin.val)) || (!first)){
                    Logger.print("debug" , "- Achou minima ratio positiva: " +
                            "num = "+ratio.num.doubleValue() +
                            ", den= "+ ratio.den.doubleValue() +
                            ", valor = "+ ratio.val.doubleValue());
                    pivotRowIndex = i;
                    ratioMin.val.assign(ratio.val);
                    ratioMin.num.assign(ratio.num);
                    ratioMin.den .assign(ratio.den);
                    if(!first) first = true;
                }
                foundPositive = true;
            }
        }
        if(!foundPositive){
            Logger.print("warning" , "Is unlimited");
            System.exit(1);
        }
        Logger.println("debug","Pivot row updated to: " + pivotRowIndex);
        printer.setPivotalRow(pivotRowIndex);
    }
    @Override
    protected void updatePivotColumn(){
        Logger.println("debug","Updating pivot column");
        pivotColumnIndex = 0;
        for(int i=0;i<c.length;i++)
            if(c[i].isSmallerThan(c[pivotColumnIndex]))
                pivotColumnIndex = i;
        if(!c[pivotColumnIndex].isNegative()){
            this.done = true;
        }
        Logger.println("debug","Pivot column updated to: " + pivotColumnIndex + " with c[pc] = " + c[pivotColumnIndex]);
        printer.setPivotalColumn(pivotColumnIndex);
    }


}
