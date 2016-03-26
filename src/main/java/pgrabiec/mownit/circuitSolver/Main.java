package pgrabiec.mownit.circuitSolver;

import pgrabiec.mownit.circuitSolver.graph.processing.CurrentComputation;
import pgrabiec.mownit.circuitSolver.graph.io.EdgesIO;
import pgrabiec.mownit.circuitSolver.graph.processing.GraphBuilder;
import pgrabiec.mownit.circuitSolver.graph.processing.GraphVisualizer;
import pgrabiec.mownit.circuitSolver.implemantation.DefaultSystemMatrix;
import pgrabiec.mownit.circuitSolver.implemantation.DefaultSolver;
import org.graphstream.graph.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // Test linear system solving efficiency
        testLinearSystemSolving(2000);

        try {
            // Load graph from file or get an example for displaying
            executeGraphVisualisation();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *  Loads and displays circuit graph representation of random attributes
     * */
    private static void executeGraphVisualisation() throws IOException {
        // Create edges storage source file
        EdgesIO edgesIO = new EdgesIO("edges.csv", " ");

        // Get and save an example circuit graph
        edgesIO.writeEdges(
                GraphGenerator.getSampleCircuit(7, 1)
        );

        // Parse Graph from the edges source file
        Graph graph = GraphBuilder.parseEdges(
                edgesIO.getEdges()
        );

        // Display graph basic attributes (i.e. connections, resistance, voltage)
        GraphVisualizer.initAndDisplayGraph(graph);

        // Compute current on each edge based on the graph's basic attributes
        CurrentComputation.computeCurrent(graph);

        // Refresh the graph in order to display computed current attribute
        GraphVisualizer.reloadGraphInfo(graph);
    }

    /**
     * Saves an example of linear system matrix and values vector in separate files
     * Then solves the system and prints the elapsed time
     *
     * @param size size of the linear system matrix to be solved
     * */
    private static void testLinearSystemSolving(int size) {
        // Initialize linear system matrix
        double[][] matrix = new double[size][size];
        double[] values = new double[size];

        LinearSystemMatrix linearSystemMatrix = new DefaultSystemMatrix(matrix, values);

        getExample(matrix, values);

        // Save matrix and values vector (for efficiency comparison)
        try {
            BufferedWriter matrixWriter = new BufferedWriter(
                    new FileWriter(
                            "matrix"
                    )
            );
            for (int i=0; i<matrix.length; i++) {
                for (int j=0; j<matrix[0].length; j++) {
                    matrixWriter.write(matrix[i][j] + " ");
                }
                matrixWriter.write("\n");
            }
            matrixWriter.close();

            BufferedWriter writer2 = new BufferedWriter(
                    new FileWriter(
                            "values"
                    )
            );
            for (int i=0; i<values.length; i++) {
                writer2.write(values[i] + "\n");
            }
            writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Solve linear system and display time elapsed
        long startTime = System.currentTimeMillis();

        new DefaultSolver().solve(linearSystemMatrix);

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Solved linear system:\n" +
                "\tSize: " + size + " x " + size + "\n" +
                "\tTime: " + duration + " ms");
    }

    /**
     * @param matrix matrix of coefficients to be filled with random coefficients
     * @param values values vector to be filled with proper values that lead to
     *               a predefined solution vector
     *
     * @return linear system solution vector
     * */
    private static double[] getExample(double[][] matrix, double[] values) {
        int size = values.length;

        double[] solution = new double[size];

        Random random = new Random();

        for (int i=0; i<size; i++) {
            solution[i] = getRandomDouble(random);
        }

        int coefficientLimit = 10;

        for (int row=0; row<size; row++) {
            values[row] = 0;
            for (int column=0; column<size; column++) {
                int coefficient = random.nextInt() % coefficientLimit;
                matrix[row][column] = coefficient;
                values[row] += coefficient * solution[column];
            }
        }

        return solution;
    }

    private static double getRandomDouble(Random random) {
        double step = 1.0;
        int limit = 10;

        return step * (random.nextInt() % limit);
    }
}
