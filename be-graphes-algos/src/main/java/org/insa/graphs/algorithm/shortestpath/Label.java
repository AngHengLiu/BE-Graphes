package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
    
    private Node courant;
    private boolean marque;
    private float cout_realise;
    private Label pere;

    public Label(Node courant, boolean marque, float cout_realise, Label pere) {
        this.courant = courant;
        this.marque = marque;
        this.cout_realise = cout_realise;
        this.pere = pere;
    }

    // Getters
    public Node getCourant() {return this.courant;}
    public boolean getMarque() {return this.marque;}
    public float getCoutRealise() {return this.cout_realise;}
    public Label getPere() {return this.pere;}

    //Setters
    public void setCourant(Node n) {this.courant = n;}
    public void setMarque(boolean m) {this.marque = m;}
    public void setCoutRealise(float c) {this.cout_realise = c;}
    public void setPere(Label p) {this.pere = p;}

    public float getTotalCost(){
        return this.cout_realise;
    }

    @Override
    public int compareTo(Label arg0) {
        if (this.getTotalCost() < arg0.getTotalCost()) return -1;

        return 1;
    }

}
