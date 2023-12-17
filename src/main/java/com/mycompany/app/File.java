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
		// remove the trailing '/'
		return getPath().replaceAll("/+$", "");
	}
	
	public TextContent getContent() {
		return content;
	}

	@Override
	public Node clone(Node parent) {
		File nf = new File(name, parent);
		nf.getContent().append(content.getText());
		return nf;
	}
}
