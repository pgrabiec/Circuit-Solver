# Circuit-Solver
The project for solving linear circuits via solving linear system by gaussian elimination mathod
and visualizing the result as a directed, labeled graph using GraphStream library.

The program processes provided data of vertexes and edges in the circuit and computes
current for each edge based on their resistance and edge current provided.
Default implementation creates a set of linear equations allowing to find the vector
of current induced on each edge.

Input of the program is provided by file edges.csv with default comma separator as ' ' (space).
There are four columns of data in the file for each edge in the circuit:

  - 'from'        (int)     circuit vertex id from which the edge comes from
  - 'to'          (int)     circuit vertex in which the edge ends
  - 'resistance'  (double)  resistance value of the edge
  - 'voltage'     (double)  current applied to the edge; if positive, the '+' pole
                            is on the side of 'to' vertex and the '-' pole
                            is on the side of 'from' vertex;
                            if negative, the polarization is inverted

Project is built with Maven. In order to make the project type: 'mvn package'
