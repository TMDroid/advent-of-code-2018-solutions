package com.adventofcode;

import java.awt.*;
import java.util.ArrayList;

public class ManhattanPoint {
    private static char GLOBAL_LETTER = '\0';
    public Point p;
    public char letter;
    public int times;

    public ManhattanPoint(Point p) {
        this.p = p;
        this.letter = GLOBAL_LETTER++;
        this.times = 0;
    }

    public static ManhattanPoint findNearest(Point p, ArrayList<ManhattanPoint> points) {
        ManhattanPoint manhattanPointNearestToP = points.get(0);

        for(ManhattanPoint point : points) {
            Integer xDifference = Math.abs(point.p.x - p.x);
            Integer yDifference = Math.abs(point.p.y - p.y);

            Integer pureDifference = xDifference + yDifference;

            Integer xManhattanDifference = Math.abs(manhattanPointNearestToP.p.x - p.x);
            Integer yManhattanDifference = Math.abs(manhattanPointNearestToP.p.y - p.y);

            Integer manhattanDifference = xManhattanDifference + yManhattanDifference;

            if(pureDifference == manhattanDifference) {
                return null;
            }

            if(pureDifference < manhattanDifference) {
                manhattanPointNearestToP = point;
            }
        }

        return manhattanPointNearestToP;
    }

    public Integer distanceTo(Point p) {
        return Math.abs(this.p.x - p.x) + Math.abs(this.p.y - p.y);
    }

    public Integer distanceTo(ManhattanPoint p) {
        return this.distanceTo(p.p);
    }
}
