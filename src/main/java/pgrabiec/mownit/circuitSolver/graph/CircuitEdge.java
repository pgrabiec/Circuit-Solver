package pgrabiec.mownit.circuitSolver.graph;


public class CircuitEdge {
    public final int FROM_NODE;
    public final int TO_NODE;
    public final double RESISTANCE;
    public final double VOLTAGE;


    public CircuitEdge(int from_id, int to_id, double resistance, double voltage) {
        FROM_NODE = from_id;
        TO_NODE = to_id;
        RESISTANCE = resistance;
        VOLTAGE = voltage;
    }
}
