package com.mycompany.app;

import java.util.logging.Logger;

public class File extends Node {
	
    protected static final Logger logger = Logger.getLogger(File.class.getName());
    
    private TextContent content;


	public File(String name, Node parent) {
		super(name, parent);
		content = new TextContent();
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public String display() {
		return getPath().replaceAll("/+$", "");
	}
	
	public TextContent getContent() {
		return content;
	}
}
