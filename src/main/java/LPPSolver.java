import simplex.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class LPPSolver {

    private int constraintNum = 0;
    private int variableNum = 0;
    private int[][] tableau;
    private int[] c;
    private int[] b;
    boolean toRunAsDual = false;
    boolean toRunAsPrimal= false;


    public void readFromFile(File file) {

        try {
            Scanner scanner = new Scanner(file);
            constraintNum = scanner.nextInt();
            variableNum = scanner.nextInt();
            tableau = new int[constraintNum + 1][variableNum + 1];
            c = new int[variableNum];
            b = new int[constraintNum];

            for (int i = 0; i < tableau.length; i++) {
                for (int j = 0; j < tableau[i].length; j++) {
                    //Preenche z, que não será usado agora
                    if (i == 0 && j == tableau[i].length - 1) {
                        tableau[i][j] = 0;
                    }
                    else {
                        tableau[i][j] = scanner.nextInt();
                        //Membros do vetor c vão para o vetor c
                        if(i==0){
                            tableau[i][j] = tableau[i][j] * (-1);
                            c[j] = tableau[i][j];
                        }
                        //Membros do vetor b vão para o vetor b
                        else if(j==variableNum && i!=0){
                            b[i-1] = tableau[i][j];
                        }
                    }
                }
            }

        }
        catch (FileNotFoundException fnf){
            fnf.printStackTrace();
            System.exit(1);
        }

        c = tableau[0];

    }

    public void runSimplex(){

        Simplex simplex = null;
        //Checamos a dual:
        if (canRunDual()){
            System.out.println("Vai dual!");
             simplex = new DualSimplex(tableau);
        }
        else if (canRunPrimal()){
            System.out.println("Vai primal!");
            simplex = new PrimalSimplex(tableau);
        }
        else if (canRunAux()){
            System.out.println("Vai aux!");
            simplex = new AuxSimplex(tableau);
        }

        simplex.run();


    }

    public boolean canRunDual(){
        for(int i=0; i< c.length; i++){
            if(c[i] < 0){
                // Não pode dual, tem negativo em c
                toRunAsDual = false;
                return false;
            }
        }

        //se chegou aqui, nao tem negativo em c
        //agora checamos para ver se b tem negativo
        //tem que ter, caso contrario fazemos auxiliar
        for(int i=0; i< b.length; i++){
            if(b[i] < 0){
                //Pode fazer dual, nao tem negativo em c, mas tem negativo em b
                toRunAsDual = true;
                return true;
            }
        }

        //se chegou aqui, nao tem negativo nem em c nem em b
        //nesse caso, temos que fazer o auxiliar
        // ou seja, nao pode dual, ja que nao tem negativo em nenhum dos dois
        return false;
    }

    public boolean canRunPrimal(){

        boolean hasNegative = false;

        //se tiver negativo em c, podemos fazer o primal
        for(int i=0; i < c.length; i++){
            if(c[i] < 0){
                hasNegative = true;
                break;
            }
        }
        if(!hasNegative) {
            //Nao pode primal, nao tem negativo em c
            return false;
        }
        //se chegou aqui,tem negativo em c
        //agora temos que chegar se tem algum negativo em b
        //se tiver, temos que fazer auxiliar
        for(int i=0; i< b.length; i++){
            if(b[i] < 0){
                //Nao pode primal, tem negativo em c, mas tambem tem em b
                return false;
            }
        }
        //se chegou aqui, tem negativo em c mas nao tem em b
        //faremos primal
        //Pode fazer primal, tem negativo em c, mas nao tem em b

        return true;
    }

    public boolean canRunAux(){
        //sempre podemos fazer se recebermos uma PL na forma padrão
        return true;
    }



    public void printConsoleResult(){

    }

}
