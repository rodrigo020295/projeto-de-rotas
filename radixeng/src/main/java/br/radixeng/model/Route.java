package br.radixeng.model;

import br.radixreng.model.RouteDistance;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Entity
public class Route {
    private static final String[] VERTEX_LIST = {"A", "B", "C", "D", "E"};

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer routeId;

    @JsonIgnore
    private Integer sourceId;

    private String source;

    @JsonIgnore
    private Integer targetId;

    private String target;

    private Integer distance;

    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private Graph graph;

    @JsonIgnore
    @Transient
    private static List<List<Route>> routeList = new ArrayList<>();

    @JsonIgnore
    @Transient
    private static List<Routes> routesList = new ArrayList<Routes>();

    @JsonIgnore
    @Transient
    private static int to;

    @JsonIgnore
    @Transient
    private static int stops;

    @JsonIgnore
    @Transient
    private static int tripsCount;

    public static void createRouteList(List<Route> data) {
        routeList.clear();

        for (int i = 1; i <= VERTEX_LIST.length; i++) {
            routeList.add(new ArrayList<Route>());
        }

        for (Route route : data) {
            route.setSourceId(getIndex(route.getSource()));
            route.setTargetId(getIndex(route.getTarget()));

            routeList.get(route.getSourceId()).add(route);
        }
    }

    public static List<Routes> calculateTrips(String from, String to, Predicate<Integer> p, int stops) {
        int startIndex = getIndex(from);
        Route.to = getIndex(to);
        Route.stops = stops;
        Route.tripsCount = 0;

        return calculateTripsCount(startIndex, from, p);
    }

    private static List<Routes> calculateTripsCount(int from, String path, Predicate<Integer> p) {
        List<Route> routes = routeList.get(from);
        for (Route route : routes) {
            String next = path + route.getTarget();
            int stopCount = next.length() - 1;

            if (to == route.getTargetId() && p.test(stopCount)) {
                Routes newRoutes = new Routes();
                newRoutes.setRoute(next);
                newRoutes.setStops(next.length() - 1);

                Route.routesList.add(newRoutes);

                Route.tripsCount++;
            }

            if (stopCount <= Route.stops)
                calculateTripsCount(route.getTargetId(), next, p);
        }

        return Route.routesList;
    }

    public static RouteDistance findMinimumRouteDistance(String from, String to) {
        RouteDistance routeDistance = new RouteDistance();
        routeDistance.setAllPath(new ArrayList<>());
        routeDistance.setPath(new ArrayList<>());

        Route.to = getIndex(to);
        int startIndex = getIndex(from);
        calculateShortestPath(startIndex, String.valueOf(startIndex), routeDistance);

        int shortestDistance = Integer.MAX_VALUE, currentDistance;
        for (String s : routeDistance.getAllPath()) {
            currentDistance = calculateDistance(s);

            if (shortestDistance > currentDistance) {
                routeDistance.getPath().add(s);
                routeDistance.setDistance(currentDistance);

                shortestDistance = currentDistance;
            }
        }

        if (shortestDistance == Integer.MAX_VALUE) {
            return null;
        }

        return routeDistance;
    }

    private static void calculateShortestPath(int from, String path, RouteDistance routeDistance) {
        List<Route> routes = routeList.get(from);

        for (Route route : routes) {
            if (path.length() > 1 && path.substring(1).contains(String.valueOf(route.getTargetId()))) {
                continue;
            }

            String next = path + route.getTargetId();

            if (Route.to == route.getTargetId()) {
                routeDistance.getAllPath().add(getPathName(next));
            }

            calculateShortestPath(route.getTargetId(), next, routeDistance);
        }
    }

    private static int calculateDistance(String route) {
        if (route == null) {
            throw new IllegalArgumentException("Route is wrong");
        }

        int distance = 0;
        String[] vertex = route.trim().split("");
        int from, to;

        for (int i = 0; i < vertex.length - 1; ) {
            boolean hasPath = false;
            from = getIndex(vertex[i++]);
            to = getIndex(vertex[i]);
            List<Route> routes = routeList.get(from);

            for (Route route1 : routes) {
                if (route1.getTargetId() == to) {
                    distance += route1.getDistance();
                    
          
                    hasPath = true;
                    break;
                }
            }

            if (!hasPath) {
                return -1;
            }
        }

        return distance;
    }

    private static int getIndex(String vertex) {
        return Arrays.binarySearch(VERTEX_LIST, vertex);
    }

    private static String getPathName(String path) {
        String arr[] = path.trim().split("");
        String name = "";
        for (String v : arr)
            name += getVertexName(Integer.parseInt(v));

        return name;
    }

    private static String getVertexName(int index) {
        if (index < 0 || index >= VERTEX_LIST.length)
            throw new IllegalArgumentException("Wrong index");

        return VERTEX_LIST[index];
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public List<List<Route>> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<List<Route>> routeList) {
        Route.routeList = routeList;
    }

    public List<Routes> getRoutesList() {
        return routesList;
    }

    public void setRoutesList(List<Routes> routesList) {
        Route.routesList = routesList;
    }
    
    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        Route.to = to;
    }

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        Route.stops = stops;
    }

    public int getTripsCount() {
        return tripsCount;
    }

    public void setTripsCount(int tripsCount) {
        Route.tripsCount = tripsCount;
    }

    @Override
    public String toString() {
        return source + target + distance;
    }
}