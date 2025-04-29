package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
    
    private Node courant;
    private boolean marque;
    private float cout_realise;
    private Node pere;
    //private static int NodeCount = 0;
    private int NodeNumber;



    public Label(Node courant, boolean marque, float cout_realise, Node pere) {
        this.courant = courant;
        this.marque = marque;
        this.cout_realise = cout_realise;
        this.pere = pere;
        NodeNumber = courant.getId();
    }

    // Getters
    public Node getCourant() {return this.courant;}
    public boolean getMarque() {return this.marque;}
    public float getCoutRealise() {return this.cout_realise;}
    public Node getPere() {return this.pere;}
    public int getNodeNumber() {return this.NodeNumber;}
 

    //Setters
    public void setCourant(Node n) {this.courant = n;}
    public void setMarque(boolean m) {this.marque = m;}
    public void setCoutRealise(float c) {this.cout_realise = c;}
    public void setPere(Node p) {this.pere = p;}

    @Override
    public int compareTo(Label arg0) {
        if (this.cout_realise < arg0.cout_realise) return 1;

        return 0;
    }

}
