package br.radixreng.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class RouteDistance {
    private int distance;

    @JsonIgnore
    private List<String> allPath;

    private List<String> path;

    
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<String> getAllPath() {
        return allPath;
    }

    public void setAllPath(List<String> allPath) {
        this.allPath = allPath;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }
}
