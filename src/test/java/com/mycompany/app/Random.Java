import java.util.*;

class File {
    private String name;
    private String content;

    public File(String name) {
        this.name = name;
        this.content = "";
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

class Directory {
    private String name;
    private Map<String, Directory> subdirectories;
    private Map<String, File> files;

    public Directory(String name) {
        this.name = name;
        this.subdirectories = new HashMap<>();
        this.files = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<String, Directory> getSubdirectories() {
        return subdirectories;
    }

    public Map<String, File> getFiles() {
        return files;
    }
}

public class InMemoryFileSystem {
    private Directory root;
    private Directory currentDirectory;

    public InMemoryFileSystem() {
        root = new Directory("/");
        currentDirectory = root;
    }

    public String getCurrentDirectoryPath() {
        return getPath(currentDirectory);
    }

    public void changeDirectory(String directoryName) {
        if (directoryName.equals("..")) {
            if (currentDirectory != root) {
                currentDirectory = getParentDirectory(currentDirectory);
            }
        } else if (currentDirectory.getSubdirectories().containsKey(directoryName)) {
            currentDirectory = currentDirectory.getSubdirectories().get(directoryName);
        }
    }

    public void createDirectory(String directoryName) {
        if (!currentDirectory.getSubdirectories().containsKey(directoryName)) {
            currentDirectory.getSubdirectories().put(directoryName, new Directory(directoryName));
        }
    }

    public List<String> getDirectoryContents() {
        List<String> contents = new ArrayList<>();
        contents.addAll(currentDirectory.getSubdirectories().keySet());
        contents.addAll(currentDirectory.getFiles().keySet());
        return contents;
    }

    public void removeDirectory(String directoryName) {
        currentDirectory.getSubdirectories().remove(directoryName);
    }

    public void createFile(String fileName) {
        if (!currentDirectory.getFiles().containsKey(fileName)) {
            currentDirectory.getFiles().put(fileName, new File(fileName));
        }
    }

    public void writeFile(String fileName, String content) {
        File file = currentDirectory.getFiles().get(fileName);
        if (file != null) {
            file.setContent(content);
        }
    }

    public String readFile(String fileName) {
        File file = currentDirectory.getFiles().get(fileName);
        return (file != null) ? file.getContent() : null;
    }

    public void moveFile(String sourceFileName, String destinationFileName) {
        File sourceFile = currentDirectory.getFiles().get(sourceFileName);
        if (sourceFile != null) {
            currentDirectory.getFiles().remove(sourceFileName);
            currentDirectory.getFiles().put(destinationFileName, sourceFile);
        }
    }

    public List<String> findFiles(String fileName) {
        List<String> results = new ArrayList<>();
        for (String name : currentDirectory.getFiles().keySet()) {
            if (name.equals(fileName)) {
                results.add(getPath(currentDirectory) + "/" + name);
            }
        }
        for (Directory subdir : currentDirectory.getSubdirectories().values()) {
            results.addAll(findFilesInDirectory(subdir, fileName));
        }
        return results;
    }

    private List<String> findFilesInDirectory(Directory directory, String fileName) {
        List<String> results = new ArrayList<>();
        for (String name : directory.getFiles().keySet()) {
            if (name.equals(fileName)) {
                results.add(getPath(directory) + "/" + name);
            }
        }
        for (Directory subdir : directory.getSubdirectories().values()) {
            results.addAll(findFilesInDirectory(subdir, fileName));
        }
        return results;
    }

    private String getPath(Directory directory) {
        if (directory == root) {
            return "/";
        } else {
            return getPath(directory.getParent()) + "/" + directory.getName();
        }
    }

    private Directory getParentDirectory(Directory directory) {
        if (directory == root) {
            return root;
        }
        for (Directory parent : currentDirectory.getParent().getSubdirectories().values()) {
            if (parent.getSubdirectories().containsValue(directory)) {
                return parent;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        System.out.println("Current Directory: " + fs.getCurrentDirectoryPath());

        fs.createDirectory("school");
        fs.createDirectory("work");
        fs.changeDirectory("school");
        fs.createDirectory("homework");
        fs.createFile("report.txt");
        fs.writeFile("report.txt", "This is my homework report.");
        fs.createFile("notes.txt");
        fs.writeFile("notes.txt", "These are my class notes.");

        System.out.println("Current Directory: " + fs.getCurrentDirectoryPath());

        List<String> contents = fs.getDirectoryContents();
        System.out.println("Contents of Current Directory: " + contents);

        fs.moveFile("notes.txt", "new_notes.txt");

        List<String> foundFiles = fs.findFiles("new_notes.txt");
        System.out.println("Found Files: " + foundFiles);
    }
}
