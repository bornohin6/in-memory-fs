package com.mycompany.app;

import java.util.List;
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
 */
public class CommandLineMain {

	private final String PWD = "pwd";
	private final String MKDIR = "mkdir";
	private final String CD = "cd";
	private final String LS = "ls";
	private final String TOUCH = "touch";
	private final String APPEND = "append";
	private final String CAT = "cat";
	private final String MV = "mv";
	private final String RM = "rm";
	private final String MVR = "mv-r";
	private final String CP = "cp";
	private final String CPR = "cp-r";
	private final String RMR = "rm-r";
	private final String EXIT = "exit";
	private final String HELP = "help";
	
    private static final Logger logger = LogManager.getLogger(CommandLineMain.class);
	
	private final InMemoryFS fs;
	
	CommandLineMain() {
		fs = new InMemoryFS();
	}
	
    private static void help() {
        System.out.println("Available commands:");
        System.out.println("  pwd    - Print the current working directory");
        System.out.println("  ls     - List directory contents");
        System.out.println("  cd     - Change the current directory");
        System.out.println("  mkdir  - Create directories");
        System.out.println("  touch  - Create an empty file");
        System.out.println("  append - Append text to a file");
        System.out.println("  cat    - Display file contents");
        System.out.println("  rm     - Remove a file");
        System.out.println("  rm-r   - Remove a directory");
        System.out.println("  mv     - Move files");
        System.out.println("  mv-r   - Move directories");
        System.out.println("  cp     - Copy files");
        System.out.println("  cp-r   - Copy directories");
        System.out.println("  exit   - Quit the program");
        System.out.println("  help   - Display this help message");
    }
	
	private void createDirectory(String directoryName) {
		try {
			fs.createDirectory(directoryName);
		} catch (Exception e) {
			System.out.println(e.toString());
		}	
	}

	private void createFile(String name) {
		try {
			fs.createFile(name);
		} catch (Exception e) {
			System.out.println(e.toString());
		}	
	}
	
	private void displayFile(String name) {
		try {
			System.out.println(fs.displayFile(name));
		} catch (Exception e) {
			System.out.println(e.toString());
		}	
	}
	
	private void appendFile(String text, String file) {
		try {
			fs.appendFile(text, file);
		} catch (Exception e) {
			System.out.println(e.toString());
		}			
	}
	
	private void removeFile(String name) {
		try {
			fs.removeFile(name);
		} catch (Exception e) {
			System.out.println(e.toString());
		}			
	}	

	private void removeDir(String name) {
		try {
			fs.removeDir(name);
		} catch (Exception e) {
			System.out.println(e.toString());
		}			
	}
	
	private void moveFile(String src, String dest) {
		try {
			fs.moveFile(src, dest);
		} catch (Exception e) {
			System.out.println(e.toString());
		}			
	}	

	private void copyFile(String src, String dest) {
		try {
			fs.copyFile(src, dest);
		} catch (Exception e) {
			System.out.println(e.toString());
		}			
	}

	private void copyDirectory(String src, String dest) {
		try {
			fs.copyDirectory(src, dest);
		} catch (Exception e) {
			System.out.println(e.toString());
		}			
	}

	private void moveDir(String src, String dest) {
		try {
			fs.moveDir(src, dest);
		} catch (Exception e) {
			System.out.println(e.toString());
		}			
	}
	
	private void changeDirectory(String directoryName) {
		try {
			fs.changeDir(directoryName);
		} catch (Exception e) {
			System.out.println(e.toString());
		}	
	}
	
	private void listDirectory(String[] parts) {
		String dir;
		if (parts.length == 2) {
			dir = parts[1];
		} else {
			dir = fs.getCurrentDir();
		}
		try {
			logger.info("Listing directory {}", dir);
			List<String> res = fs.listDirectory(dir);
			res.forEach(System.out::println);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void run() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Welcome to the In Memory Filesystem!");

		while (true) {
			System.out.print("Enter a command (type 'help' for help, 'exit' to quit): ");
			String input = scanner.nextLine().trim();

			logger.info("You entered: {}", input);
			
            String[] parts = input.split(" ");

            if (parts.length == 0) {
                continue;
            }

            String command = parts[0].toLowerCase();

			switch (command) {
			case LS:	
				if (parts.length <= 2) {
					listDirectory(parts);
				} else {
					System.out.println("Usage: ls [<directoryName>]");
				}
				break;
			case MKDIR:
                if (parts.length == 2) {
                    createDirectory(parts[1]);
                } else {
                    System.out.println("Usage: mkdir <directoryName>");
                }				
				break;
			case CD:
                if (parts.length == 2) {
                    changeDirectory(parts[1]);
                } else {
                    System.out.println("Usage: chdir <directoryName>");
                }					break;
			case PWD:
				System.out.println(fs.getCurrentDir());
				break;
			case TOUCH:
                if (parts.length == 2) {
                    createFile(parts[1]);
                } else {
                    System.out.println("Usage: touch <fileName>");
                }				
                break;
			case CAT:
                if (parts.length == 2) {
                    displayFile(parts[1]);
                } else {
                    System.out.println("Usage: cat <fileName>");
                }				
                break;                
			case APPEND:
                if (parts.length == 3) {
                    appendFile(parts[1], parts[2]);
                } else {
                    System.out.println("Usage: append <word> <fileName>");
                }				
				break;
			case RM:
                if (parts.length == 2) {
                    removeFile(parts[1]);
                } else {
                    System.out.println("Usage: rm <name>");
                }				
				break;
			case RMR:
                if (parts.length == 2) {
                    removeDir(parts[1]);
                } else {
                    System.out.println("Usage: rm-r <name>");
                }				
				break;
			case MV:
                if (parts.length == 3) {
                    moveFile(parts[1], parts[2]);
                } else {
                    System.out.println("Usage: mv <fileName> <dirName>");
                }				
				break;
			case MVR:
                if (parts.length == 3) {
                    moveDir(parts[1], parts[2]);
                } else {
                    System.out.println("Usage: mv-r <srcDirName> <destDirName>");
                }				
				break;
			case CP:
                if (parts.length == 3) {
                    copyFile(parts[1], parts[2]);
                } else {
                    System.out.println("Usage: cp <srcFileName> <destDirName>");
                }				
				break;
			case CPR:
                if (parts.length == 3) {
                    copyDirectory(parts[1], parts[2]);
                } else {
                    System.out.println("Usage: cp <srcDirName> <destDirName>");
                }				
				break;
			case HELP:
				help();
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
		CommandLineMain app = new CommandLineMain();
		app.run();
	}
}
