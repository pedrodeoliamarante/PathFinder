
package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.*;


public class CampusMap implements ModelAPI {
    private final Graph<Point, Double> campusGraph;
    private final Map<String, String> namesMap;
    private final Map<String, Point> coordinateMap;
    private final List<CampusBuilding> campusBuildingsList;

    /**
     * Contracts the CampusMap Object
     * @spec.effects this
     * @spec.modifies initializes the state of campusMap
     */
    public CampusMap() {
        campusBuildingsList =
                CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> campusPathsList =
                CampusPathsParser.parseCampusPaths("campus_paths.csv");

        this.campusGraph = new Graph<>();
        this.namesMap = new HashMap<>();
        this.coordinateMap = new HashMap<>();

        for (CampusBuilding building: campusBuildingsList) {
            Point coordinate = new Point(building.getX(), building.getY());
            coordinateMap.put(building.getShortName(), coordinate);
            namesMap.put(building.getShortName(), building.getLongName());
        }

        for (CampusPath path: campusPathsList) {
            Point parentCoordinates = new Point(path.getX1(), path.getY1());
            Point childCoordinates = new Point(path.getX2(), path.getY2());
            this.campusGraph.addNode(childCoordinates);
            this.campusGraph.addEdge(parentCoordinates, childCoordinates, path.getDistance());
        }
    }


    @Override
    public boolean shortNameExists(String shortName) {
        return this.namesMap.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        if (!shortNameExists(shortName)) {
            throw new IllegalArgumentException();
        }
        return this.namesMap.get(shortName);
    }

    @Override
    public Map<String, String> buildingNames() {
        return this.namesMap;
    }

    // uses Dijkstra's so no negative-edge weights are allowed, and ties are broken arbitrarily
    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        if( startShortName == null || endShortName == null ||
        !shortNameExists(startShortName)  || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException();
        }

        Point startCoordinate = coordinateMap.get(startShortName);
        Point endCoordinate = coordinateMap.get(endShortName);

        if( startCoordinate.getX() < 0 || startCoordinate.getY() < 0 || endCoordinate.getX() < 0 || endCoordinate.getY() < 0) {
            throw new IllegalArgumentException("One of the coordinates is negative");
        }

        Path<Point> minPath = Dijkstra.findMinPath(startCoordinate, endCoordinate ,this.campusGraph);

        if (minPath == null) {
            throw new IllegalStateException("No path between nodes");
        }

        return Dijkstra.findMinPath(startCoordinate, endCoordinate ,this.campusGraph);
    }

    /**
     *  Returns a list of CampusBuildings stored in the object
     * @return list of CampusBuildings stored in the object
     */
    public List<CampusBuilding> listOfBuildings() {
        return this.campusBuildingsList;
    }

}
