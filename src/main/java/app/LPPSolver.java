package app;

import logging.Logger;
import numbers.Value;
import simplex.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class LPPSolver {

    private int constraintNum = 0;
    private int variableNum = 0;
    private Value[][] tableau;
    private Value[] c;
    private Value[] b;
    boolean toRunAsDual = false;
    boolean toRunAsPrimal= false;
    public Simplex simplex;


    public void readFromFile(File file) {
        Logger.println("info","Reading data from file...");
        try {
            Scanner scanner = new Scanner(file).useLocale(Locale.US);;
            constraintNum = scanner.nextInt();
            variableNum = scanner.nextInt();
            tableau = new Value[constraintNum + 1][variableNum + 1];
                for(int i =0; i < tableau.length; i++){
                    for(int j =0; j < tableau[i].length; j++){
                        tableau[i][j] = new Value(0);
                    }
                }
            c = new Value[variableNum];
                for(int i =0; i < c.length; i++){
                    c[i] = new Value(0);
                }
            b = new Value[constraintNum];
                for(int i =0; i < b.length; i++){
                    b[i] = new Value(0);
                }

            for (int i = 0; i < tableau.length; i++) {
                for (int j = 0; j < tableau[i].length; j++) {
                    //Preenche z, que não será usado agora
                    if (i == 0 && j == tableau[i].length - 1) {
                        tableau[i][j].assign(0);
                    }
                    else {
                        tableau[i][j].assign( scanner.nextDouble() );
                        //Membros do vetor c vão para o vetor c
                        if(i==0){
                            tableau[i][j].mult(-1);
                            c[j].assign(tableau[i][j]);
                        }
                        //Membros do vetor b vão para o vetor b
                        else if(j==variableNum && i!=0){
                            b[i-1].assign(tableau[i][j]);
                        }
                    }
                }
            }

        }
        catch (FileNotFoundException fnf){
            fnf.printStackTrace();
            System.exit(1);
        }
        c = tableau[0].clone();
    }

    public void writeToFile(File file){
        Logger.println("info","Writing data from file...");
        try {
            FileWriter fileWriter = new FileWriter(file);
            String simplexStatus = "error";

            if(simplex.isUnbounded){
                simplexStatus = "ilimitada";
            }
            if(simplex.isInfeasible){
                simplexStatus = "inviavel";
            }
            if(simplex.isOptimal){
                simplexStatus = "otima";
            }
            fileWriter.write(simplexStatus + "\n");

            if(simplex.isUnbounded){
                for(Value value : simplex.tableau.solution){
                    fileWriter.write(value.toString() + " ");
                }
                fileWriter.write("\n");
                for(Value value : simplex.tableau.certUnb){
                    fileWriter.write(value.toString() + " ");
                }
            }
            else if(simplex.isInfeasible){
                for(Value value : simplex.tableau.certInf){
                    fileWriter.write(value.toString() + " ");
                }
            }
            else{ //simplex.isOptimal
                fileWriter.write(simplex.tableau.z.toString() + "\n");
                for(Value value : simplex.tableau.solution){
                    fileWriter.write(value.toString() + " ");
                }
                fileWriter.write("\n");
                for(Value value : simplex.tableau.certOpt){
                    fileWriter.write(value.toString() + " ");
                }
            }




            fileWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void runSimplex(){

        Logger.println("info","Checking simplex type");
        if (canRunDual()){
            Logger.println("info","Will run the dual simplex method");
             simplex = new DualSimplex(tableau);
        }
        else if (canRunPrimal()){
            Logger.println("info","Will run the primal simplex method");
            simplex = new PrimalSimplex(tableau);
        }
        else if (canRunAux()){
            Logger.println("info","Will run the auxiliar simplex method");
            simplex = new AuxSimplex(tableau);
        }
        else {
            Logger.println("severe","Simplex denied");
            simplex = null;
            System.exit(1);
        }
        Logger.println("info","Ready to run");
        simplex.run();
    }

    private boolean canRunDual(){
        for(int i=0; i< c.length; i++){
            if(c[i].isPositive()){
                // Não pode dual, tem negativo em -c
                toRunAsDual = false;
                return false;
            }
        }
        //se chegou aqui, nao tem negativo em -c
        //agora checamos para ver se b tem negativo
        //tem que ter, caso contrario fazemos auxiliar
        for(int i=0; i< b.length; i++){
            if(b[i].isNegative()){
                //Pode fazer dual, nao tem negativo em -c, mas tem negativo em b
                toRunAsDual = true;
                return true;
            }
        }
        //se chegou aqui, nao tem negativo nem em -c nem em b
        //nesse caso, temos que fazer o auxiliar
        // ou seja, nao pode dual, ja que nao tem negativo em nenhum dos dois
        return false;
    }

    private boolean canRunPrimal(){

        boolean hasPositive = false;

        //se tiver negativo em -c, podemos fazer o primal
        for(int i=0; i < c.length; i++){
            if(c[i].isPositive()){
                hasPositive = true;
                break;
            }
        }
        if(!hasPositive) {
            //Nao pode primal, nao tem negativo em -c
            return false;
        }
        //se chegou aqui,tem negativo em -c
        //agora temos que chegar se tem algum negativo em b
        //se tiver, temos que fazer auxiliar
        for(int i=0; i< b.length; i++){
            if(b[i].isNegative()){
                //Nao pode primal, tem negativo em -c, mas tambem tem em b
                return false;
            }
        }
        //se chegou aqui, tem negativo em -c mas nao tem em b
        //faremos primal
        //Pode fazer primal, tem negativo em -c, mas nao tem em b

        return true;
    }

    private boolean canRunAux(){
        //sempre podemos fazer se recebermos uma PL na forma padrão
        return true;
    }


}
