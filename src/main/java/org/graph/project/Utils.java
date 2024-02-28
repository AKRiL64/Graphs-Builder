package org.graph.project;

import java.awt.*;

import static java.lang.Math.*;

public class Utils {

    public static boolean isDistanceBiggerThan(Point firstCords, Point secondCords, int comparableNumber){
        int distance;
        distance = (int) round(sqrt(
                (firstCords.x-secondCords.x)*(firstCords.x-secondCords.x) +
                        (firstCords.y-secondCords.y)*(firstCords.y-secondCords.y)
        ));
        return distance > comparableNumber;
    }
}
