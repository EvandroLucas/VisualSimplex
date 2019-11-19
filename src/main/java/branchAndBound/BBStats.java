package branchAndBound;

public class BBStats {
    private static BBStats ourInstance = new BBStats();
    private Integer bestResult;
    private BBNode bestNode;

    public static BBStats getInstance() {
        return ourInstance;
    }

    private BBStats() {
    }

    public Integer getBestResult(){
        return bestResult;
    }

    public synchronized void postBestNode(BBNode node){

    }

}
