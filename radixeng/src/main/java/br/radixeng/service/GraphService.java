package br.radixeng.service;

import br.radixeng.model.Graph;
import br.radixeng.repository.GraphRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GraphService {
    private final GraphRepository graphRepository;

    public GraphService(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    public Graph createGraph(Graph graph) {
        return graphRepository.save(graph);
    }

    public Optional<Graph> findById(Integer graphId) {
        return graphRepository.findById(graphId);
    }
}