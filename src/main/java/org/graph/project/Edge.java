package org.graph.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class Edge {
    private HashSet<Peak> connectedPeaks;
    private String Type;
    public Edge() {
        connectedPeaks = new HashSet<>();
    }

    public Edge(Peak peak) {
        connectedPeaks = new HashSet<>();
        connectedPeaks.add(peak);
    }

    public HashSet<Peak> getConnectedPeaks() {
        return connectedPeaks;
    }

    public void addPeak(Peak peak) {
        this.connectedPeaks.add(peak);
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
