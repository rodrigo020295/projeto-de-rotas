package br.radixeng.controller;

import br.radixeng.model.Graph;
import br.radixeng.model.Route;
import br.radixeng.model.Routes;
import br.radixeng.service.GraphService;
import br.radixreng.model.RouteDistance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GraphController {
    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @PostMapping(path = "/graph")
    public ResponseEntity createGraph(@RequestBody Graph graph) {
        if (graph.isSameSourceAndTarget(graph.getData())) {
            return new ResponseEntity("Existem rota(s) com saída e destino iguais", HttpStatus.BAD_REQUEST);
        }

        for (Route route : graph.getData()) {
            route.setGraph(graph);
        }

        Graph graphResponse = graphService.createGraph(graph);
        return new ResponseEntity(graphResponse, HttpStatus.CREATED);
    }

    @GetMapping(path = "/graph/{graphId}")
    public ResponseEntity findById(@PathVariable Integer graphId) {
        return graphService.findById(graphId)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(new ResponseEntity("Grafo não localizado", HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "/routes/from/{town1}/to/{town2}")
    public ResponseEntity findAvariableRoutes(@RequestBody Graph graph,
                                              @PathVariable String town1,
                                              @PathVariable String town2,
                                              @RequestParam Integer maxStop) {
        Route.createRouteList(graph.getData());
        List<Routes> routesList = Route.calculateTrips(town1, town2, t -> t <= maxStop, maxStop);

        if (routesList.isEmpty()) {
            return new ResponseEntity("Grafo não localizado", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(routesList, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/distance/from/{town1}/to/{town2}")
    public ResponseEntity findMinimumRouteDistance(@RequestBody Graph graph,
                                                   @PathVariable String town1,
                                                   @PathVariable String town2) {
        if (town1.equalsIgnoreCase(town2)) {
            return new ResponseEntity(0, HttpStatus.OK);
        }

        Route.createRouteList(graph.getData());
        RouteDistance routeDistance = Route.findMinimumRouteDistance(town1, town2);

        if (routeDistance == null) {
            return new ResponseEntity(-1, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(routeDistance, HttpStatus.OK);
    }
}