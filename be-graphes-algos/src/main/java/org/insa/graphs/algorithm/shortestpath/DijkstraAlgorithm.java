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
        BinaryHeap<Label> pq = new BinaryHeap<>();
        HashMap<Node, Label> hm = new HashMap<>();

        //Init de tableau
        Label pointer = null;
        for (Node node : graph.getNodes()) {
            if (node.equals(data.getOrigin())) {
                pointer = new Label(node, false, 0, null);
                pq.insert(pointer);
            } else {
                pointer = new Label(node, false, (float) Double.POSITIVE_INFINITY, null);
            }
            hm.put(node, pointer);
        }

        while(!pq.isEmpty()) {
            Label current = pq.deleteMin();

            if (current.getMarque()) continue;
            if (current.getCourant().equals(data.getDestination())) break;
            

            current.setMarque(true);

            for (Arc successor : current.getCourant().getSuccessors()) {
                if (!data.isAllowed(successor)) continue;

                Node node1 = successor.getDestination();
                pointer = hm.get(node1);
                if (pointer != null && !pointer.getMarque()) {
                    float cout = current.getCoutRealise() + (float) data.getCost(successor);
                    if (pointer.getCoutRealise() > cout) {
                       
                        boolean found = true;
                        while (found) {
                            found = false;
                             try {
                             pq.remove(pointer);
                             found = true;
                        } catch (Exception e) {}
                        }
                            pointer.setCoutRealise(cout);
                            pointer.setPere(current);
                            pq.insert(pointer);
                    }
                } 
            }
        }
    
    ArrayList<Node> pathlist= new ArrayList<>();
    Label dest = hm.get(data.getDestination());

    while (dest != null) {
        pathlist.add(dest.getCourant());
        dest = dest.getPere();
    }
    Collections.reverse(pathlist);
    Path path = org.insa.graphs.model.Path.createShortestPathFromNodes(graph, pathlist);

    solution = new ShortestPathSolution(data, Status.OPTIMAL, path);

    // when the algorithm terminates, return the solution that has been found
    return solution;


    }
}
