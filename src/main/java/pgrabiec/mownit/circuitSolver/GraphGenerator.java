package pgrabiec.mownit.circuitSolver;

import pgrabiec.mownit.circuitSolver.graph.CircuitEdge;

import java.util.*;

public class GraphGenerator {
    /**
     * Generates list of edges of random parameters
     *
     * Starting with <param>startPerimeter</param> nodes connected with each other in pairs
     *
     * The graph grows in each iteration in the way that upon an iteration the graph
     *      contains an interval of recently added nodes (initially that is
     *      from 0 to <param>startPerimeter</param>
     *
     * Each iteration adds two times the number of recently added nodes and currently added nodes
     *      are connected with each other and each node created in the last iteration
     *      is connected with two other nodes created in the current iteration
     *
     * @param size number of iterations
     * @param startPerimeter initial number of nodes
     * */
    public static Collection<CircuitEdge> getSampleCircuit(int startPerimeter, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be less than 0 -> " + size);
        }
        if (startPerimeter < 1) {
            startPerimeter = 1;
        }

        int maxPerimeter = getEdgesResultCount(startPerimeter, size);

        List<CircuitEdge> edges = new ArrayList<CircuitEdge>(maxPerimeter);

        Random random = new Random();

        initEdges(startPerimeter, edges, random);

        int start = 1;
        int end = startPerimeter;
        int newEnd;

        for (int i=0; i<size; i++) {
            newEnd = addEnvelope(edges, start, end, random);
            start = end + 1;
            end = newEnd;
        }

        return edges;
    }

    /**
     * @return id of the last added node in the envelope
     * */
    private static int addEnvelope(Collection<CircuitEdge> edges, int lastStart, int lastEnd, Random random) {
        int lastCount = lastEnd - lastStart + 1;  // Number of edges added in previous iteration

        int current1 = lastEnd + 1;
        int current2 = lastEnd + 2;
        int last = lastStart;


        for (int i=0; i<lastCount; i++) {
            edges.add(getEdgeRandomValues(
                            last,
                            current1,
                            random
            ));

            edges.add(getEdgeRandomValues(
                            last,
                            current2,
                            random
            ));

            edges.add(getEdgeRandomValues(
                            current1,
                            current2,
                            random
            ));

            if (i != 0) {
                edges.add(getEdgeRandomValues(
                        current1,
                        current1 - 1,
                        random
                ));
            }
            if (i == lastCount - 1) {
                edges.add(getEdgeRandomValues(
                        lastEnd + 1,
                        current2,
                        random
                ));
            }

            current1 += 2;
            current2 += 2;
            last++;
        }

        return lastEnd + lastCount * 2;
    }

    /**
     * @return  CircuitEdge conforming to <param>from</param> and
     *          <param>to</param> nodes id
     *          Resistance and voltage are randomized
     * */
    private static CircuitEdge getEdgeRandomValues(int from, int to, Random random) {
        return new CircuitEdge(from, to, random.nextDouble() % 10.0 + 1.0, random.nextDouble() % 10.0);
    }

    /**
     * Adds initial number of edges to the edges collection
     * */
    private static void initEdges(int startPerimeter, Collection<CircuitEdge> edges, Random random) {
        for (int i=1; i < startPerimeter; i++) {
            edges.add(
                    getEdgeRandomValues(i, i+1, random)
            );
        }
        edges.add(getEdgeRandomValues(startPerimeter, 1, random));
    }

    /**
     * Computes the number of edges created by this.addEnvelope method
     * */
    private static int getEdgesResultCount(int startPerimeter, int size) {
        int currentPerimeter = startPerimeter;

        int result = startPerimeter;

        for (int i=0; i<size; i++) {
            currentPerimeter *= 2;
            result += currentPerimeter;
        }

        return result;
    }
}
