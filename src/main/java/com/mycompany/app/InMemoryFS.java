package com.mycompany.app;


/**
 * 
 * Inode: name, isDirectory, parent, permission, group?
 * File: write, read, rename, find, {content should be separate object with ref count to support soft/hard link}
 * Directory: write(create a new directory or file), read(pwd), chdir, list, remove, find, 
 * User: , Group: read, write
 * 
● Change the current working directory. The working directory begins at '/'. You may traverse to a child directory or the parent.

● Get the current working directory. Returns the current working directory's path from the root to console. Example: ‘/school/homework’
● Create a new directory. The current working directory is the parent.
● Get the directory contents: Returns the children of the current working directory.
Example: [‘math’, ‘history’, ‘spanish’]
● Remove a directory. The target directory must be among the current working directory’s
children.
● Create a new file: Creates a new empty file in the current working directory.
● Write file contents: Writes the specified contents to a file in the current working
directory. All file contents will fit into memory.
● Get file contents: Returns the content of a file in the current working directory.
● Move a file: Move an existing file in the current working directory to a new location (in
the same directory).
● Find a file/directory: Given a filename, find all the files and directories within the current
working directory that have exactly that name.

 */
public class InMemoryFS {
	/*
	 * Needs to track currentDirectory, root
	 * Implement chdir
	 */
	private Directory root, currentDir;
	
	private boolean isAbsolute(String name) {
		return name.startsWith("/");
    }
	
	private String getAbsolutePath(String name) {
		if (isAbsolute(name)) {
			return name;
		}
		
		String currentPath = currentDir.getPath();
		return currentPath + "/" + name;
	}
	
	private Node findNode(String name) {
		// check if absolute path or relative path
		// if absolute path - start searching
		return null;
	}

	public InMemoryFS() {
		root = new Directory("", null);
		currentDir = root;
	}

	public String getCurrentDir() {
		return currentDir.getPath();
	}

	public boolean createDirectory(String name) {
		// get an absolute path
		// iterate over the names
		String absolutePath = getAbsolutePath(name);
		String[] pathComponents = absolutePath.split("/");
		Directory prev = root;
		try {
			for (String component : pathComponents) {
				prev = prev.mkdir(component);
			}
		} catch(Exception e) {
			System.out.println(e.toString());
		}

		return true;
	}

	public boolean changeDirectory(String name) {
		// TODO need to check for whole path
		currentDir.chdir(name);
		return true;
	}
}
