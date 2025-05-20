package org.insa.graphs.gui.simple;

import org.insa.graphs.model.Node;

public class Scenario {
    private String carte;
    private Boolean faisable;
    private Double coutTemps;
    private Double coutDistance;
    private Node origin;
    private Node destination;

    private Scenario(String carte, Boolean faisable, Double coutTemps, Double coutDistance, Node origin, Node destination) {
        this.carte = carte;
        this.faisable = faisable;
        this.coutTemps = coutTemps;
        this.coutDistance = coutDistance;
        this.origin = origin;
        this.destination = destination;
    }
    

    public String getCarte() {return this.carte;}
    public Boolean getFaisable() {return this.faisable;}
    public Node getOrigin() {return this.origin;}
    public Node getDestination() {return this.destination;}
    public Double getCoutTemps() {return this.coutTemps;}
    public Double getCoutDistance() {return this.coutDistance;}
    
    



}
