package org.insa.graphs.gui.simple.test;

import java.util.ArrayList;
import java.util.List;

public class TestScenario {
        public String mapName = "";
        public String description;
        public int originId;
        public int destinationId;
        public int arcInspectorId;
        public boolean expectedFeasible;

        public TestScenario(String mapName, String description, int originId, int destinationId, int arcInspectorId, boolean expectedFeasible) {
            this.mapName = mapName;
            this.description = description;
            this.originId = originId;
            this.destinationId = destinationId;
            this.arcInspectorId = arcInspectorId;
            this.expectedFeasible = expectedFeasible;
    }

        // Create test scenarios
public static List<TestScenario> createTestScenarios() {
    List<TestScenario> scenarios = new ArrayList<>();
    
    // Short path in road map (distance)
    scenarios.add(new TestScenario(
        "insa.mapgr", 
        "INSA short path",
        800,               
        900,                   
        0,            // Cost type(0  = Distance)       
        true       
    ));
    
    // Medium path in larger map (distance)
    scenarios.add(new TestScenario(
        "toulouse.mapgr",
        "Toulouse medium path",
        25578,
        30566,
        0,
        true
    ));

    /*
     // Short path in road map (time)
    scenarios.add(new TestScenario(
        "insa.mapgr",
        "INSA short path (time)",
        800,
        900,
        2,                      // Cost type (2 = time)
        true
    ));
    
    // Medium path in larger map (time)
    scenarios.add(new TestScenario(
        "toulouse.mapgr",
        "Toulouse medium path (time)",
        25578,
        30566,
        2,
        true
    ));
    */
    
    // Long path in large map
    scenarios.add(new TestScenario(
        "brazil.mapgr",
        "Brazil long path",
        5116028,
        147462,
        0,
        true
    ));
    
    // Non-road map
    scenarios.add(new TestScenario(
        "paris.mapgr",
        "Paris non-road map",
        10000,
        20000,
        0,
        true
    ));

    // Non-existent path
    scenarios.add(new TestScenario(
        "insa.mapgr",
        "INSA non-existent path",
        246,
        865,
        0,
        false
    ));

    // Same node (zero-length path)
    scenarios.add(new TestScenario(
        "insa.mapgr",
        "Same node path",
        800,                    // Same origin and destination
        800,
        0,
        true
    ));
    
    return scenarios;
}
        // Node node = graph.get(nodeId);
        @Override
        public String toString() {
            return description + " (" + mapName + ", " + (arcInspectorId == 0 ? "distance" : "time") + ")";
        }
        
    }