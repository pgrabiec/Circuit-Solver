package pgrabiec.mownit.circuitSolver.graph.processing;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import pgrabiec.mownit.circuitSolver.graph.Cycle;

import java.util.*;

public class CycleFinder {
    public static List<Cycle> findCycles(Graph graph) {
        List<Cycle> cycles = new LinkedList<Cycle>();

        /** If empty, the cycles searching ends
         * each leaving of a node provides entry consisting of:
         * (on top of stack):
         *      iterator to neighbours to be visited upon return to the node
         * */
        Stack<Iterator<Node>> neighboursStack = new Stack<Iterator<Node>>();

        /** Contains indices of nodes of the current DFS path
         * */
        Stack<Node> visitedStack = new Stack<Node>();

        /** indicates if the node of an index has been already visited
         * */
        boolean[] visited = new boolean[graph.getNodeCount()];
        for (int i = 0; i < graph.getNodeCount(); i++) {
            visited[i] = false;
        }


        visitedStack.push(graph.getNode(0));
        visited[0] = true;
        neighboursStack.push(graph.getNode(0).getNeighborNodeIterator());

        Iterator<Node> neighbours;
        Node currentNode;
        Node neighbour;

        while (!neighboursStack.empty()) {
            neighbours = neighboursStack.pop();
            currentNode = visitedStack.peek();

            if (neighbours.hasNext()) {

                neighbour = neighbours.next();

                if (visited[neighbour.getIndex()]) {
                    if (visitedStack.size() >= 3) {
                        if (neighbour != visitedStack.get(visitedStack.size() - 2)) {
                            visitedStack.push(neighbour);
                            extractCycle(visitedStack, cycles);
                            visitedStack.pop();
                        }
                    }
                    neighboursStack.push(neighbours);
                } else {
                    neighboursStack.push(neighbours);

                    visited[neighbour.getIndex()] = true;
                    visitedStack.push(neighbour);
                    neighboursStack.push(neighbour.getNeighborNodeIterator());
                }

            } else {
                visited[currentNode.getIndex()] = false;
                visitedStack.pop();
            }
        }

        return cycles;
    }

    private static void extractCycle(Stack<Node> nodesPath, List<Cycle> cycles) {
        Node startNode = nodesPath.peek();

        int cycleEnd = nodesPath.size() - 1;
        int cycleStart = 0;

        for (int i = nodesPath.size() - 2; i >= 0; i--) {
            if (nodesPath.get(i) == startNode) {
                cycleStart = i;
                break;
            }
        }

        if (cycleStart == 0 && nodesPath.peek() != nodesPath.get(0)) {
            throw new IllegalArgumentException("passed nodes stack does not contain a cycle");
        }

        int cycleLength = cycleEnd - cycleStart;

        Node[] resultCycle = new Node[cycleLength];
        for (int i = cycleStart; i < cycleEnd; i++) {
            resultCycle[i - cycleStart] = nodesPath.get(i);
        }

        Cycle result = new Cycle(resultCycle);

        if (!cycleAlreadyAdded(result, cycles)) {
            cycles.add(result);
        }
    }

    /**
     * Checks if the <param>cycles</param> already contains a cycle equivalent to the <param>cycle</param>
     *
     * @param cycle  cycle to be tested
     * @param cycles list of cycles the <param>cycle</param> is to be tested against
     *
     * @return  <code>true</code> if the <param>cycles</param> already contains an equivalent cycle
     *          <code>false</code> otherwise
     * */
    private static boolean cycleAlreadyAdded(Cycle cycle, Collection<Cycle> cycles) {
        for (Cycle added : cycles) {
            if (added.equals(cycle)) {
                return true;
            }
        }

        return false;
    }
}