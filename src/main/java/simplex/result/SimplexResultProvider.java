package simplex.result;

import simplex.Simplex;

public class SimplexResultProvider {

    public static SimplexResult getResult(Simplex simplex){
        if(simplex.isOptimal){
            return new OptimalResult(simplex);
        }
        else if(simplex.isUnbounded){
            return new UnboundedResult(simplex);
        }
        else {
            return new InfeasibleResult(simplex);
        }
    }

}
