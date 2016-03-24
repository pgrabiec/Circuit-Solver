package mownit.lab2.gauss.graph.io;

import mownit.lab2.gauss.graph.CircuitEdge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class EdgesInput {
    public static List<CircuitEdge> getEdges(String resistanceFileName, String voltageFileName) throws IOException {
        List<CircuitEdge> edges = new LinkedList<CircuitEdge>();

        BufferedReader resistanceReader = null;
        BufferedReader voltageReader = null;

        try {
            resistanceReader = new BufferedReader(
                    new FileReader(
                            resistanceFileName
                    )
            );

            voltageReader = new BufferedReader(
                    new FileReader(
                            voltageFileName
                    )
            );

            String line;
            String[] data;

            int nodeFrom;
            int nodeTo;

            while (resistanceReader.ready()) {
                line = resistanceReader.readLine();
                data = line.split(" ");

                nodeFrom = Integer.valueOf(data[0]);
                nodeTo = Integer.valueOf(data[1]);

                if (nodeFrom > nodeTo) {
                    int tmp = nodeFrom;
                    nodeFrom = nodeTo;
                    nodeTo = tmp;
                }

                edges.add(
                        new CircuitEdge(
                                nodeFrom,
                                nodeTo,
                                Double.valueOf(data[2]),
                                0.0
                        )
                );
            }

            while (voltageReader.ready()) {
                line = voltageReader.readLine();
                data = line.split(" ");

                nodeFrom = Integer.valueOf(data[0]);
                nodeTo = Integer.valueOf(data[1]);

                if (nodeFrom > nodeTo) {
                    int tmp = nodeFrom;
                    nodeFrom = nodeTo;
                    nodeTo = tmp;
                }

                edges.add(
                        new CircuitEdge(
                                nodeFrom,
                                nodeTo,
                                0.0,
                                Double.valueOf(data[2])
                        )
                );
            }

        } finally {
            if (resistanceReader != null) {
                resistanceReader.close();
            }
            if (voltageReader != null) {
                voltageReader.close();
            }
        }

        return edges;
    }
}
