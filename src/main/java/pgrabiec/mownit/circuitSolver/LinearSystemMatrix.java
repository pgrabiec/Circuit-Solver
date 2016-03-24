package pgrabiec.mownit.circuitSolver;



public interface LinearSystemMatrix extends Matrix {
    void setVectorValue(int row, double value);
    double[] getValuesVector();
    double getVectorValue(int row);
    void undoSwapsIdentitySolved();
}
