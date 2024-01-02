package com.mycompany.app;

public abstract class Node {

	protected String name;
	protected Node parent;
	
	/*
	 * The file system is organized as an in-tree or parent pointer tree.
	 * Each node either represents a file or a directory, and has a pointer
	 * to a parent node except the root node.
	 */
	public Node(String name, Node parent) {
		this.name = name;
		this.parent = parent;
	}

	/*
	 * Returns true if node is a directory
	 */
	public abstract boolean isDirectory();
	
	/*
	 * Displays full path name
	 */
	public abstract String printPath();
	
	/*
	 * Clones the entire subtree to the parent node
	 */
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
