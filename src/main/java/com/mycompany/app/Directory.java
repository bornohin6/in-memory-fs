package com.mycompany.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Directory extends Node {

	private Map<String, Node> content;
    private static final Logger logger = LogManager.getLogger(Directory.class);
	
	public Directory(String name, Node parent) {
		super(name, parent);
		content  = new HashMap<>();
	}

	@Override
	public boolean isDirectory() {
		return true;
	}
	
	@Override
	public String display() {
		return getPath();
	}
	
	@Override
	public Node clone(Node parent) {
		Directory np = new Directory(name, parent);
		if (content.isEmpty()) {
			return np;
		}
		for(String s : content.keySet()) {
			Node oldC = content.get(s);
			Node newC = oldC.clone(np);
			np.add(newC);
		}
		return np;
	}
	
	public void add(Node n) {
		logger.info("Adding node {}", n.getName());
		
		content.put(n.getName(), n);
		n.setParent(this);
	}
	
	public boolean contains(String name) {
		return content.containsKey(name);
	}

	public Node getChild(String name) throws Exception {
		if (!content.containsKey(name)) {
			throw new Exception(getPath() + " doesn't have any directory named " + name);
		}
		return content.get(name);
	}

	public Directory mkdir(String name) throws Exception {
		Node n = content.get(name);

		if(n != null) {
			if (n.isDirectory()) {
				logger.info("Directory exists {}", name);
				return (Directory)n;
			}
			else {
				throw new Exception("A file with name \"" + name + "\" already exists under " + getPath());		
			}
		}
		
		Directory d = new Directory(name, this);
		content.put(name, d);
		logger.info("Directory created {}", name);
		return d;
	}
	
	public void touch(String name) {
		Node n = content.get(name);
		if(n != null) {
			logger.info("There is a already a file/directory named {}", name);
			return;
		}
		n = new File(name, this);
		content.put(name, n);
	}
	
	public void remove(String name) {
		Node n = content.get(name);
		content.remove(n.getName());
	}
	
	public void copy(Node n) {
		content.put(n.getName(), n.clone(this));
	}
	
	public List<String> contents() {
		List<String> res = new ArrayList<>(content.keySet());
		Collections.sort(res);
		return res;
	}
}
