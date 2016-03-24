package mownit.lab2.gauss.graph.processing;

import mownit.lab2.gauss.graph.Const;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

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
                label += e.getAttribute(Const.RESISTANCE_ATTRIBUTE) + "[Ohm]";
            }

            if (e.hasAttribute(Const.VOLTAGE_ATTRIBUTE)) {
                label += " " + e.getAttribute(Const.VOLTAGE_ATTRIBUTE) + " [V]";
            }

            if (e.hasAttribute(Const.CURRENT_ATTRIBUTE)) {
                double current = e.getAttribute(Const.CURRENT_ATTRIBUTE);
                double absCurrent = Math.abs(current);
                label += " " + current + " [A]";

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
            setEdgesColor(graph, minCurrent, maxCurrent);
        }

        for (Node n : graph.getEachNode()) {
            n.setAttribute("ui.label", n.getId());
        }

        graph.display();
    }

    /**
     * Sets edges colours appropriately to the current on the edge
     * @param graph Graph to be modified
     * @param maxCurrent maximal current value in the graph
     * @param minCurrent minimal current value in the graph
     * */
    private static void setEdgesColor(Graph graph, double minCurrent, double maxCurrent) {
        String attributes = "";
        for (Edge e : graph.getEachEdge()) {
            double current = Math.abs((Double) e.getAttribute(Const.CURRENT_ATTRIBUTE));

            int redValue = getValueScaled(255, current, 0.0, maxCurrent);

            attributes += "edge#\"" + e.getId() + "\" {\nfill-color: rgb(" + redValue + ",0,0);\n}\n";

            System.out.println("ID:" + e.getId() + " CURRENT:" + current + " min:" + minCurrent + " max:" + maxCurrent + " RED:" + redValue);
        }
        graph.addAttribute("ui.stylesheet", attributes);
    }

    /**
     * @param lowerLimit the lowest value in the probed interval
     * @param upperLimit the highest value in the probed interval
     * @param value      the value contained by the interval
     * @param limit      maximal value that can be returned,
     *                   associated with the upperLimit
     *
     * @return integer between 0 and limit representing the value position within the interval.
     *         the greater is the value in comparison with the upperLimit and lowerLimit,
     *         the greater the return value will be: starting with value == lowerLimit (return 0)
     *         up to value == upperLimit (return limit)
     *
     * The return value is used for representing (current) values in the rgb color scale (limit == 255)
     * */
    private static int getValueScaled(int limit, double value, double lowerLimit, double upperLimit) {
        if (upperLimit < lowerLimit) {
            throw new IllegalArgumentException("lowerLimit " + lowerLimit
                    + " greater than the upperlimit " + upperLimit);
        }
        if (value < lowerLimit) {
            return 0;
        }
        if (value > upperLimit) {
            return limit;
        }
        if (lowerLimit == upperLimit) {
            return limit;
        }

        double length = upperLimit - lowerLimit;
        double relativePosition = value - lowerLimit;

        return (int) ((relativePosition / length) * limit);
    }
}
