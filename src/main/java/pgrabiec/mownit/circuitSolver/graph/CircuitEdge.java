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

    public static CircuitEdge valueOf(String csvLine, String seperator) {
        String[] data = csvLine.split(seperator);

        if (data.length < 4) {
            throw new IllegalArgumentException("the passed csv line does not contain" +
                    "enough data to create CircuitEdge object");
        }

        return new CircuitEdge(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                Double.parseDouble(data[2]),
                Double.parseDouble(data[3])
        );
    }

    public String toCsvLine(String separator) {
        StringBuilder result = new StringBuilder();

        result
                .append(FROM_NODE)
                .append(separator)
                .append(TO_NODE)
                .append(separator)
                .append(RESISTANCE)
                .append(separator)
                .append(VOLTAGE)
        ;

        return result.toString();
    }

    public static String[] getHeaders() {
        return new String[] {
                "from",
                "to",
                "resistance",
                "voltage"
        };
    }
}
