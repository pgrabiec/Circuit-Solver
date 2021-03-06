package pgrabiec.mownit.circuitSolver.implemantation;

import pgrabiec.mownit.circuitSolver.LinearSystemMatrix;

public class DefaultSystemMatrix extends DefaultMatrix implements LinearSystemMatrix {
    protected final double[] values;

    public DefaultSystemMatrix(double[][] matrix, double[] values) {
        super(matrix);

        if (values.length != height) {
            throw new IllegalArgumentException("values vector width " + values.length + " does not match the matrix height: " + height);
        }

        this.values = values;
    }

    public void swapRows(int row1, int row2) {
        super.swapRows(row1, row2);

        double tmp;

        tmp = values[row1];
        values[row1] = values[row2];
        values[row2] = tmp;
    }

    public void multiplyRow(int row, double value) {
        super.multiplyRow(row, value);
        values[row] *= value;
    }

    public void divideRow(int row, double value) {
        super.divideRow(row, value);
        values[row] /= value;
    }

    public void subtractRowByAnother(int rowToBeSubtracted, int subtractionValuesRow, double multiplier) {
        super.subtractRowByAnother(rowToBeSubtracted, subtractionValuesRow, multiplier);

        values[rowToBeSubtracted] -= values[subtractionValuesRow] * multiplier;
    }

    public void setVectorValue(int row, double value) {
        values[row] = value;
    }

    public double[] getValuesVector() {
        return values;
    }

    public double getVectorValue(int row) {
        return values[row];
    }

    public void undoSwapsIdentitySolved () {
        for (int row = 0; row < width; row++) {
            int variableIdCurr = variableAssociation[row];
            if (variableIdCurr != row) {
                int row2 = getColumnByVariableId(row, row);
                swapVectorValues(row, row2);
            }
        }
    }

    private void swapVectorValues(int row, int row2) {
        double tmp = values[row];
        values[row] = values[row2];
        values[row2] = tmp;

        int tmp2 = variableAssociation[row];
        variableAssociation[row] = variableAssociation[row2];
        variableAssociation[row2] = tmp2;
    }

    public void print() {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                System.out.print(matrix[row][column] + " ");
            }
            System.out.println("| " + values[row]);
        }
    }
}
