package org.insa.graphs.gui.simple;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.simple.test.TestScenario;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;

// visit these directory to see the list of available files on commetud.

public class ShortestPathTestVisuelle {

    public static void main(String[] args) {
        List<TestScenario> scenarios = TestScenario.createTestScenarios();
        
        for (TestScenario scenario : scenarios) {
            System.out.println("");
            System.out.println("Testing: " + scenario);
            
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
                DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
                ShortestPathSolution dijkstraSolution = dijkstra.run();
                
                // Check if solution matches expected feasibility
                boolean pathExists = dijkstraSolution.getStatus() == AbstractSolution.Status.OPTIMAL;
                if (pathExists != scenario.expectedFeasible) {
                    System.err.println("Error: Not expected feasibility");
                }

                // Create variables for visual storage
                Path dijkstraPath = null;
                Path bellmanFordPath = null;
                double dijkstraCost = 0;
                double bellmanFordCost = 0;

                // Process Dijkstra results
                if (pathExists) {
                    dijkstraPath = dijkstraSolution.getPath();
                    boolean isValid = dijkstraPath.isValid();
                    dijkstraCost = scenario.arcInspectorId == 0 ? dijkstraPath.getLength() : dijkstraPath.getMinimumTravelTime();
                    
                    System.out.println("Dijkstra result: Path valid: " + isValid + 
                                      ", Cost: " + dijkstraCost);
                } else {
                    System.out.println("Dijkstra could not find a path.");
                }

                // For small graphs, also run Bellman-Ford for comparison (all maps except Brazil)
                if (graph.size() < 100000) {

                    //BELLMAN FORD
                    BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
                    ShortestPathSolution bellmanFordSolution = bellmanFord.run();
                    
                    // Calculate distance / length and print 
                    if (bellmanFordSolution.getStatus() == AbstractSolution.Status.OPTIMAL) {
                        bellmanFordPath = bellmanFordSolution.getPath();
                        //Get length or travel time based on looking for shortest or fastest
                        bellmanFordCost = scenario.arcInspectorId == 0 ? bellmanFordPath.getLength() : bellmanFordPath.getMinimumTravelTime();
                        
                        System.out.println("Bellman-Ford result: Path valid: " + bellmanFordPath.isValid() + ", Cost: " + bellmanFordCost);
                        System.out.println("Costs match: " + Math.abs(dijkstraCost - bellmanFordCost));
                    } else {
                        System.out.println("Bellman-Ford could not find a path.");
                    }
                }

                // Map display
                try {
                    // Create a latch to wait for the window to close
                    final CountDownLatch latch = new CountDownLatch(1);
                    
                    // Store the paths for drawing
                    final Path finalDijkstraPath = dijkstraPath;
                    final Path finalBellmanFordPath = bellmanFordPath;
                    
                    // Create drawing from Launch class
                    final Drawing drawing = Launch.createDrawing();
                    
                        // Try to draw
                        try {
                            drawing.drawGraph(graph);
                            
                            // Draw markers
                            drawing.drawMarker(origin.getPoint(), java.awt.Color.GREEN, java.awt.Color.BLACK, Drawing.AlphaMode.TRANSPARENT);
                            drawing.drawMarker(destination.getPoint(), java.awt.Color.YELLOW, java.awt.Color.BLACK, Drawing.AlphaMode.TRANSPARENT);
                            
                             // Draw paths
                            if (finalBellmanFordPath != null) drawing.drawPath(finalBellmanFordPath, java.awt.Color.GREEN, true);
                            if (finalDijkstraPath != null) drawing.drawPath(finalDijkstraPath, java.awt.Color.BLUE, true);
                            
                            // Code for the opening of the widow
                            javax.swing.JFrame frame = null;
                            java.awt.Component component = (java.awt.Component)drawing;
                            while (component != null && !(component instanceof javax.swing.JFrame)) component = component.getParent();
                            
                            if (component instanceof javax.swing.JFrame) {
                                frame = (javax.swing.JFrame)component;

                                // Change so closing window does not stop program
                                frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
                                frame.addWindowListener(new WindowAdapter() {
                                    @Override
                                    public void windowClosed(WindowEvent e) {
                                        System.out.println("Window closed event received");
                                        latch.countDown();
                                    }
                                    
                                    @Override
                                    public void windowClosing(WindowEvent e) {System.out.println("Window closing event received");}
                                });
                            }
                        } catch (Exception e) {
                            latch.countDown();
                        }
                
                    // Wait for the window to be closed
                    System.out.println("Displaying map. Close to continue.");
                    latch.await();
                    System.out.println("Window closed. Continuing if more tests.");
                } catch (Exception e) {
                    System.err.println("Error displaying map: " + e.getMessage());
                }
                
            } catch (Exception e) {
                System.err.println("Error testing scenario " + scenario + ": " + e.getMessage());
            }
        }
    }
}
