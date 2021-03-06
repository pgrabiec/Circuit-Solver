package pgrabiec.mownit.circuitSolver.implemantation;

import pgrabiec.mownit.circuitSolver.Matrix;

public class DefaultMatrix implements Matrix {
    /** matrix[row][column] */
    protected final double[][] matrix;

    /** size of a row */
    protected final int width;

    /** size of a column */
    protected final int height;

    /**
     * Indicates variable-column association current swap status.
     *
     * a field in the variableAssociation of: variableAssociation[i] stands for:
     * variable number variableAssociation[i] is associated with column number i
     * */
    protected final int[] variableAssociation;

    public DefaultMatrix(double[][] matrix) {
        this.matrix = matrix;


        this.height = matrix.length;
        this.width = matrix[0].length;
        this.variableAssociation = new int[width];

        for (int i=0; i<width; i++) {
            this.variableAssociation[i] = i;
        }
    }


    public void swapColumns(int column1, int column2) {
        rememberSwapping(column1, column2);

        double tmp;

        for (int i = 0; i< height; i++) {
            tmp = matrix[i][column1];
            matrix[i][column1] = matrix[i][column2];
            matrix[i][column2] = tmp;
        }
    }

    public void swapRows(int row1, int row2) {
        double tmp;

        for (int i = 0; i< width; i++) {
            tmp = matrix[row1][i];
            matrix[row1][i] = matrix[row2][i];
            matrix[row2][i] = tmp;
        }
    }

    public void multiplyRow(int row, double value) {
        for (int i = 0; i< width; i++) {
            matrix[row][i] *= value;
        }
    }

    public void divideRow(int row, double value) {
        if (value == 0.0) {
            throw new IllegalArgumentException("attempt to divide by 0 row number " + row);
        }

        for (int i = 0; i< width; i++) {
            matrix[row][i] /= value;
        }
    }

    public void subtractRowByAnother(int rowToBeSubtracted, int subtractionValuesRow, double multiplier) {
        for (int column = 0; column< width; column++) {
            matrix[rowToBeSubtracted][column] -= matrix[subtractionValuesRow][column] * multiplier;
        }
    }

    public void setMatrixValue(int row, int column, double value) {
        matrix[row][column] = value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public double getMatrixValue(int row, int column) {
        return matrix[row][column];
    }

    public void pivoteRow(int row, int startColumn) {
        int largestColumn = startColumn;
        double largestValue = Math.abs(matrix[row][largestColumn]);

        double currentValue;
        for (int column = startColumn+1; column< width; column++) {
            currentValue = Math.abs(matrix[row][column]);
            if (currentValue > largestValue) {
                largestValue = currentValue;
                largestColumn = column;
            }
        }

        if (largestColumn != startColumn) {
            swapColumns(largestColumn, startColumn);
        }
    }

    public void pivoteColumn(int startRow, int column) {
        int largestRow = startRow;
        double largestValue = Math.abs(matrix[largestRow][column]);

        double currentValue;
        for (int row = startRow+1; row< width; row++) {
            currentValue = Math.abs(matrix[row][column]);
            if (currentValue > largestValue) {
                largestValue = currentValue;
                largestRow = row;
            }
        }

        if (largestRow != startRow) {
            swapRows(largestRow, startRow);
        }
    }

    public void pivoteFull(int row, int column) {
        pivoteRow(row, column);
        pivoteColumn(row, column);
    }

    public void undoSwaps() {
        for (int column = 0; column < width; column++) {
            int variableIdCurr = variableAssociation[column];
            if (variableIdCurr != column) {
                int column2 = getColumnByVariableId(column, column);
                swapColumns(column, column2);
            }
        }
    }

    public boolean removeZeroFromDiagonal(int rowAndColumn) {
        if (matrix[rowAndColumn][rowAndColumn] != 0.0) {
            return true;
        }

        for (int row = height-1; row > rowAndColumn; row--) {
            if (matrix[row][rowAndColumn] != 0.0) {
                swapRows(row, rowAndColumn);
                return true;
            }
        }

        return false;
    }

    public void print() {
        for (int row = 0; row< width; row++) {
            for (int column = 0; column< width; column++) {
                System.out.print(matrix[row][column] + " ");
            }
            System.out.println();
        }
    }


    private void rememberSwapping(int column1, int column2) {
        int varTmp = variableAssociation[column1];
        variableAssociation[column1] = variableAssociation[column2];
        variableAssociation[column2] = varTmp;
    }

    protected int getColumnByVariableId(int variableId, int startIndex) {
        for (int i = startIndex; i< width; i++) {
            if (variableAssociation[i] == variableId) {
                return i;
            }
        }

        throw new IllegalArgumentException("cannot acquire column association for variable id " + variableId + " and width " + width);
    }
}
