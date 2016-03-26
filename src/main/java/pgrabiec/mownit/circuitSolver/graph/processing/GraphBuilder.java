package pgrabiec.mownit.circuitSolver.graph.processing;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import pgrabiec.mownit.circuitSolver.graph.CircuitEdge;
import pgrabiec.mownit.circuitSolver.graph.Const;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GraphBuilder {
    public static Graph parseEdges(Collection<CircuitEdge> edges) {
        Graph graph = new SingleGraph("Circuit Graph");

        Set<Integer> nodesCreated = new HashSet<Integer>();

        Node from;
        Node to;
        Edge edge;

        for (CircuitEdge e : edges) {
            if (!nodesCreated.contains(e.FROM_NODE)) {
                from = graph.addNode(String.valueOf(e.FROM_NODE));
                nodesCreated.add(e.FROM_NODE);
            } else {
                from = graph.getNode(String.valueOf(e.FROM_NODE));
            }

            if (!nodesCreated.contains(e.TO_NODE)) {
                to = graph.addNode(String.valueOf(e.TO_NODE));
                nodesCreated.add(e.TO_NODE);
            } else {
                to = graph.getNode(String.valueOf(e.TO_NODE));
            }

            edge = from.getEdgeBetween(to);

            if (edge == null) {
                edge = graph.addEdge(from.getId() + " " + to.getId(), from, to, false);
            }


            if (e.VOLTAGE != 0.0) {
                if (edge.hasAttribute(Const.VOLTAGE_ATTRIBUTE)) {
                    edge.setAttribute(Const.VOLTAGE_ATTRIBUTE,
                            (Double) edge.getAttribute(Const.VOLTAGE_ATTRIBUTE) + e.VOLTAGE
                    );
                } else {
                    edge.setAttribute(Const.VOLTAGE_ATTRIBUTE, e.VOLTAGE);
                }
            }
            if (e.RESISTANCE != 0.0) {
                edge.setAttribute(Const.RESISTANCE_ATTRIBUTE, e.RESISTANCE);
            }
        }

        return graph;
    }
}
