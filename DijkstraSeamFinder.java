package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPathFinder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class DijkstraSeamFinder implements SeamFinder {
    private final ShortestPathFinder<Graph<Point, Edge<Point>>, Point, Edge<Point>> pathFinder;

    public DijkstraSeamFinder() {
        this.pathFinder = createPathFinder();
    }

    protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
        /*
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
        */
        return new DijkstraShortestPathFinder<>();
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        DijkstraShortestPathFinder<EnergyGraph, Point, Edge<Point>> spf = new DijkstraShortestPathFinder<>();
        List<Point> sp = spf.findShortestPath(new EnergyGraph(energies, true),
            new Point(0, 0), new Point(energies.length - 1, energies[0].length - 1)).vertices();
        List<Integer> spInt = new ArrayList<>();

        if (sp.get(0).x == sp.get(1).x) {
            sp.remove(0);
        }
        if (sp.get(sp.size() - 1).x == sp.get(sp.size() - 2).x) {
            sp.remove(sp.size() - 1);
        }

        for (Point p : sp) {
            spInt.add(p.y);
        }

        return spInt;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        DijkstraShortestPathFinder<EnergyGraph, Point, Edge<Point>> spf = new DijkstraShortestPathFinder<>();
        List<Point> sp = spf.findShortestPath(new EnergyGraph(energies, false),
            new Point(0, 0), new Point(energies.length - 1, energies[0].length - 1)).vertices();
        List<Integer> spInt = new ArrayList<>();

        if (sp.get(0).y == sp.get(1).y) {
            sp.remove(0);
        }
        if (sp.get(sp.size() - 1).y == sp.get(sp.size() - 2).y) {
            sp.remove(sp.size() - 1);
        }

        for (Point p : sp) {
            spInt.add(p.x);
        }

        return spInt;
    }

    private class EnergyGraph implements Graph<Point, Edge<Point>> {
        //Vertex: Pixel
        double[][] energies;
        int xDir;
        int yDir;
        boolean isHorizontal;
        int xMax;
        int yMax;

        public EnergyGraph(double[][] energies, boolean isHorizontal) {
            this.energies = energies;
            if (isHorizontal) {
                xDir = 1;
                yDir = 0;
            } else {
                xDir = 0;
                yDir = 1;
            }
            xMax = energies.length - 1;
            yMax = energies[0].length - 1;

            this.isHorizontal = isHorizontal;
        }


        @Override
        public Collection<Edge<Point>> outgoingEdgesFrom(Point p) {
            Collection<Edge<Point>> c = new HashSet<>();

            addLastRow(c, p);

            if (isEndEdge(p)) {
                return c;
            }

            double firstEdgeWeight = addFirstRow(c, p);

            c.add(new Edge<>(p,
                new Point(p.x + xDir, p.y + yDir), energies[p.x + xDir][p.y + yDir] + firstEdgeWeight));

            int nextDiag = getDiagonal(p);
            if (isHorizontal) {
                if (nextDiag == 0 || nextDiag == 1) {
                    c.add(new Edge<>(p,
                        new Point(p.x + xDir, p.y + yDir + 1), energies[p.x + xDir][p.y + yDir + 1] + firstEdgeWeight));
                }
                if (nextDiag == 0 || nextDiag == -1) {
                    c.add(new Edge<>(p,
                        new Point(p.x + xDir, p.y + yDir - 1), energies[p.x + xDir][p.y + yDir - 1] + firstEdgeWeight));
                }
            } else {
                if (nextDiag == 0 || nextDiag == 1) {
                    c.add(new Edge<>(p,
                        new Point(p.x + xDir + 1, p.y + yDir), energies[p.x + xDir + 1][p.y + yDir] + firstEdgeWeight));
                }
                if (nextDiag == 0 || nextDiag == -1) {
                    c.add(new Edge<>(p,
                        new Point(p.x + xDir - 1, p.y + yDir), energies[p.x + xDir - 1][p.y + yDir] + firstEdgeWeight));
                }
            }
            return c;
        }

        private boolean isEndEdge(Point p) {


            if (xDir != 0) {
                return p.x == xMax;
            }
            return p.y == yMax;

        }

        private int getDiagonal(Point p) {
            if (isHorizontal) {
                if (p.y == yMax) {
                    return -1;
                } else if (p.y == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }

            if (p.x == xMax) {
                return -1;
            } else if (p.x == 0) {
                return 1;
            } else {
                return 0;
            }

        }

        private void addLastRow(Collection<Edge<Point>> c, Point p) {
            if (isHorizontal && p.x == xMax) {
                for (int i = 0; i <= yMax; i++) {
                    if (i != p.y) {
                        c.add(new Edge<>(p, new Point(xMax, i), 0));
                    }
                }
            }
            if (!isHorizontal && p.y == yMax) {
                for (int i = 0; i <= xMax; i++) {
                    if (i != p.x) {
                        c.add(new Edge<>(p, new Point(i, yMax), 0));
                    }
                }
            }
        }

        private double addFirstRow(Collection<Edge<Point>> c, Point p) {
            double firstEdgeWeight = 0;
            if (isHorizontal && p.x == 0) {
                for (int i = 0; i <= yMax; i++) {
                    if (i != p.y) {
                        c.add(new Edge<>(p, new Point(0, i), 0));
                    }
                }
                firstEdgeWeight = energies[p.x][p.y];
            }
            if (!isHorizontal && p.y == 0) {
                for (int i = 0; i <= xMax; i++) {
                    if (i != p.x) {
                        c.add(new Edge<>(p, new Point(i, 0), 0));
                    }
                }
                firstEdgeWeight = energies[p.x][p.y];
            }
            return firstEdgeWeight;
        }

    }

}
