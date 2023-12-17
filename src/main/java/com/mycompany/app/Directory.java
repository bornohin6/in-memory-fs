package com.mycompany.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jdk.internal.org.jline.utils.Log;

public class Directory extends Node {

	private Map<String, Node> content;
    private static final Logger logger = LogManager.getLogger();
	
	public Directory(String name, Node parent) {
		super(name, parent);
		content  = new HashMap<>();
	}

	@Override
	public boolean isDirectory() {
		return true;
	}
	
	public boolean contains(String name) {
		return content.containsKey(name);
	}
	
	public Directory mkdir(String name) throws Exception {
		// TODO handle directory name
		Node n = content.get(name);

		if(n != null && !n.isDirectory()) {
			if (n.isDirectory()) {
				logger.debug("Directory exists " + name);
				return (Directory)n;
			}
			else {
				throw new Exception("File exists");		
			}

		}
		
		n = new Directory(name, this);
		content.put(name, n);
		logger.debug("Directory created " + name);
		return (Directory)n;
	}
	
	public void touch(String name) {
		// TODO handle directory name		
		Node n = new File(name, this);
		if(content.containsKey(name)) {
			System.out.println("There is a already a file named " + name);
			return;
		}		
		content.put(name, n);
	}
	
	public Directory chdir(String name) {
		// TODO handle directory name
		return null;
	}

	@Override
	public String display() {
		// TODO: this is the pwd
		return getPath();
	}
}
