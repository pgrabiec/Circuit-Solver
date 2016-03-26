package pgrabiec.mownit.circuitSolver;

/**
 * Extension for the simple Matrix
 *
 * Provides an abstraction for performing operations
 * on a linear system matrix of linear equations
 *
 * Consists of:
 *      1) Coefficients matrix
 *      2) Values vector
 * */

public interface LinearSystemMatrix extends Matrix {

    void setVectorValue(int row, double value);

    double[] getValuesVector();

    double getVectorValue(int row);


    /**
     * Provides a way to undo column swaps performed during solving
     * the linear system.
     *
     * Meant for retrieving the initial variables permutation of columns
     * WHEN AND ONLY WHEN the coefficients matrix is diagonalized
     * ie when the linear system is solved. The values vector representing
     * result is permuted appropriately
     * */
    void undoSwapsIdentitySolved();
}
