package org.graph.project;

import org.graph.project.dto.Point2D;

import java.awt.*;

import static java.lang.Math.*;

public class Utils {
    public final static int HITBOX_DELTA = 10;
    public static void sortPoint2D (Point2D p2D){
        int temp;
        if (p2D.getX2()<p2D.getX1()){
            temp = p2D.getX1();
            p2D.setX1(p2D.getX2());
            p2D.setX2(temp);
        }
        if (p2D.getY2()<p2D.getY1()){
            temp = p2D.getY1();
            p2D.setY1(p2D.getY2());
            p2D.setY2(temp);
            }
    }

    public static int calculateDistanceFromPointToLine(Point2D p2D, Point p) {
        double a;
        double b;
        double c;
        a = (p2D.getY2()-p2D.getY1());
        b = (p2D.getX1()-p2D.getX2());
        c = (p2D.getX2()*p2D.getY1() - p2D.getX1()*p2D.getY2());

        return (int) round(abs((a * p.x + b * p.y + c) / sqrt(a * a + b * b)));
    }

    public static boolean isDistanceBetweenPointsBiggerThan(Point firstCords, Point secondCords, int comparableNumber){
        int distance;
        distance = (int) round(sqrt(
                (firstCords.x-secondCords.x)*(firstCords.x-secondCords.x) +
                        (firstCords.y-secondCords.y)*(firstCords.y-secondCords.y)
        ));
        return distance > comparableNumber;
    }

    public static boolean isDistanceToLineSectionBiggerThan(Point firstLineCords, Point secondLineCords, Point pointCords, int comparableNumber){
        int distance;
        Point2D lineCords = new Point2D(firstLineCords.x, firstLineCords.y,
                secondLineCords.x, secondLineCords.y);
        sortPoint2D(lineCords);
        if (pointCords.x>lineCords.getX1()-HITBOX_DELTA && pointCords.x<lineCords.getX2()+HITBOX_DELTA &&
                pointCords.y>lineCords.getY1()-HITBOX_DELTA && pointCords.y< lineCords.getY2()+HITBOX_DELTA) {
            distance = calculateDistanceFromPointToLine(new Point2D(firstLineCords.x, firstLineCords.y,
                    secondLineCords.x, secondLineCords.y), pointCords);
            return distance > comparableNumber;
        }
        else {
            return true;
        }

    }
}
