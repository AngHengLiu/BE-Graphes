package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

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
        BinaryHeap<Label> pq = new BinaryHeap<>();
        ArrayList<Label> list = new ArrayList<>();

        //Init de tableau
        for (Node node : graph.getNodes()) {
            if (node.equals(data.getOrigin())) {
                Label pointer = new Label(node, false, 0, null);
                list.add(pointer);
                pq.insert(pointer);
            } else {
                list.add(new Label(node, false, Float.MAX_VALUE, null));
            }
           
        }

        while(!pq.isEmpty()) {
            Label pointer = pq.deleteMin();
            if (pointer.getMarque()) continue;
            pointer.setMarque(true);

            for (Arc successor : pointer.getCourant().getSuccessors()) {
                Node node1 = successor.getDestination();
                for (Label l : list) {
                    if (l.getCourant().equals(node1) && !l.getMarque()) {
                        
                        if (l.getCoutRealise() > successor.getLength() + pointer.getCoutRealise()) {
                            l.setCoutRealise(successor.getLength() + pointer.getCoutRealise());
                            l.setPere(pointer);
                            pq.insert(l);
                            }
                        }
                    }
                }
    }
    
    ArrayList<Node> pathlist= new ArrayList<>();
    Label dest = null;
    for (Label prev : list) {
        if (prev.getCourant().equals(data.getDestination())) {
            dest = prev;
        }
    }

    while (dest.getPere() != null) {
        pathlist.add(dest.getCourant());
        dest = dest.getPere();
    }
    pathlist.add(data.getOrigin());
    Collections.reverse(pathlist);
    Path path = org.insa.graphs.model.Path.createFastestPathFromNodes(graph, pathlist);

    solution = new ShortestPathSolution(data, Status.OPTIMAL, path);

    // when the algorithm terminates, return the solution that has been found
    return solution;


    }
}
