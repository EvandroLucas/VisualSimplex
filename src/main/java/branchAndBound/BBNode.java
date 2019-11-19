package branchAndBound;

import lpp.CanonicalLPP;
import simplex.Simplex;

import java.util.ArrayList;

public class BBNode extends Thread{

    public ArrayList<String> code;
    public CanonicalLPP lpp;
    public Simplex simplex;
    private BBSolver solver;
    private BBResult result;


    @Override
    public void run(){
        //Pedir permissão ao solver
        //Resolver o problema
            //Criar outros nós e executá-los, se necessário
        //Postar resultado ao solver
    }

    public String getNodeName(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < code.size();i++){
            if(i > 0) sb.append("-");
            sb.append(code.get(i));
        }
        return sb.toString();
    }


}
