package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
//import org.insa.graphs.model.Point;

public class LabelStar extends Label{

    private float cout_dest;
    private Node dest;
    
    public LabelStar(Node courant,boolean marque,float cout_realise,Label pere, float cout_dest,Node dest){
        super(courant,marque,cout_realise,pere);
        this.cout_dest = cout_dest;
        this.dest = dest;
    }

    //Getter
    public float getCoutDest(){
        return this.cout_dest;
    }

    //Setter
    public void setCoutDest(float cout){
        this.cout_dest = cout;
    }

    //Redefinition
    public float getTotalCost(){
        float cout_dest = (float)this.getCourant().getPoint().distanceTo(dest.getPoint());
        return this.getCoutRealise() + cout_dest;
    }
}

