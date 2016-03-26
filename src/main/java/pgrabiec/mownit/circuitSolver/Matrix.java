package pgrabiec.mownit.circuitSolver;

/**
 * Matrix default orientation: getMatrixValue(row, column);
 *     0            column
 *      |----------->
 *      |
 *      |
 *      |
 *      |
 *  row V
 * */

public interface Matrix {
    /**
     * Swaps two different columns
     *
     * The swapping changes the variables permutation, therefore
     * swapping columns is remembered in order to enable initial
     * variable permutation retrieval
     * */
    void swapColumns(int column1, int column2);

    /**
     * Swaps two different rows without any particular consequences
     * */
    void swapRows(int row1, int row2);

    /**
     * @param row row to be multiplied
     * @param value multiplier to multiply the row
     * */
    void multiplyRow(int row, double value);

    /**
     * @param row row to be divided
     * @param value divisor to divide the row
     * */
    void divideRow(int row, double value);

    /**
     *  Subtracts row <param>rowToBeSubtracted</param> by another row,
     *  <param>subtractionValuesRow</param> multiplied by the <param>multiplier</param>
     * */
    void subtractRowByAnother(int rowToBeSubtracted, int subtractionValuesRow, double multiplier);

    void setMatrixValue(int row, int column, double value);

    int getWidth();

    int getHeight();

    double[][] getMatrix();

    double getMatrixValue(int row, int column);

    /**
     * If necessary, swaps appropriate columns in order for
     * the absolute value of the element in the row <param>row</param>
     * and column <param>column</param> to be the biggest absolute
     * value in the row
     * */
    void pivoteRow(int row, int column);

    /**
     * Adequate to this.pivoteRow(int, int) pivoting method, but
     * the searching area is a column instead of a row
     * */
    void pivoteColumn(int row, int column);

    /**
     * Performs operations resulting in
     * the same state as after calling methods:
     * this.pivoteRow(int, int) and
     * this.pivoteColumn(int, int)
     * */
    void pivoteFull(int row, int column);

    /**
     * Switches back the columns into variable permutation
     * equivalent to the initial variables permutations.
     *
     * Has no effect if there were no column swaps performed
     * on this Matrix
     * */
    void undoSwaps();

    /**
     * Prints this Matrix to System.out
     * */
    void print();

    /**
     * If there is a zero value in the diagonal of specified row and column,
     * The row containing the diagonal field is swapped with another one
     * that has non-zero value in the same column and the row is greater than
     * the diagonal row - meant for enabling Gaussian elimination if zero occurs
     * on the main diagonal.
     *
     * @return  true    if either the diagonal was made non-zero valued by swapping
     *                  two different rows or the value on the diagonal was already
     *                  non-zero valued
     *          false   if there is no row with greater index having non-zero value
     *                  that the diagonal can be swapped with.
     * */
    boolean removeZeroFromDiagonal(int rowAndColumn);
}
