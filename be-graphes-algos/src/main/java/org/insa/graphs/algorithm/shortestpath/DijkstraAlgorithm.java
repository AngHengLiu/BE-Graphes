package org.insa.graphs.algorithm.shortestpath;

import java.nio.file.Path;
import java.util.PriorityQueue;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;

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
        BinaryHeap<Label> Distances = new BinaryHeap<>();

        Label pointer = null;
        //Init de tableau
        for (Node node : graph.getNodes()) {
            if (node.equals(data.getOrigin())) {
                pointer = new Label(node, true, 0, null);
                Distances.insert(pointer);
            } else {
                Distances.insert(new Label(node, false, Float.MAX_VALUE, null));
            }
        }



        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}