package com.programming.friendship;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
* Clase Grafo, implementa la busqueda de caminos
* y utiliza una Hashtable como estructura de datos
* para almacenar el grafo.
*
*/
public final class Graph<T>{

	//Se utiliza un TreeMap para evitar repetir keys y para
	//que al insertar elementos con el mismo nombre pero con mayusculas o 
	//minusculas
    private HashMap<T, Set<T>> graph = new HashMap<T, Set<T>>();

    //Regresar falso si el nodo ya esta en el grafo
    public boolean addNode(T node) {
    	if (graph.containsKey(node)) {
    		return false;
    	}
    	graph.put(node, new HashSet<T>());
        return true;
    }

    //Obtener el Set del key correspondiente y agregarle la arista
    public void addEdge(T start, T dest) {
        //System.out.println(this.graph+" - "+start);
        graph.get(start).add(dest);
    }
    
    public void addBiEdge(T start, T dest) {
    	if(!start.equals(dest)) {
    		graph.get(start).add(dest);
        	graph.get(dest).add(start);
    	}else {
    		//System.out.println("Igualess "+start+","+dest);
    	}
    }
    
    public boolean areAdjacents(T first, T second) {
    	
    	Set<T> friends = graph.get(first);
    	return friends.contains(second);
    }

    public boolean removeEdge(T start, T dest) {
        if (graph.containsKey(start) && graph.containsKey(dest)) {
            return graph.get(start).remove(dest);
        }else{
        	return false;
        }
    }
    
    public boolean removeBiEdge(T first, T second) {
    	if (graph.containsKey(first) && graph.containsKey(second)) {
            return graph.get(first).remove(second) && graph.get(second).remove(first);
        }else{
        	return false;
        }
    }

    public boolean edgeExists(T start, T end) {
        if (!graph.containsKey(start) || !graph.containsKey(end))
            return false;

        return graph.get(start).contains(end);
    }
    
    //Obtener las aristas del Nodo
    public Set<T> edgesFrom(T node) {
        Set<T> arcs = graph.get(node);
        if (arcs == null)
            return null;

        return Collections.unmodifiableSet(arcs);
    }
        
    public ArrayList<T> getNodesByLevel(T root, int nivel) {
		
    	//Una lista de los nodos del nivel actual
    	ArrayList<T> currentLevelNodes = new ArrayList<T>();
    	//Una lista de los nodos que se recorreran el siguiente nivel
		ArrayList<T> nextLevelNodes = new ArrayList<T>();
		//Una lista de todos los nodos que han sido visitados
		ArrayList<T> visited = new ArrayList<T>();
		
		currentLevelNodes.add(root);
		//Se hace una iteración por nivel, si el nivel es n, se haran n iteraciones
		for (int i = 0; i < nivel; i++) {
			//Se retorna vacio si no hay nodos
			if (currentLevelNodes.isEmpty())
				return currentLevelNodes;
			else {
				for (T currentNode : currentLevelNodes) {
					//Se obtienen los vecinos inmediatos
					Set<T> edges = this.edgesFrom(currentNode);
					//y para cada uno, se checa que
					//1.- Que no este ya en la lista de visitados
					//2.- Que no este contenido ya en la lista de nodos para el siguiente nivel
					//3.- Que este contenido en los nodos del nivel actual
					for (T nextLevelNode : edges) {
						if (!visited.contains(nextLevelNode) 
							&& !nextLevelNodes.contains(nextLevelNode) 
							&& !currentLevelNodes.contains(nextLevelNode)) {
							//Si no esta en uno de esas 3 listas, el nodo se visitara en el siguiente nivel
							nextLevelNodes.add(nextLevelNode);
						}
					}
				}
				//Se agregan todos los nodos de este nivel a la lista de visitados
				visited.addAll(currentLevelNodes);
				currentLevelNodes.clear();
				//Y ahora, para la siguiente iteración, los nodos siguientes son los nodos actuales
				currentLevelNodes.addAll(nextLevelNodes);
				nextLevelNodes.clear();
			}
		}

		return currentLevelNodes;
	}
    
    /**
     * Metodo recursivo, se manda el nodo visitado, el nodo buscado,
     * la lista de nodos que se han visitado y el stack
     */
    private boolean findPath(T node,T wanted, List<T> visited, Stack<T> path) 
    { 
    	boolean found= false;
        visited.add(node);
        //Si el nodo visitado es el nodo buscado, se retorna true y regresa
        if(node.equals(wanted)) {
        	path.push(node);
        	return true;
        }
        Set<T> edges = edgesFrom(node);
        //Si el nodo visitado no es el buscado y no hay mas camino,
        //se retorna falso y regresa
        //Si sí hay camino, se manda llamar findPath, en los nodos adyacentes
        if(edges == null || edges.size() == 0 ) {
        	return false;
        }else {
        	Iterator<T> edgesIt = edges.iterator();
            while (edgesIt.hasNext()) 
            { 
                T n = edgesIt.next(); 
                if (!visited.contains(n)) {
                	found = findPath(n, wanted, visited, path);
                	//Si el retorno es verdadero, este nodo forma parte del
                	//camino hacia el nodo buscado, por lo que regresa
                	//true y se agrega a la pila de camino encontrado.
                	if(found) {
                		path.push(node);
                		return true;
                	}
                }
            }
        }
        return found;
    } 
    
    /**
     * Buscar si hay un camino del nodo from al nodo to
     */
    public void findPath(T from, T to){
    	
    	
    	//La lista de nodos visitados y el stack con el camino 
    	//correcto se modifican por referencia
    	List<T> visited = new ArrayList<T>();
    	Stack<T> path = new Stack<T>();
    	boolean isPath = this.findPath(from, to, visited, path);
    	//Si se encuentra un camino, se procesa el path para imprimir la salida
    	//Si no, se imprime la salida solicitada cuando no hay camino
    	if(isPath) {
    		System.out.print("+");
    		while(!path.isEmpty()) {
        		
        		System.out.print(path.pop());
        		if(!path.isEmpty()) {
        			System.out.print(" => ");
        		}
        	}
    		System.out.println("");
    	}else {
    		System.out.println("-"+from +" => "+to);
    	}
    }
    
    @Override
    public String toString() {
    	
    	StringBuilder builder = new StringBuilder();
    	for (Map.Entry<T, Set<T>> entry : this.graph.entrySet())  {
    		T node = entry.getKey();
    		Set<T> destinies = entry.getValue();
    		builder.append(node+" amigo de:"+destinies);
    		builder.append("\r");
    	}
    	return builder.toString();
    }

}