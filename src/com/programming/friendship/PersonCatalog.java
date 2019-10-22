package com.programming.friendship;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonCatalog {
	
	private static PersonCatalog instance = null;
	
	private final String ROUTE = "./catalogo.txt";
	private final String DATE_FORMAT = "yyyy/mm/dd";
	private DateFormat dateFormat;
	private List<Person> persons;
	
	private Pattern PATTERN;
	 
	private PersonCatalog()  throws ParseException{
		
		this.dateFormat = new SimpleDateFormat(DATE_FORMAT);
		PATTERN = Pattern.compile("([a-zA-Z\\s*]{1,20})\\s*,\\s*([a-zA-Z\\s*]{1,20})\\s*,\\s*([MmFf])\\s*,\\s*(\\d{2}[/]\\d{2}[/]\\d{4})");
		this.persons = new ArrayList<Person>();
		Person person;
		Scanner sc = null;
		try {
			sc = new Scanner(new File(this.ROUTE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			person = this.parseFromLine(line);
			this.persons.add(person);
		}
		sc.close();
	}
	
	public static PersonCatalog getInstance() throws ParseException {
		if(instance == null) {
			instance = new PersonCatalog();
		}
		return instance;
	}
	
	public List<Person> getCatalog(){
		
		return this.persons;
	}
	
	public Person get(int i) {
		return this.persons.get(i-1);
	}
	
	public Person get(String line) {
		Person person = null;
		boolean isIndex = false;
		try {
			int position = Integer.valueOf(line);
			if(position >= this.persons.size() || position < 0) {
				return null;
			}
			person = this.get(position);
		} catch(NumberFormatException nfe) {
			isIndex = true;
		}
		if(isIndex) {
			try {
				person = this.parseFromLine(line);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return person;
	}
	private Person parseFromLine(String line) throws ParseException {
		
		Matcher matcher = PATTERN.matcher(line);
		matcher.find();
		String name = matcher.group(1);
		String lastName = matcher.group(2);
		char sex = matcher.group(3).charAt(0);
		String birthDate = matcher.group(4);
		
		Person person = new Person();
		
		person.setName(name);
		person.setLastName(lastName);
		person.setSex(sex);
		
		Date date = this.dateFormat.parse(birthDate);
		person.setBirth(date);
		
		return person;
	}
}
