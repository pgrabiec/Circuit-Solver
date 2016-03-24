package mownit.lab2.gauss;

import mownit.lab2.gauss.graph.CurrentComputation;
import mownit.lab2.gauss.graph.io.EdgesInput;
import mownit.lab2.gauss.graph.processing.GraphBuilder;
import mownit.lab2.gauss.graph.processing.GraphVisualizer;
import mownit.lab2.gauss.implemantation.DefaultSystemMatrix;
import mownit.lab2.gauss.implemantation.Solver;
import org.graphstream.graph.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try {

            Graph graph = GraphBuilder.parseEdges(
                    EdgesInput.getEdges(
                            "resistance.csv",
                            "voltage.csv"
                    )
            );

            //GraphVisualizer.initAndDisplayGraph(graph);

            CurrentComputation.computeCurrent(graph);

            GraphVisualizer.reloadGraphInfo(graph);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void test() {
        int size = 700;

        double[][] A = new double[size][size];

        double[] values = new double[size];

        LinearSystemMatrix linearSystemMatrix = new DefaultSystemMatrix(A, values);

        getExample(A, values);

        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(
                            "matrix"
                    )
            );

            for (int i=0; i<A.length; i++) {
                for (int j=0; j<A[0].length; j++) {
                    writer.write(A[i][j] + " ");
                }
                writer.write("\n");
            }

            BufferedWriter writer2 = new BufferedWriter(
                    new FileWriter(
                            "values"
                    )
            );

            for (int i=0; i<values.length; i++) {
                writer2.write(values[i] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Solver().solve(linearSystemMatrix);
    }

    private static double[] getExample(double[][] matrix, double[] values) {
        int size = values.length;

        double[] solution = new double[size];

        for (int i=0; i<size; i++) {
            solution[i] = getRandomDouble();
        }

        Random random = new Random();

        int limit = 10;

        for (int row=0; row<size; row++) {
            values[row] = 0;
            for (int column=0; column<size; column++) {
                int coefficient = random.nextInt() % limit;
                matrix[row][column] = coefficient;
                values[row] += coefficient * solution[column];
            }
        }

        return solution;
    }

    private static double getRandomDouble() {
        double step = 1.0;
        int limit = 10;

        return step * (new Random().nextInt() % limit);
    }
}
