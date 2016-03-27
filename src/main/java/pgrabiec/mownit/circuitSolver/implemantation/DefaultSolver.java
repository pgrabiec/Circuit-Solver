package pgrabiec.mownit.circuitSolver.implemantation;

import pgrabiec.mownit.circuitSolver.LinearSystemMatrix;
import pgrabiec.mownit.circuitSolver.LinearSystemSolver;
import pgrabiec.mownit.circuitSolver.Matrix;

public class DefaultSolver implements LinearSystemSolver {

    /**
     * Solves the LinearSystemMatrix as a result of transformations
     * resulting in the state of matrices:
     *
     * matrix - identity matrix (1 on diagonal, zeros elsewhere)
     * values - one dimensional matrix containing values of corresponding
     *      variables in the linear system
     * */
    public void solve(LinearSystemMatrix matrix) {
        // Achieve zeros under the main diagonal
        makeMatrixDiagonalized(matrix);

        // Achieve zeros everywhere but the diagonal which consists if <code>1</code>
        makeMatrixIdentitySolved(matrix);

        // Undo variable permutation because of column swaps
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
                        double multiplier = matrix.getMatrixValue(row, column)
                                / matrix.getMatrixValue(column, column);

                        matrix.subtractRowByAnother(row, column, multiplier);
                        matrix.setMatrixValue(row, column, 0.0);
                    }
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

    /**
     * Decomposes the matrix A into l and u:
     * l * u = A
     *
     * Starts with diagonalizing matrix l and copying A into u
     *
     * Then, zeroes the u by subtracting rows below diagonal
     * and the multiplier is written in the corresponding place
     * to the matrix u
     * */
    public void luDecompose(double[][] A, Matrix l, Matrix u) {
        int size = l.getWidth();

        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                u.setMatrixValue(i, j, A[i][j]);
            }
        }

        initLowerTriangularMatrix(l);

        for (int column = 0; column < size-1; column++) {

            l.pivoteColumn(column, column);

            boolean diagonalColumnZeroed = false;
            if (u.getMatrixValue(column, column) == 0.0) {
                diagonalColumnZeroed = !u.removeZeroFromDiagonal(column);
            }

            if (!diagonalColumnZeroed) {

                for (int row = column + 1; row < size; row++) {

                    double multiplier =
                            u.getMatrixValue(row, column)
                            / u.getMatrixValue(column, column);

                    u.subtractRowByAnother(row, column, multiplier);
                    u.setMatrixValue(row, column, 0);
                    l.setMatrixValue(row, column, multiplier);
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
