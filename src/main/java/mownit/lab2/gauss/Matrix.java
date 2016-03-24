package mownit.lab2.gauss;

/**    0            column
 *      |----------->
 *      |
 *      |
 *      |
 *      |
 *  row V
 * */
public interface Matrix {
    void swapColumns(int column1, int column2);
    void swapRows(int row1, int row2);

    void multiplyRow(int row, double value);
    void divideRow(int row, double value);

    void substractRow(int row, double value);
    void addRow(int row, double value);

    void substractRowByAnother(int rowToBeSubstracted, int substractionValuesRow, double multiplicant);

    void setMatrixValue(int row, int column, double value);

    int getWidth();
    int getHeight();

    double[][] getMatrix();
    double getMatrixValue(int row, int column);

    void pivoteRow(int row, int column);
    void pivoteColumn(int row, int column);
    void pivoteFull(int row, int column);

    void undoSwaps();
    void print();
    void multiply(Matrix m1, Matrix m2, Matrix result);

    boolean removeZeroFromDiagonal(int rowAndColumn);
}
