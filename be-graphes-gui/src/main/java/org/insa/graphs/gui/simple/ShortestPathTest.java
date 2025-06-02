package org.insa.graphs.gui.simple;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.simple.test.TestScenario;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
 // visit these directory to see the list of available files on commetud.

public class ShortestPathTest {
    public static void main(String[] args) {
        List<TestScenario> scenarios = TestScenario.createTestScenarios();
        
        for (TestScenario scenario : scenarios) {
            System.out.println("\nTesting: " + scenario);
            
            // Load the graph
            // Create a graph reader
            final String mapPath = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/" + scenario.mapName;
            try (GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapPath))))) {
                Graph graph = reader.read();
                
                // Get nodes
                Node origin = graph.get(scenario.originId);
                Node destination = graph.get(scenario.destinationId);
                
                // Create data
                ShortestPathData data = new ShortestPathData(graph, origin, destination, 
                        ArcInspectorFactory.getAllFilters().get(scenario.arcInspectorId));
                
                // DIJKSTRA
                DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
                ShortestPathSolution dijkstraSolution = dijkstra.run();
                
                // Check if solution matches expected feasibility
                boolean pathExists = dijkstraSolution.getStatus() == AbstractSolution.Status.OPTIMAL;
                if (pathExists != scenario.expectedFeasible) {
                    System.err.println("Error: Not expected feasibility");
                }
                
                // If path exists, validate
                if (pathExists) {
                    Path path = dijkstraSolution.getPath();
                    boolean isValid = path.isValid();
                    double computedCost = scenario.arcInspectorId == 0 ? path.getLength() : path.getMinimumTravelTime();
                    
                    System.out.println("Dijkstra result: Path valid: " + isValid + ", Cost: " + computedCost);

                    // For small graphs, also run Bellman-Ford for comparison
                    if (graph.size() < 100000) {

                        //BELLMAN FORD
                        BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
                        ShortestPathSolution bellmanFordSolution = bellmanFord.run();
                        
                        if (bellmanFordSolution.getStatus() == AbstractSolution.Status.OPTIMAL) {
                            Path bellmanPath = bellmanFordSolution.getPath();
                            double bellmanCost = scenario.arcInspectorId == 0 ? 
                                    bellmanPath.getLength() : bellmanPath.getMinimumTravelTime();
                            
                            // Compare costs
                            System.out.println("Bellman-Ford result: Path valid: " + bellmanPath.isValid() + ", Cost: " + bellmanCost);
                            System.out.println("Cost difference: " + Math.abs(computedCost - bellmanCost));
                        } else {
                            System.out.println("Bellman-Ford could not find a path.");
                        }
                    }
                } else {
                    System.out.println("Path does not exist.");
                }
                
            } catch (Exception e) {
                System.err.println("Error testing scenario " + scenario + ": " + e.getMessage());
            }
        }
    }
}
