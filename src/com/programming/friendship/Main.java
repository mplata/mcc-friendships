package com.programming.friendship;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		Graph<Person> graph = new Graph<Person>();
		for (Person person : persons) {
			graph.addNode(person);
		}
		//System.out.println(graph);
		FriendshipParser parser = new FriendshipParser("./friendships.txt");
		
		LineResult result;
		Person first;
		Person second;
		
		while (parser.hasNext()){
			result = parser.next();
			LineType type = result.getType();
			if(type == LineType.SET_FRIENDSHIP) {
				
				first = result.getFirst();
				second = result.getSecond();
				graph.addBiEdge(first, second);
				
			}
			else if(type == LineType.DIRECT_FRIENDSHIP) {
				
				first = result.getFirst();
				second = result.getSecond();
				boolean areDirect = graph.areAdjacents(first, second);
				String directRes = areDirect?"Verdadero":"Falso";
				System.out.println(directRes);
				
			}else if(type == LineType.FRIENDS_LEVEL) {
				
				first = result.getFirst();
				int level = result.getLevel();
				List<Person> res = graph.getNodesByLevel(first, level);
				List<String> asString = res.stream().map(f-> f.toString()).collect(Collectors.toList());
				String joined = String.join(",",asString);
				System.out.print("Amigos nivel "+level+" de "+first+": ");
				System.out.println(joined);
				
			}else if(type == LineType.REMOVE_FRIENDSHIP) {
				
				first = result.getFirst();
				second = result.getSecond();
				//System.out.println("Eliminar amistad: "+first+","+second);
				graph.removeBiEdge(first, second);
				
			}else if(type == LineType.INVALID_LINE) {
				
				trashCan.addTrash(result.getLine());
				
			}
		}
		trashCan.trowTrash();
		//System.out.println(graph);
	}

}
