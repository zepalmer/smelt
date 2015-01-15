package com.bahj.smelt.util.partialorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 * An object for arbitrarily (but consistently) resolving a set of partial ordering constraints into a consistent
 * ordering.
 * 
 * @author Zachary Palmer
 * @param <T>
 *            The type of element to be ordered.
 */
public class PartialOrderUtils {
    /**
     * Orders the provided elements based on some partial ordering constraints. Ties are broken by the provided
     * comparator (which may be arbitrary but must be consistent and specified).
     * 
     * @param elements
     *            The elements to order. These elements must not be equal or they will be considered identical in the
     *            order.
     * @param constraints
     *            The constraints on the ordering.
     * @param tieBreaker
     *            The comparator used to break ties.
     * @return The provided elements in an order respecting the provided constraints.
     * @throws InconsistentPartialOrderException
     *             If the partial order is inconsistent based on the provided constraints.
     */
    public static <T> List<T> orderByConstraints(Collection<T> elements,
            Collection<PartialOrderConstraint<T>> constraints, Comparator<T> tieBreaker)
            throws InconsistentPartialOrderException {
        // Create a directed graph describing the partial ordering constraints.
        DirectedGraph<T, DefaultEdge> partialOrderGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (T element : elements) {
            partialOrderGraph.addVertex(element);
        }
        for (PartialOrderConstraint<T> constraint : constraints) {
            if (elements.contains(constraint.getLess()) && elements.contains(constraint.getGreater())) {
                partialOrderGraph.addEdge(constraint.getLess(), constraint.getGreater());
            }
        }

        // Determine if there are any strongly connected components. If so, pick one and scream about it.
        StrongConnectivityInspector<T, DefaultEdge> strongConnectivityInspector = new StrongConnectivityInspector<>(
                partialOrderGraph);
        List<Set<T>> components = strongConnectivityInspector.stronglyConnectedSets();
        if (components.size()>0) {
            throw new InconsistentPartialOrderException(components.get(0));
        }
        
        // Otherwise, there is no cycle in the graph.  That means we can just iterate a tie-breaking topological sort
        // to get our ordering.
        TopologicalOrderIterator<T, DefaultEdge> iterator = new TopologicalOrderIterator<T, DefaultEdge>(
                partialOrderGraph, new PriorityQueue<T>(elements.size(), tieBreaker));
        List<T> result = new ArrayList<>(elements.size());
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }
}
