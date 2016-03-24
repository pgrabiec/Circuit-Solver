package pgrabiec.mownit.circuitSolver.implemantation;

import pgrabiec.mownit.circuitSolver.LinearSystemMatrix;
import pgrabiec.mownit.circuitSolver.LinearSystemSolver;
import pgrabiec.mownit.circuitSolver.Matrix;

public class Solver implements LinearSystemSolver {

    /**
     * Solves the LinearSystemMatrix as a result of transformations
     * resulting in the state of matrices:
     *
     * matrix - identity matrix (1 on diagonal, zeros elsewhere)
     * values - one dimensional matrix containing values of corresponding
     *      variables in the linear system
     * */
    public void solve(LinearSystemMatrix matrix) {
        makeMatrixDiagonalized(matrix);

        makeMatrixIdentitySolved(matrix);

        matrix.undoSwapsIdentitySolved();
    }

    /**
     * Transforms matrix so it has zeros everywhere below the diagonal.
     * May perform column swaps
     * */
    private void makeMatrixDiagonalized(LinearSystemMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        for (int column = 0; column < width; column++) {
            matrix.pivoteFull(column, column);

            boolean diagonalColumnZeroed = false;
            if (matrix.getMatrixValue(column, column) == 0.0) {
                diagonalColumnZeroed = !matrix.removeZeroFromDiagonal(column);
            }

            if (!diagonalColumnZeroed) {

                for (int row = column + 1; row < height; row++) {
                    if (matrix.getMatrixValue(row, column) != 0.0) {
                        double multiplicant = matrix.getMatrixValue(row, column)
                                / matrix.getMatrixValue(column, column);

                        matrix.substractRowByAnother(row, column, multiplicant);
                        matrix.setMatrixValue(row, column, 0.0);
                    }
                    //System.out.println("Step:");
                    //matrix.print();
                    //System.out.println();
                }
            }
        }
    }

    /**
     * Transforms diagonalized matrix in such way that the only values
     * it contains are 1.0 and are only on the diagonal.
     * The values vector is computed accordingly.
     *
     * Note that if previous column swaps were performed, the values vector
     * is not in the permutation that conforms initial variables association.
     * In order to reach a complete result, it is necessary to reorder the matrix
     * according to column swaps performed.
     * */
    private void makeMatrixIdentitySolved(LinearSystemMatrix matrix) {
        int size = matrix.getWidth();
        for (int row = size-1; row >= 0; row--) {

            double sum = 0.0;
            for (int column = row + 1; column < size; column++) {
                sum += matrix.getMatrixValue(row, column) * matrix.getVectorValue(column);
                matrix.setMatrixValue(row, column, 0.0);
            }

            matrix.setVectorValue(
                    row,
                    (matrix.getVectorValue(row) - sum) / matrix.getMatrixValue(row, row));
            matrix.setMatrixValue(row, row, 1.0);
        }
    }

    public void LUDecompose(double[][] A, Matrix l, Matrix u) {
        int size = l.getWidth();

        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                u.setMatrixValue(i, j, A[i][j]);
            }
        }

        initLowerTriangularMatrix(l);

        System.out.println("U init:");
        u.print();
        System.out.println();
        System.out.println("L init:");
        l.print();
        System.out.println();

        for (int column = 0; column < size-1; column++) {

            boolean diagonalColumnZeroed = false;
            if (u.getMatrixValue(column, column) == 0.0) {
                diagonalColumnZeroed = !u.removeZeroFromDiagonal(column);
            }

            if (!diagonalColumnZeroed) {

                for (int row = column + 1; row < size; row++) {

                    double multiplicant =
                            u.getMatrixValue(row, column)
                            / u.getMatrixValue(column, column);

                    u.substractRowByAnother(row, column, multiplicant);
                    u.setMatrixValue(row, column, 0);
                    l.setMatrixValue(row, column, multiplicant);
                }
            }
        }
    }

    private void initLowerTriangularMatrix(Matrix l) {
        for (int row = 0; row < l.getWidth(); row++) {
            for (int column = row+1; column < l.getWidth(); column++) {
                l.setMatrixValue(row, column, 0.0);
            }
            l.setMatrixValue(row, row, 1.0);
        }
    }
}
