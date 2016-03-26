package pgrabiec.mownit.circuitSolver.graph;

import org.graphstream.graph.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Cycle {
    public final Node[] nodes;

    public Cycle(Node[] nodes) {
        this.nodes = nodes;
    }

    /**
     * @return  <code>true</code> if the <param>anotherCycle</param>
     *          consists of the same nodes as this cycle
     *          <code>false</code> otherwise
     * */
    public boolean equals(Object anotherCycle) {
        if (anotherCycle == this) {
            return true;
        }

        if (!(anotherCycle instanceof Cycle)) {
            return false;
        }

        if (this.nodes.length != ((Cycle) anotherCycle).nodes.length) {
            return false;
        }

        Set<Node> firstCycle = new HashSet<Node>(this.nodes.length);

        Collections.addAll(firstCycle, this.nodes);

        for (Node node : ((Cycle) anotherCycle).nodes) {
            if (!firstCycle.contains(node)) {
                return false;
            }
        }

        return true;
    }
}
