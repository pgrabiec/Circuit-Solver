package mownit.lab2.gauss;

public interface LinearSystemSolver {

    /**
     * Solves linear system by setting values vector of <param>systemMatrix</param>
     * as if it's main matrix represented an identity matrix
     *
     * @param systemMatrix represents matrix of the linear system.
     *                     The object is modified and values vector
     *                     set according to the solution
     * */
    void solve(LinearSystemMatrix systemMatrix);

    /**
     * Decomposes the matrix A into:
     * @param L - lower triangular matrix
     * @param U - upper triangular matrix
     *
     * Such that A = L * U
     * */
    void LUDecompose(double[][] A, Matrix L, Matrix U);
}
