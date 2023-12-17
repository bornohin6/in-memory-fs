package com.mycompany.app;

public abstract class Node {

	private String name;
	private Node parent;
	

	public Node(String name, Node parent) {
		this.name = name;
		this.parent = parent;
	}

	public abstract boolean isDirectory();
	public abstract String display();
	
	public String getName() {
		return name;
	}
	
	public void rename(String name) {
		this.name = name;
	}	
	
	public Node getParent() {
		return parent;
	}
	
    public String getPath() {
        if (parent == null) {
            return "/";
        } else {
            return parent.getPath() + "/" + getName();
        }
    }
}
