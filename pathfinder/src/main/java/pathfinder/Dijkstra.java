package pathfinder;
import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;


public class Dijkstra {
    // Dijkstra's algorithm assumes a graph with nonnegative edge weights.
    // Each element is a path from start to a given node.
    // A path's “priority” in the queue is the total cost of that path.
    // Nodes for which no path is known yet are not in the queue.

    // this method takes a start element and an end element and returns the smallest cost map of those
    // points in the graph parameter
    // E represents the data type of the start and end
    public static <E> Path<E> findMinPath(E start, E dest, Graph<E, Double> graph){

        PriorityQueue<Path<E>> active = new PriorityQueue<>(Comparator.comparingDouble(Path::getCost));
        HashSet<E> finished = new HashSet<>();
        active.add(new Path<>(start));

        while (active.size() != 0) {
            Path<E> minPath = active.remove();
            E minDest = minPath.getEnd();
            if (minDest.equals(dest)) {
                return minPath;
            }
            if (finished.contains(minDest)) {
                continue;
            }
            for (Graph.Edge<E, Double> tempEdge: graph.getEdges(minDest)) {
                if (!finished.contains(tempEdge.getChild())) {
                    Path<E> newPath = minPath.extend(tempEdge.getChild(), tempEdge.getLabel());
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        return null;
    }
}
