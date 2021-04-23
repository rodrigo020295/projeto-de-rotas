package br.radixeng.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
public class Graph {
	
    @Id
    @GeneratedValue
    private Integer id;

    @OneToMany(cascade = ALL, mappedBy = "graph")
    private List<Route> data;

    public boolean isSameSourceAndTarget(List<Route> data) {
        for (Route route : data) {
            if (route.getSource().equalsIgnoreCase(route.getTarget())) {
                return true;
            }
        }
        return false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Route> getData() {
        return data;
    }

    public void setData(List<Route> data) {
        this.data = data;
    }
}