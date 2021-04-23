package br.radixeng.controller;

import br.radixeng.model.Graph;
import br.radixeng.model.Route;
import br.radixeng.model.Routes;
import br.radixreng.model.RouteDistance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GraphControllerTest {
    private Graph graph;

    @Autowired
    private GraphController graphController;

    @Test
    public void origemCDestinoCCom3ParadasTest() {
        graph = createGraph();
        ResponseEntity responseEntity = graphController.findAvariableRoutes(graph, "C", "C", 3);

        List<Routes> routesList = (List<Routes>) responseEntity.getBody();
        assertEquals(2, routesList.size());
    }

    @Test
    public void origemADestinoCCom4ParadasTest() {
        graph = createGraph();
        ResponseEntity<?> responseEntity = graphController.findAvariableRoutes(graph, "A", "C", 4);

        List<Routes> routesList = (List<Routes>) responseEntity.getBody();
        assertEquals(8, routesList.size());
    }

    @Test
    public void distanciaMinimaDeAParaCTest() {
        graph = createGraph();
        ResponseEntity responseEntity = graphController.findMinimumRouteDistance(graph, "A", "C");

        RouteDistance routeDistance = (RouteDistance) responseEntity.getBody();
        assertEquals(9, routeDistance.getDistance());
    }

    @Test
    public void distanciaMinimaDeBParaBTest() {
        graph = createGraph();
        ResponseEntity responseEntity = graphController.findMinimumRouteDistance(graph, "B", "B");

        assertEquals(0, responseEntity.getBody());
    }

    private Graph createGraph() {
        Graph graph = new Graph();

        graph.setData(Arrays.asList
        		(createData("A", "B", 5),
                createData("B", "C", 4),
                createData("C", "D", 8),
                createData("D", "C", 8),
                createData("D", "E", 6),
                createData("A", "D", 5),
                createData("C", "E", 2),
                createData("E", "B", 3),
                createData("A", "E", 7)));

        return graph;
    }

    private Route createData(String a, String b, int i) {
        Route route = new Route();
        route.setSource(a);
        route.setTarget(b);
        route.setDistance(i);

        return route;
    }
}