package org.insa.graphs.gui.simple;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
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
                ShortestPathData data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(scenario.arcInspectorId));
                
                // DIJKSTRA
                long startTimeDijkstra = System.currentTimeMillis();
                DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
                ShortestPathSolution dijkstraSolution = dijkstra.run();
                long dijkstraTime = (System.currentTimeMillis() - startTimeDijkstra)/1000;

                // Check if solution matches expected feasibility
                boolean pathExists = dijkstraSolution.getStatus() == AbstractSolution.Status.OPTIMAL;
                if (pathExists != scenario.expectedFeasible) System.err.println("Error: Not expected feasibility");

                 // A*
                    long startTimeAStar = System.currentTimeMillis();
                    AStarAlgorithm astar = new AStarAlgorithm(data);
                    ShortestPathSolution astarSolution = astar.run();
                    long astarTime = (System.currentTimeMillis() - startTimeAStar)/1000;

                    // Check if solution matches expected feasibility
                    boolean pathExists2 = astarSolution.getStatus() == AbstractSolution.Status.OPTIMAL;
                    if (pathExists2 != scenario.expectedFeasible) {
                        System.err.println("Error: Not expected feasibility");
                    }
                
                // If path exists, validate
                if (pathExists && pathExists2) {
                    Path dijkstraPath = dijkstraSolution.getPath();
                    boolean dijkstraValid = dijkstraPath.isValid();
                    double dijkstraCost = scenario.arcInspectorId == 0 ? dijkstraPath.getLength() : dijkstraPath.getMinimumTravelTime();
                    
                    System.out.println("Dijkstra result: Path valid: " + dijkstraValid + ", Cost: " + dijkstraCost + ", Time: " + dijkstraTime + "s");

                    Path aStarPath = astarSolution.getPath();
                    boolean aStarValid = aStarPath.isValid();
                    double aStarCost = scenario.arcInspectorId == 0 ? aStarPath.getLength() : aStarPath.getMinimumTravelTime();
                    
                    System.out.println("A* result: Path valid: " + aStarValid + ", Cost: " + aStarCost + ", Time: " + astarTime + "s");


                    // For small graphs, also run Bellman-Ford for comparison
                    if (graph.size() < 100000) {

                        //BELLMAN FORD
                        long startTimeBell = System.currentTimeMillis();
                        BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
                        ShortestPathSolution bellmanFordSolution = bellmanFord.run();
                        long bellTime = (System.currentTimeMillis() - startTimeBell)/1000;
                        
                        // Check if solution matches expected feasibility
                        boolean pathExists3 = bellmanFordSolution.getStatus() == AbstractSolution.Status.OPTIMAL;
                        if (pathExists3!= scenario.expectedFeasible) {
                            System.err.println("Error: Not expected feasibility");
                        }

                        if (bellmanFordSolution.getStatus() == AbstractSolution.Status.OPTIMAL) {
                            Path bellmanPath = bellmanFordSolution.getPath();
                            double bellmanCost = scenario.arcInspectorId == 0 ? bellmanPath.getLength() : bellmanPath.getMinimumTravelTime();
                            
                            // Compare costs
                            System.out.println("Bellman-Ford result: Path valid: " + bellmanPath.isValid() + ", Cost: " + bellmanCost + ", Time: " + bellTime + "s");
                            System.out.println("Cost difference Dijkstra Bellman-Ford: " + Math.abs(dijkstraCost - bellmanCost));
                            System.out.println("Cost difference A* Bellman-Ford: " + Math.abs(aStarCost - bellmanCost));
                        } else {
                            System.out.println("Bellman-Ford could not find a path.");
                        }
                    }
                } else {
                    System.out.println("Dijkstra and A* did not find a valid path.");
                }
                
            } catch (Exception e) {
                System.err.println("Error testing scenario " + scenario + ": " + e.getMessage());
            }
        }
    }
}
