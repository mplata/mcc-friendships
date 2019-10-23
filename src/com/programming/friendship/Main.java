package com.programming.friendship;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Listado de personas y el 
		//Bote de basta
		List<Person> persons = null; 
		TrashCan trashCan = null;
		try {
			PersonCatalog personCatalog = PersonCatalog.getInstance();
			trashCan = TrashCan.getInstance();
			persons = personCatalog.getCatalog();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Se agregan todas las personas del catalogo al grafo,
		//sin conexiones
		Graph<Person> graph = new Graph<Person>();
		for (Person person : persons) {
			graph.addNode(person);
		}
		//Se crea el parser
		FriendshipParser parser = new FriendshipParser();
		
		LineResult result;
		Person first;
		Person second;
		//Se procesa cada linea, y se hace
		//algo dependiendo del tipo de linea del
		//Comando
		while (parser.hasNext()){
			result = parser.next();
			LineType type = result.getType();
			if(type == LineType.SET_FRIENDSHIP) {
				
				first = result.getFirst();
				second = result.getSecond();
				graph.addBiEdge(first, second);
				System.out.println("!"+first+" y "+second+" ahora son amigos!");
				System.out.println("");
				
			}
			else if(type == LineType.DIRECT_FRIENDSHIP) {
				
				first = result.getFirst();
				second = result.getSecond();
				boolean areDirect = graph.areAdjacents(first, second);
				String directRes = areDirect?"Verdadero":"Falso";
				System.out.println("¿Es "+first+" amigo de "+second+"? "+directRes);
				System.out.println("");
				
			}else if(type == LineType.FRIENDS_LEVEL) {
				
				first = result.getFirst();
				int level = result.getLevel();
				List<Person> res = graph.getNodesByLevel(first, level);
				List<String> asString = res.stream().map(f-> f.toString()).collect(Collectors.toList());
				String joined = String.join(",",asString);
				System.out.print("Amigos nivel "+level+" de "+first+": ");
				System.out.println(joined);
				System.out.println("");
				
			}else if(type == LineType.REMOVE_FRIENDSHIP) {
				
				first = result.getFirst();
				second = result.getSecond();
				System.out.println("Eliminar amistad: "+first+","+second);
				graph.removeBiEdge(first, second);
				System.out.println("");
				
			}else if(type == LineType.INVALID_LINE) {
				
				trashCan.addTrash(result.getLine());
				
			}
		}
		
		//Al final, mandamos la basura a disco
		trashCan.trowTrash();
	}

}
