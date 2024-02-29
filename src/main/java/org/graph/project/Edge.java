package org.graph.project;

public class Edge {
    private Peak peakOne;
    private Peak peakTwo;

    public Edge(Peak peakOne, Peak peakTwo) {
        this.peakOne = peakOne;
        this.peakTwo = peakTwo;
    }

    public Peak getPeakOne() {return peakOne;}
    public void setPeakOne(Peak peakOne) {this.peakOne = peakOne;}
    public Peak getPeakTwo() {return peakTwo;}
    public void setPeakTwo(Peak peakTwo) {this.peakTwo = peakTwo;}
}
