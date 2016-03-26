package pgrabiec.mownit.circuitSolver.graph.processing;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import pgrabiec.mownit.circuitSolver.graph.Const;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GraphVisualizer {
    public static void initAndDisplayGraph(Graph graph) {
        reloadGraphInfo(graph);
        graph.display();
    }

    public static void reloadGraphInfo(Graph graph) {
        double minCurrent = Double.MAX_VALUE;
        double maxCurrent = Double.MIN_VALUE;

        for (Edge e : graph.getEachEdge()) {
            String label = "";

            if (e.hasAttribute(Const.RESISTANCE_ATTRIBUTE)) {
                label += round((Double) e.getAttribute(Const.RESISTANCE_ATTRIBUTE), 1) + "[Ohm] ";
            }

            if (e.hasAttribute(Const.VOLTAGE_ATTRIBUTE)) {
                label += round((Double) e.getAttribute(Const.VOLTAGE_ATTRIBUTE), 1) + " [V] ";
            }

            if (e.hasAttribute(Const.CURRENT_ATTRIBUTE)) {
                double current = e.getAttribute(Const.CURRENT_ATTRIBUTE);
                double absCurrent = Math.abs(current);

                label += round(current, 3) + " [A]";

                if (absCurrent > maxCurrent) {
                    maxCurrent = absCurrent;
                }
                if (absCurrent < minCurrent) {
                    minCurrent = absCurrent;
                }
            }

            e.setAttribute("ui.label", label);
        }

        if (minCurrent != Double.MAX_VALUE && maxCurrent != Double.MIN_VALUE) {
            setEdgesCurrentDistinction(graph, minCurrent, maxCurrent);
        }

        for (Node n : graph.getEachNode()) {
            n.setAttribute("ui.label", n.getId());
        }

        graph.addAttribute("ui.stylesheet", "node {\n" +
                "\ttext-size: " + Const.TEXT_SIZE + ";\n" +
                "}\n"
        );
    }

    /**
     * Sets edges colours and width appropriately to the current on the edge
     * @param graph Graph to be modified
     * @param maxCurrent maximal current value in the graph
     * @param minCurrent minimal current value in the graph
     * */
    private static void setEdgesCurrentDistinction(Graph graph, double minCurrent, double maxCurrent) {
        StringBuilder attributes = new StringBuilder();

        List<Edge> edges = new LinkedList<Edge>();
        Iterator<Edge> edgeIterator = graph.getEdgeIterator();
        while (edgeIterator.hasNext()) {
            edges.add(edgeIterator.next());
        }
        for (Edge e : edges) {
            makeEdgeDirected(e, graph);
        }

        for (Edge e : graph.getEachEdge()) {
            double current = Math.abs((Double) e.getAttribute(Const.CURRENT_ATTRIBUTE));


            int redValue = getValueScaled(0, 255, current, 0.0, maxCurrent);
            int edgeWidth = getValueScaled(Const.MIN_EDGE_WIDTH,
                    Const.MAX_EDGE_WIDTH, current, 0.0, maxCurrent);
            int arrowWidth = getValueScaled(Const.MIN_ARROW_WIDTH,
                    Const.MAX_ARROW_WIDTH, current, 0.0, maxCurrent);
            int arrowLength = getValueScaled(Const.MIN_ARROW_LENGTH,
                    Const.MAX_ARROW_LENGTH, current, 0.0, maxCurrent);

            attributes.append("edge#\"" + e.getId() + "\" {\n")
                    .append("fill-color: rgb(" + redValue + ",0,0);\n")
                    .append("size: " + edgeWidth + ";\n")
                    .append("arrow-shape: arrow;\n")
                    .append("arrow-size: " + arrowLength + ", " + arrowWidth + ";\n")
                    .append("}\n")
            ;

        }
        graph.addAttribute("ui.stylesheet", attributes.toString());
    }

    /**
     * @param lowerLimit  the lowest value in the probed interval
     * @param upperLimit  the highest value in the probed interval
     * @param value       the value contained by the interval
     * @param returnMax maximal value that can be returned,
     *                    associated with the upperLimit
     * @param returnMin minimal value that can be returned
     *
     * @return integer between 0 and returnMax representing the value position within the interval.
     *         the greater is the value in comparison with the upperLimit and lowerLimit,
     *         the greater the return value will be: starting with value == lowerLimit (return 0)
     *         up to value == upperLimit (return returnMax)
     *
     * The return value is used for representing (current) values in the rgb color scale (returnMax == 255)
     * */
    private static int getValueScaled(int returnMin, int returnMax, double value, double lowerLimit, double upperLimit) {
        if (upperLimit < lowerLimit) {
            throw new IllegalArgumentException("lowerLimit " + lowerLimit
                    + " greater than the upperlimit " + upperLimit);
        }
        if (value < lowerLimit) {
            return returnMin;
        }
        if (value > upperLimit) {
            return returnMax;
        }
        if (lowerLimit == upperLimit) {
            return returnMax;
        }

        double length = upperLimit - lowerLimit;
        double relativePosition = value - lowerLimit;

        return (int) ((relativePosition / length) * returnMax) + returnMin;
    }

    /**
     * @param edge  edge to be made directed depending on the current flow direction
     * @param graph the graph containing the <param>edge</param>
     * */
    private static void makeEdgeDirected(Edge edge, Graph graph) {
        graph.removeEdge(edge);

        Node from_node, to_node;

        from_node = edge.getSourceNode();
        to_node = edge.getTargetNode();

        boolean currentPositive = (Double) edge.getAttribute(Const.CURRENT_ATTRIBUTE) > 0.0;
        int from_id = Integer.parseInt(from_node.getId());
        int to_id = Integer.parseInt(to_node.getId());

        if (to_id < from_id) {
            if (currentPositive) {
                Node tmp = from_node;
                from_node = to_node;
                to_node = tmp;
            }
        } else if (!currentPositive) {
            Node tmp = from_node;
            from_node = to_node;
            to_node = tmp;
        }

        Edge newEdge = graph.addEdge(edge.getId(), from_node, to_node, true);

        for (String attrKey : edge.getAttributeKeySet()) {
            newEdge.addAttribute(attrKey, edge.getAttribute(attrKey));
        }

        //System.out.println("FROM: " + from_node.getId() + " TO: " + to_node.getId() + " CURRENT: " + edge.getAttribute(Const.CURRENT_ATTRIBUTE));
    }

    private static Double round(double value, int places) {
        double factor = Math.pow(10.0, places);
        return Math.floor(value * factor) / factor;
    }
}
