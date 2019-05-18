import java.util.ArrayList;

public class BlockNode extends BaseNode {
    ArrayList<BaseNode> block = new ArrayList<>();
    int currentBranchInUse = -1;
    public void addToBlock(BaseNode toSet) {
        block.add(toSet);
    }
    public ArrayList<BaseNode> getBlock(){
        return block;
    }

    @Override
    public void execute(Robot robot){
            for(BaseNode B:block){
                B.execute(robot);
            }
    }

    @Override
    public String toString(){
        StringBuilder toReturn = new StringBuilder();
        for(BaseNode b : block){
            toReturn.append(b.toString());
        }
        return toReturn.toString();
    }

    public void addNewBranch(BaseNode temp) {
        currentBranchInUse++;
        block.add(temp);
    }

    public void setBlock(ArrayList<BaseNode> addToTree) {
        block = addToTree;
    }

    public void setBranch(BaseNode branch){
        if(block.size()==0){
            block.add(branch);
        } else{
            block.set(currentBranchInUse,branch);
        }
    }
}
