package com.mycompany.app;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class CommandLineMain {

	private final String PWD = "pwd";
	private final String MKDIR = "mkdir";
	private final String CHDIR = "chdir";
	private final String LS = "ls";
	private final String EXIT = "exit";
	private final String HELP = "help";
	
    private static final Logger logger = LogManager.getLogger(CommandLineMain.class);
	
	private final InMemoryFS fs;
	
	CommandLineMain() {
		fs = new InMemoryFS();
	}
	
	private void createDirectory(String directoryName) {
		if(fs.createDirectory(directoryName)) {
			System.out.println("Directory '" + directoryName + "' created.");
		}
	}

	public void run() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Welcome to the In Memory Filesystem!");
		InMemoryFS fs = new InMemoryFS(); // TODO singleton

		while (true) {
			// TODO add help
			System.out.print("Enter a command (type 'exit' to quit): ");
			String input = scanner.nextLine().trim();
			// Process the user input
			// TODO remove or put into debug mode
			System.out.println("You entered: " + input);
			
            String[] parts = input.split(" ");

            if (parts.length == 0) {
                continue;
            }

            String command = parts[0].toLowerCase();

			switch (command) {
			case LS:
				//listDirectoryContents();
				break;
			case MKDIR:
                if (parts.length == 2) {
                    createDirectory(parts[1]);
                } else {
                    System.out.println("Usage: mkdir <directoryName>");
                }				
				break;
			case CHDIR:
				//changeDirectory();
				break;
			case PWD:
				System.out.println(fs.getCurrentDir());
				break;
			case HELP:
				// help()
				break;				
			case EXIT:
				System.out.println("Goodbye!");
				scanner.close();
				return;
			default:
				System.out.println("Unknown command. Please try again.");
			}			
		}

	}
	
	public static void main(String[] args) {
		logger.debug("Debug enabled");
		logger.info("info enabled");
		logger.error("error enabled");
		CommandLineMain app = new CommandLineMain();
		app.run();
	}
}
