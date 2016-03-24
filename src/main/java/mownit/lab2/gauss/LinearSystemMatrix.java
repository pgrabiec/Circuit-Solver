package mownit.lab2.gauss;



public interface LinearSystemMatrix extends Matrix {
    void setVectorValue(int row, double value);
    double[] getValuesVector();
    double getVectorValue(int row);
    void undoSwapsIdentitySolved();
}
