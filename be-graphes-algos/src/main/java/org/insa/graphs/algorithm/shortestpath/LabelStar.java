package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
//import org.insa.graphs.model.Point;

public class LabelStar extends Label{

    private float cout_dest;
    private Node dest;
    
    public LabelStar(Node courant,boolean marque,float cout_realise,LabelStar pere, float cout_dest,Node dest){
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

    //Calcul cout_dest
    public float calculCoutDest(){
        return (float)this.getCourant().getPoint().distanceTo(dest.getPoint());
    } 

    //Redefinitions
    public float getTotalCost(){
        return this.getCoutRealise() + this.calculCoutDest();
    }

    public int compareTo(LabelStar otherLS) {
        if (this.getTotalCost() < otherLS.getTotalCost()){
            return -1;
        } 
        
        //Ajouté : En cas d'égalite
        if (this.getTotalCost() == otherLS.getTotalCost()){ 
            if (this.calculCoutDest() < otherLS.calculCoutDest()){
                return -1;
            }     
        } 
        return 1;
    }

}

