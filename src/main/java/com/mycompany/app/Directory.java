package com.mycompany.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Directory extends Node {

	private Map<String, Node> children;
    private static final Logger logger = LogManager.getLogger(Directory.class);
	
	public Directory(String name, Node parent) {
		super(name, parent);
		children  = new HashMap<>();
	}

	@Override
	public boolean isDirectory() {
		return true;
	}
	
	@Override
	public String printPath() {
		return getPath();
	}
	
	@Override
	public Node clone(Node parent) {
		Directory dir = new Directory(name, parent);
		if (children.isEmpty()) {
			return dir;
		}
		for(String s : children.keySet()) {
			Node oldChild = children.get(s);
			Node newChild = oldChild.clone(dir);
			dir.addChild(newChild);
		}
		return dir;
	}
	
	/*
	 * Adds a new child to the directory
	 */
	public void addChild(Node c) {
		logger.info("Adding node {}", c.getName());
		
		children.put(c.getName(), c);
		c.setParent(this);
	}
	
	/*
	 * Clones the entire subtree and adds it as a child
	 */
	public void addSubTree(Node n) {
		children.put(n.getName(), n.clone(this));
	}
	
	public boolean contains(String name) {
		return children.containsKey(name);
	}

	public Node getChild(String name) throws Exception {
		if (!children.containsKey(name)) {
			throw new Exception(getPath() + " doesn't have any directory named " + name);
		}
		return children.get(name);
	}

	public Directory mkdir(String name) throws Exception {
		Node n = children.get(name);

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
		children.put(name, d);
		logger.info("Directory created {}", name);
		return d;
	}
	
	public void touch(String name) {
		Node n = children.get(name);
		if(n != null) {
			logger.info("There is a already a file/directory named {}", name);
			return;
		}
		n = new File(name, this);
		children.put(name, n);
	}
	
	public void removeChild(String name) {
		Node n = children.get(name);
		children.remove(n.getName());
		n.setParent(null);
	}
	
	public List<String> getChildren() {
		List<String> res = new ArrayList<>(children.keySet());
		Collections.sort(res);
		return res;
	}
	
	private void helper(Node n, String name, String buffer, Set<String> res) {		
		if (!n.isDirectory()) {
			return;
		}
		
		Directory dir = (Directory)n;
		for (Map.Entry<String, Node> child : dir.children.entrySet()) {
			if (child.getKey().equals(name)) {
				res.add(buffer + "/" + name);
			}
			helper(child.getValue(), name, buffer + "/" + child.getKey(), res);
		}
	}
	
	public Set<String> findChildren(String name) {
		Set<String> res = new HashSet<>(); 
		helper(this, name, ".", res);
		return res;
	}
}
