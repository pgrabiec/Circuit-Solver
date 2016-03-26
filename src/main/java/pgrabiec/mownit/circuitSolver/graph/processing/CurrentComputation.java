package pgrabiec.mownit.circuitSolver.graph.processing;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import pgrabiec.mownit.circuitSolver.graph.Const;
import pgrabiec.mownit.circuitSolver.graph.Cycle;
import pgrabiec.mownit.circuitSolver.implemantation.DefaultSystemMatrix;
import pgrabiec.mownit.circuitSolver.implemantation.DefaultSolver;

import java.util.Iterator;
import java.util.List;

public class CurrentComputation {
    public static void computeCurrent(Graph graph) {
        int edgeCount = graph.getEdgeCount();
        int nodeCount = graph.getNodeCount();

        List<Cycle> cycles = CycleFinder.findCycles(graph);
        int cyclesCount = cycles.size();

        double[][] matrix = new double[cyclesCount + nodeCount][edgeCount];
        double[] values = new double[cyclesCount + nodeCount];
        for (int i=0; i < cyclesCount + nodeCount; i++) {
            for (int j=0; j < edgeCount; j++) {
                matrix[i][j] = 0.0;
            }
            values[i] = 0.0;
        }

        int row = 0;

        for (Cycle cycle : cycles) {
            addEquation(cycle, matrix, values, row);
            row++;
        }

        for (Node node : graph.getEachNode()) {
            addEquation(node, matrix, values, row);
            row++;
        }

        double[][] m = new double[matrix.length][matrix[0].length];
        double [] v = new double[values.length];

        for (int i=0; i<matrix.length; i++) {
            System.arraycopy(matrix[i], 0, m[i], 0, matrix[0].length);
            v[i] = values[i];
        }

        new DefaultSolver().solve(
                new DefaultSystemMatrix(
                        matrix,
                        values
                )
        );

        for (int i=0; i<edgeCount; i++) {
            graph.getEdge(i).addAttribute(Const.CURRENT_ATTRIBUTE, values[i]);
        }
    }

    private static void addEquation(Cycle cycle, double[][] matrix, double[] values, int row) {
        if (cycle == null) {
            throw new IllegalArgumentException("passed empty cycle");
        }
        if (cycle.nodes == null) {
            throw new IllegalArgumentException("passed empty cycle");
        }
        if (cycle.nodes.length < 3) {
            throw new IllegalArgumentException("cycle length too low: " + cycle.nodes.length);
        }

        Node from_node = cycle.nodes[0];
        int id_from = Integer.parseInt(from_node.getId());

        Node to_node;
        int id_to;

        Edge edge;
        double voltageSum = 0.0;

        for (int i=1; i<cycle.nodes.length+1; i++) {
            to_node = cycle.nodes[i % cycle.nodes.length];
            edge = from_node.getEdgeBetween(to_node);
            id_to = Integer.parseInt(to_node.getId());

            if (edge == null) {
                throw new IllegalArgumentException("edge between " + from_node.getId()
                        + " and " + to_node.getId() + " does not exist");
            }


            double sign;

            if (id_to > id_from) {
                sign = 1.0;
            } else {
                sign = -1.0;
            }


            matrix
                    [row]
                    [edge.getIndex()]
                    =
                    ((Double) edge.getAttribute(Const.RESISTANCE_ATTRIBUTE))
                            * sign;

            if (edge.hasAttribute(Const.VOLTAGE_ATTRIBUTE)) {
                voltageSum += sign * ((Double) edge.getAttribute(Const.VOLTAGE_ATTRIBUTE));
            }

            from_node = to_node;
            id_from = id_to;
        }

        values[row] = voltageSum;
    }

    private static void addEquation(Node node, double[][] matrix, double[] values, int row) {
        int currentId = Integer.parseInt(node.getId());

        int neighbourId;
        Node neighbour;

        Edge edge;

        Iterator<Node> neighbours = node.getNeighborNodeIterator();
        while (neighbours.hasNext()) {
            neighbour = neighbours.next();
            neighbourId = Integer.parseInt(neighbour.getId());

            double sign = (neighbourId > currentId) ? 1.0 : -1.0;

            edge = node.getEdgeBetween(neighbour);

            matrix[row][edge.getIndex()] = sign;
        }

        values[row] = 0.0;
    }
}
