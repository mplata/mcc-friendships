package com.programming.friendship;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrashCan {
	
	private List<String> trash;
	
	private static TrashCan instance = null;
	
	private final String ROUTE = "./trash.txt";
	
	private TrashCan() {
		trash = new ArrayList<String>();
	}
	
	public static TrashCan getInstance() {
		if(instance == null){
			instance = new TrashCan();
		}
		return instance;
	}
	
	public void addTrash(String line) {
		this.trash.add(line);
	}
	
	public void trowTrash() {
		FileWriter writer;
		try {
			writer = new FileWriter(ROUTE);
			for(String str: this.trash) {
			  writer.write(str + System.lineSeparator());
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			this.trash.clear();
		}
	}
}
