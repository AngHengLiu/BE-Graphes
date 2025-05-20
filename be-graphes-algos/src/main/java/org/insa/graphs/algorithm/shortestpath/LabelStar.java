package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label {
    
    public LabelStar (Node courant, boolean marque, float cout_realise, Label pere) {
        super(courant, marque, cout_realise, pere);
    }

    @Override
    public int compareTo(Label arg0) {
        if (this.getCoutRealise() < arg0.getCoutRealise()) return -1;

        return 1;
    }

    
}
