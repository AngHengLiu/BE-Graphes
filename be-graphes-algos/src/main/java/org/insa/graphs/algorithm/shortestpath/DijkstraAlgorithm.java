package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;

        Graph graph = data.getGraph();
        BinaryHeap<Label> pq = new BinaryHeap<>(); // Pour les iterations
        HashMap<Node, Label> hm = new HashMap<>(); // Pour lien entre Node et label

        //Init de tableau
        Label pointer = null;
        for (Node node : graph.getNodes()) {
            if (node.equals(data.getOrigin())) {
                pointer = new Label(node, false, 0, null);
                pq.insert(pointer);
            } else {
                pointer = new Label(node, false, Float.POSITIVE_INFINITY, null);
            }
            hm.put(node, pointer);
        }

        notifyOriginProcessed(data.getOrigin());
        // Boucle principal
        while(!pq.isEmpty()) {
            Label current = pq.deleteMin();

            if (current.getMarque()) continue; // SI déjà marqué, skip
            if (current.getCourant().equals(data.getDestination())) break; // Si dest trouvé, fin
            

            current.setMarque(true);

            for (Arc successor : current.getCourant().getSuccessors()) {
                if (!data.isAllowed(successor)) continue;

                Node node1 = successor.getDestination();
                pointer = hm.get(node1);
                if (pointer != null && !pointer.getMarque()) {
                    notifyNodeReached(node1);
                    float newCout = current.getCoutRealise() + (float) data.getCost(successor);
                    if (pointer.getCoutRealise() > newCout) {
                        pointer.setCoutRealise(newCout);
                       
                        // Change le père et ajoute dans le Binary Heap
                        pointer.setPere(current);
                        pq.insert(pointer);
                    }
                } 
            }
        }
    
    notifyDestinationReached(data.getDestination());
    // Creer liste et parcourir le plus court chemin
    ArrayList<Node> pathlist= new ArrayList<>();
    Label dest = hm.get(data.getDestination());
    Path path;

    // Si pas faisable
    if (dest == null || dest.getCoutRealise() == Float.POSITIVE_INFINITY || dest.getPere() == null) {
        solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        return solution;
    }

    // Parcours
    while (dest != null) {
        pathlist.add(dest.getCourant());
        dest = dest.getPere();
    }

    Collections.reverse(pathlist); // Inverser liste pour avoir: debut -> fin

    path = org.insa.graphs.model.Path.createShortestPathFromNodes(graph, pathlist);
    solution = new ShortestPathSolution(data, Status.OPTIMAL, path);

    return solution;


    }
}
