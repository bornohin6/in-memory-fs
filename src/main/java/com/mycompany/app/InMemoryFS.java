package com.mycompany.app;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InMemoryFS {
	private static final Logger logger = LogManager.getLogger(InMemoryFS.class);
	private Directory root, currentDir;

	public InMemoryFS() {
		root = new Directory("", null);
		currentDir = root;
	}

	private boolean isAbsolute(String name) {
		return name.startsWith("/");
    }
	
	public String getAbsolutePath(String name) {
		if (isAbsolute(name)) {
			return name;
		}
		
		String currentPath = currentDir.getPath();
		return currentPath + name;
	}

	public String getCurrentDir() {
		return currentDir.getPath();
	}

	public void createDirectory(String name) throws Exception {
		String absolutePath = getAbsolutePath(name);
		logger.info("Absolute path {}", absolutePath);
		String[] pathComponents = absolutePath.split("/");
		Directory prev = root;
		for (int i = 1; i < pathComponents.length; i++) {
			prev = prev.mkdir(pathComponents[i]);
		}
	}
	
	private void createFileInternal(String name) throws Exception {
		String absolutePath = getAbsolutePath(name);
		logger.info("Absolute path {}", absolutePath);
		String[] pathComponents = absolutePath.split("/");
		Directory prev = root;
		int i = 1;
		while(i < pathComponents.length - 1) {
			prev = prev.mkdir(pathComponents[i++]);
		}
		if(prev.contains(pathComponents[i])) {
			throw new Exception(prev.getPath() + " already contains a node named " + name);
		}
		prev.touch(pathComponents[i]);
	}
	
	public void createFile(String name) throws Exception {
		createFileInternal(name);
	}
	
	private Directory findDirectory(String name) throws Exception {
		String absolutePath = getAbsolutePath(name);
		logger.info("Absolute path {}", absolutePath);
		
		String[] pathComponents = absolutePath.split("/");
		Directory prev = root;
		
		for (int i = 1; i < pathComponents.length; i++) {
			Node child = prev.getChild(pathComponents[i]);
			if (!child.isDirectory()) {
				throw new Exception(child.printPath() + " is not a directory");
			}
			prev = (Directory) child;
		}

		return prev;
	}
	
	private File findFile(String name) throws Exception {
		String absolutePath = getAbsolutePath(name);
		logger.info("Absolute path {}", absolutePath);
		
		String[] pathComponents = absolutePath.split("/");
		Directory prev = root;
		int i;
		for (i = 1; i < pathComponents.length - 1; i++) {
			Node child = prev.getChild(pathComponents[i]);
			if (!child.isDirectory()) {
				throw new Exception(child.printPath() + " is not a directory");
			}
			prev = (Directory) child;
		}
		
		Node file = prev.getChild(pathComponents[i]);
		if(file == null) {
			throw new Exception(pathComponents[i] + " could not be found");
		}
		
		if(file.isDirectory()) {
			throw new Exception(pathComponents[i] + " is not a file");
		}
		
		return (File)file;
	}
	
	public void appendFile(String text, String name) throws Exception {
		File file = findFile(name);
		file.getContent().append(text);
	}
	
	public String displayFile(String name) throws Exception {
		File file = findFile(name);
		return file.getContent().getText();
	}
	
	public void removeFile(String name) throws Exception {
		File file = findFile(name);
		Directory parent = (Directory)file.getParent();
		parent.removeChild(file.getName());
	}
	
	public void moveFile(String src, String dest) throws Exception {
		File file = findFile(src);
		Directory parent = (Directory)file.getParent();
		parent.removeChild(file.getName());
		
		Directory dir = findDirectory(dest);
		dir.addChild(file);
	}

	public void copyFile(String src, String dest) throws Exception {
		File file = findFile(src);		
		Directory dir = findDirectory(dest);
		dir.addSubTree(file);
	}

	public void copyDirectory(String src, String dest) throws Exception {
		Directory sdir = findDirectory(src);		
		Directory ddir = findDirectory(dest);
		ddir.addSubTree(sdir);
	}

	public void moveDir(String src, String dest) throws Exception {
		Directory sdir = findDirectory(src);
		Directory ddir = findDirectory(dest);
		
		String spath = sdir.getPath();
		String dpath = ddir.getPath();
		// check for recursive move
		if (dpath.startsWith(spath)) {
			throw new Exception("Can't move parent directory " + spath + " inside child directory " + dpath);
		}
		
		Directory parent = (Directory)sdir.getParent();
		parent.removeChild(sdir.getName());
		
		ddir.addChild(sdir);
	}
	
	public void removeDir(String name) throws Exception {
		Directory dir = findDirectory(name);
		Directory parent = (Directory)dir.getParent();
		parent.removeChild(dir.getName());
	}
	
	public void changeDir(String name) throws Exception {
		currentDir = findDirectory(name);
		logger.info("Directory changed to {}", currentDir.getPath());
	}
	
	public List<String> listDirectory(String name) throws Exception {
		Directory dir = findDirectory(name);
		return dir.getChildren();
	}
	
	public Set<String> findNode(String name) throws Exception {
		Set<String> res = currentDir.findChildren(name);
		if (res.isEmpty()) {
			throw new Exception(name + " not found in " + currentDir.getPath());
		}
		return res;
	}
}
