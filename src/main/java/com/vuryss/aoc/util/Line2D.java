package com.vuryss.aoc.util;

public class Line2D {
    public PointLong point;
    public PointLong delta;

    public Line2D(PointLong point, long deltaX, long deltaY) {
        this.point = point;
        this.delta = new PointLong(deltaX, deltaY);
    }

    public Line2D(PointLong point1, PointLong point2) {
        this.point = point1;
        this.delta = point2.sub(point1);
    }

    /**
     * Line formula is: y = slope * x + b
     * -
     * Slope can be calculated as: deltaY / deltaX
     * And b can be calculated as: y - slope * x
     * -
     * If we want the lines to have intersection, then the (x, y) coordinates should be the same at some point.
     * For the two lines: y1 = slope1 * x1 + b1 and y2 = slope2 * x2 + b2 to have the same y coordinates, we
     * can set them equal to each other: slope1 * x1 + b1 = slope2 * x2 + b2
     * And if (x, y) have to be the same for the 2 lines we are left with: slope1 * x + b1 = slope2 * x + b2
     * Extracting x from the equation we get: x = (b2 - b1) / (slope1 - slope2)
     * -
     * And y can be calculated as: y = slope1 * x + b1
     */
    public PointLong intersects(Line2D line) {
        double line1Slope = (double) delta.y / delta.x;
        double line2Slope = (double) line.delta.y / line.delta.x;

        // If slopes are equal, lines are parallel
        if (line1Slope == line2Slope) {
            return null;
        }

        double line1b = point.y - line1Slope * point.x;
        double line2b = line.point.y - line2Slope * line.point.x;

        var intersectionX = Math.round((line2b - line1b) / (line1Slope - line2Slope));
        var intersectionY = Math.round(line1Slope * intersectionX + line1b);

        return new PointLong(intersectionX, intersectionY);
    }
}
