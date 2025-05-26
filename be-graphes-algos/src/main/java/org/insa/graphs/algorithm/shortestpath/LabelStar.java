package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
//import org.insa.graphs.model.Point;

public class LabelStar extends Label{

    private Node dest;
    
    public LabelStar(Node courant,boolean marque,float cout_realise,LabelStar pere, Node dest){
        super(courant,marque,cout_realise,pere);
        this.dest = dest;
    }

    //Getters redefined
    @Override  
    public float getCoutDest(){
        return this.calculCoutDest();
    }

    @Override 
    public LabelStar getPere(){
        return (LabelStar)super.getPere();
    } 

    //Calcul cout_dest
    public float calculCoutDest(){
        return (float)this.getCourant().getPoint().distanceTo(dest.getPoint());
    } 
}

