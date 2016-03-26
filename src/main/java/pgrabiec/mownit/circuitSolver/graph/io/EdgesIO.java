package pgrabiec.mownit.circuitSolver.graph.io;

import pgrabiec.mownit.circuitSolver.graph.CircuitEdge;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class EdgesIO {
    private final String SOURCE_FILE;
    private final String SEPARATOR;

    public EdgesIO(String source_file, String separator) {
        SOURCE_FILE = source_file;
        SEPARATOR = separator;
    }

    public Collection<CircuitEdge> getEdges() throws IOException {
        List<CircuitEdge> edges = new LinkedList<CircuitEdge>();

        BufferedReader csvReader = null;

        try {
            csvReader = new BufferedReader(
                    new FileReader(
                            SOURCE_FILE
                    )
            );

            if (!csvReader.ready()) {
                throw new NoSuchElementException("empty source file");
            }

            csvReader.readLine(); // Skipping headers

            String line;

            while (csvReader.ready()) {
                line = csvReader.readLine();

                edges.add(
                        CircuitEdge.valueOf(line, SEPARATOR)
                );
            }
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }

        return edges;
    }

    public void writeEdges(Collection<CircuitEdge> edges) throws IOException {
        if (edges.isEmpty()) {
            return;
        }

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(
                    new FileWriter(
                            SOURCE_FILE,
                            false           // Not appending - overwriting
                    )
            );

            String[] headers = CircuitEdge.getHeaders();
            for (int i=0; i<headers.length; i++) {
                writer.write(headers[i]);
                if (i < headers.length - 1) {
                    writer.write(SEPARATOR);
                }
            }

            writer.write('\n');

            int count = 0;

            for (CircuitEdge edge : edges) {
                count++;
                writer.write(edge.toCsvLine(SEPARATOR));
                if (count < edges.size()) {
                    writer.write('\n');
                }
            }

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
