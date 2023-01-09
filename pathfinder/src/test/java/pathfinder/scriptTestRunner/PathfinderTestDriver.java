
package pathfinder.scriptTestRunner;

import graph.Graph;
import pathfinder.Dijkstra;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    private final Map<String, Graph<String,Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;
    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        // TODO: Implement this, reading commands from `r` and writing output to `w`.
        // See GraphTestDriver as an example.
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        //
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        Graph<String, Double> tempGraph = new Graph<>();

        graphs.put(graphName, tempGraph);
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> tempGraph;
        tempGraph = graphs.get(graphName);
        tempGraph.addNode(nodeName);
        output.printf("added node %s to %s%n", nodeName, graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabelString = arguments.get(3);
        double edgeLabel = Double.parseDouble(edgeLabelString);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {
        Graph<String, Double> tempGraph = graphs.get(graphName);
        tempGraph.addEdge(parentName, childName, edgeLabel);

        output.printf("added edge %.3f from %s to %s in %s%n", edgeLabel, parentName, childName,
                graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {

        Set<String> tempSet = graphs.get(graphName).listNodes();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(graphName).append(" contains: ");

        for (String node: tempSet) {
            strBuilder.append(node);
            strBuilder.append(" ");
        }

        strBuilder.deleteCharAt(strBuilder.length() - 1);
        output.println(strBuilder);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        List<String> childrenList = graphs.get(graphName).listChildren(parentName);
        List<Double> edgeList = graphs.get(graphName).listEdges(parentName);
        Set<String> combinedList = new TreeSet<>();

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.format("the children of %s in %s are: ", parentName, graphName));


        if (childrenList != null && edgeList != null) {
            for (int i = 0; i < childrenList.size(); i++) {
                combinedList.add(String.format("%s(%.3f)", childrenList.get(i), edgeList.get(i)));
            }
        }
        for (String combination:
                combinedList) {
            strBuilder.append(combination);
            strBuilder.append(" ");
        }
        String tempString = strBuilder.toString();
        output.println(tempString.substring(0 , tempString.length() - 1));
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String node_a = arguments.get(1);
        String node_b = arguments.get(2);
        findPath(graphName, node_a, node_b);
    }

    private void findPath(String graphName, String node_a, String node_b) {

        Graph<String, Double> tempGraph = graphs.get(graphName);
        Path<String> shortestPath = Dijkstra.findMinPath(node_a, node_b, tempGraph);

        if(!tempGraph.checkNode(node_a) || !tempGraph.checkNode(node_b)) {
            if (!tempGraph.checkNode(node_a)) {
                output.printf("unknown: %s%n", node_a);
            }
            if (!tempGraph.checkNode(node_a)) {
                output.printf("unknown: %s%n", node_b);
            }
        } else {
            output.printf("path from %s to %s:%n", node_a, node_b);
            if (shortestPath != null) {
                if (shortestPath.getCost() == 0) {
                    output.printf("%s to %s with weight %.3f%n", node_a, node_a, 0.000);
                }

                for (Path<String>.Segment<String> segment : shortestPath) {
                    output.printf("%s to %s with weight %.3f%n", segment.getStart(), segment.getEnd(), segment.getCost());
                }
                output.printf("total cost: %.3f%n", shortestPath.getCost());
            }
        }
    }
    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
