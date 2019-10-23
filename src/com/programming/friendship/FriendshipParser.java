package com.programming.friendship;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FriendshipParser implements Iterator<LineResult>{
	
	private int currentIndex = 0;
	private List<String> lines;

	private String PERSON_PATTERN = "(([a-zA-Z\\s*]{1,20})\\s*,\\s*([a-zA-Z\\s*]{1,20})\\s*,\\s*([MmFf])\\s*,\\s*(\\d{2}[/]\\d{2}[/]\\d{4}))";
	
	private final String SET_FRIENDSHIP_PATTERN = "\\d+\\s*amigo\\s*\\d+|"+PERSON_PATTERN+"\\s*amigo\\s*"+PERSON_PATTERN+"\\s*|"+PERSON_PATTERN+"\\s*amigo\\s*\\d|\\s*\\d+\\s*amigo\\s*"+PERSON_PATTERN;
	private final String REMOVE_FRIENDSHIP_PATTERN = PERSON_PATTERN+"\\s*eliminar\\s*"+PERSON_PATTERN+"\\s*|"+PERSON_PATTERN+"\\s*eliminar\\s*\\d+|\\d+\\s*eliminar\\s*"+PERSON_PATTERN+"|\\d+\\s*eliminar\\s*\\d+";
	private final String DIRECT_FRIENDSHIP_PATTERN = "\\d+\\s*amigos\\s*\\d+|"+PERSON_PATTERN+"\\s*amigos\\s*"+PERSON_PATTERN+"|"+PERSON_PATTERN+"\\s*amigos\\s*\\d|\\s*\\d+\\s*amigos\\s*"+PERSON_PATTERN;
	private final String FRIENDSHIP_LEVEL = "amigos\\s*([a-zA-Z\\s*]{1,20}\\s*,\\s*[a-zA-Z\\s*]{1,20}\\s*,\\s*[MmFf]\\s*,\\s*\\d{2}[\\/]\\d{2}[\\/]\\d{4})\\s*(\\d+)|amigos\\s*(\\d+)\\s*(\\d+)";
	
	public FriendshipParser() {
		
		Scanner sc = null;
		JFileChooser file=new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		file.setFileFilter(filter);
		file.setFileSelectionMode(JFileChooser.FILES_ONLY);
		File selectedFile = null;
		while(selectedFile == null) {
			file.showOpenDialog(null);
			selectedFile = file.getSelectedFile();
		}
		try {
			sc = new Scanner(selectedFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		this.lines = new ArrayList<String>();
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			//line = line.replaceAll("\\s+","");
			this.lines.add(line);
		}
		sc.close();
	}
	
	private void createFromLine(LineResult result, String separator, String line, PersonCatalog catalog) {
		line = line.trim().replaceAll("[ ]{2,}", " ");
		String[] nodes = line.split(separator);
		String left = nodes[0];
		String right = nodes[1];
		left = left.trim();
		right = right.trim();
		Person leftPerson = catalog.get(left);
		Person rigthPerson = catalog.get(right);
		if(leftPerson == null || rigthPerson == null) {
			result.setType(LineType.INVALID_LINE);
			result.setLine(line);
		}
		result.setFirst(leftPerson);
		result.setSecond(rigthPerson);
	}
	
	@Override
	public boolean hasNext() {
		return this.currentIndex < this.lines.size();
	}
	
	@Override
	public LineResult next() {
		
		LineResult result = new LineResult();
		
		PersonCatalog catalog = null;
		try {
			catalog = PersonCatalog.getInstance();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String line = this.lines.get(this.currentIndex);
		if(Pattern.matches(SET_FRIENDSHIP_PATTERN,line)) {
			result.setType(LineType.SET_FRIENDSHIP);
			String separator = "amigo";
			this.createFromLine(result, separator, line,catalog);
		}else if(Pattern.matches(REMOVE_FRIENDSHIP_PATTERN, line)) {
			result.setType(LineType.REMOVE_FRIENDSHIP);
			String separator = "eliminar";
			this.createFromLine(result, separator, line,catalog);
			//System.out.println("remove friends "+line);
		}else if(Pattern.matches(DIRECT_FRIENDSHIP_PATTERN, line)) {
			result.setType(LineType.DIRECT_FRIENDSHIP);
			String separator = "amigos";
			this.createFromLine(result, separator, line,catalog);
			//System.out.println("direct friends "+line);
		}else if(Pattern.matches(FRIENDSHIP_LEVEL, line)) {
			
			line = line.trim().replaceAll("[ ]{2,}", " ");
			result.setType(LineType.FRIENDS_LEVEL);
			Pattern pattern = Pattern.compile(FRIENDSHIP_LEVEL);
			Matcher matcher = pattern.matcher(line);
			
			matcher.find();
			String left = matcher.group(1);
			String right = matcher.group(2);
			if(left == null && right == null) {
				left = matcher.group(3);
				right = matcher.group(4);
			}
			left = left.trim();
			right = right.trim();
			Person leftPerson = catalog.get(left);
			int level = Integer.valueOf(right);
			result.setFirst(leftPerson);
			result.setLevel(level);
		}else {
			result.setType(LineType.INVALID_LINE);
			result.setLine(line);
			System.out.println("Linae invalida "+line);
		}
		this.currentIndex++;
		return result;
	}
	
}
