package com.mycompany.app;

public abstract class Node {

	protected String name;
	protected Node parent;
	

	public Node(String name, Node parent) {
		this.name = name;
		this.parent = parent;
	}

	public abstract boolean isDirectory();
	public abstract String display();
	public abstract Node clone(Node parent);
	
	public String getName() {
		return name;
	}
	
	public void rename(String name) {
		this.name = name;
	}	
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node p) {
		parent = p;
	}	
	
    public String getPath() {
        if (parent == null) {
            return "/";
        } else {
            return parent.getPath() + getName() + "/";
        }
    }
}
