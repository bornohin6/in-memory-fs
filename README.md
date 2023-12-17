# In-Memory File System

This is an in-memory file system that supports both files and directories. It allows you to perform various file and directory operations within a virtual file system that exists entirely in memory.

## Building

To build the project, run the following command:

```
mvn clean install
```
## Running
```
java -jar target/in-memory-fs-1.0-SNAPSHOT.jar
```

## Available commands
```
pwd: Print the current working directory.
ls: List directory contents.
cd: Change the current directory.
mkdir: Create directories.
touch: Create an empty file.
append: Append text to a file.
cat: Display file contents.
rm: Remove a file.
rm-r: Remove a directory.
mv: Move files.
mv-r: Move directories.
cp: Copy files.
cp-r: Copy directories.
exit: Quit the program.
help: Display this help message.
```

## Note
When performing basic operations, you can use absolute paths or relative paths with respect to the current working directory. However, special paths like . or .. are not supported.

Enjoy!