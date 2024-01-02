package com.mycompany.app;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class InMemoryFsTest
{
	@Test
	public void testAbsolutePath() {
		InMemoryFS fs = new InMemoryFS();
		Assertions.assertEquals(fs.getCurrentDir(), "/");
		Assertions.assertEquals(fs.getAbsolutePath("/abc"), "/abc");
		Assertions.assertEquals(fs.getAbsolutePath("abc"), "/abc");
		Assertions.assertEquals(fs.getAbsolutePath("abc/def"), "/abc/def");
		Assertions.assertEquals(fs.getAbsolutePath("/abc/def"), "/abc/def");
	}

	@Test
	public void testOperations() {
		InMemoryFS fs = new InMemoryFS();
		List<String> ls;
		try {
			Assertions.assertEquals(fs.getCurrentDir(), "/");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 0);

			fs.createDirectory("/a");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);

			ls = fs.listDirectory("/a");
			Assertions.assertEquals(ls.size(), 0);

			ls = fs.listDirectory("a");
			Assertions.assertEquals(ls.size(), 0);

			fs.changeDir("/a");
			Assertions.assertEquals(fs.getCurrentDir(), "/a/");

			fs.createDirectory("/b");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 2);

			fs.createDirectory("/a"); // duplicate directory is ok
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 2);


			fs.createDirectory("b");
			fs.changeDir("b");
			Assertions.assertEquals(fs.getCurrentDir(), "/a/b/");

			ls = fs.listDirectory("/a");
			Assertions.assertEquals(ls.size(), 1);

			fs.changeDir("/");
			Assertions.assertEquals(fs.getCurrentDir(), "/");

			fs.createFile("/f");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 3);

			fs.createFile("/a/e");
			ls = fs.listDirectory("/a");
			Assertions.assertEquals(ls.size(), 2);

			fs.createFile("/c/e");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 4);

			Assertions.assertEquals(fs.getCurrentDir(), "/");
			fs.changeDir("/a");
			Assertions.assertEquals(fs.getCurrentDir(), "/a/");

		} catch(Exception e) {
			Assertions.assertTrue(false);
		}

		try {
			fs.createFile("/f"); // duplicate file is not ok
			Assertions.assertTrue(false);
		} catch(Exception e) {
		}

		try {
			fs.createFile("/a/e"); // duplicate file is not ok
			Assertions.assertTrue(false);
		} catch(Exception e) {
		}    	
	}

	@Test
	public void testFileContent() {
		InMemoryFS fs = new InMemoryFS();
		try {
			fs.createFile("/a");
			fs.appendFile("hello", "/a");
			Assertions.assertEquals(fs.displayFile("/a"), "hello");
			fs.appendFile("world", "/a");
			Assertions.assertEquals(fs.displayFile("/a"), "helloworld");
			fs.createFile("/c/b");
			fs.appendFile("hello", "c/b");
			Assertions.assertEquals(fs.displayFile("c/b"), "hello");

		} catch(Exception e) {
			Assertions.assertTrue(false);
		}
	}

	@Test
	public void testFileContentNegative() {
		InMemoryFS fs = new InMemoryFS();
		try {
			fs.createFile("/a");
			fs.appendFile("hello", "/b");
			Assertions.assertTrue(false);
		} catch(Exception e) {
		}

		// append to directory
		try {
			fs.createFile("/a/b");
			fs.appendFile("hello", "/a");
			Assertions.assertTrue(false);
		} catch(Exception e) {
		}
	}

	@Test
	public void testRemoval() {
		InMemoryFS fs = new InMemoryFS();
		List<String> ls;
		try {
			fs.createFile("/a");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);
			fs.appendFile("hello", "/a");
			Assertions.assertEquals(fs.displayFile("/a"), "hello");

			fs.removeFile("/a");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 0);

			fs.createFile("/c/d");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);

			fs.removeDir("c");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 0);			
		} catch(Exception e) {
			Assertions.assertTrue(false);
		}
	}

	@Test
	public void testRemovalNegative() {
		InMemoryFS fs = new InMemoryFS();
		List<String> ls;
		try {
			fs.createFile("/a");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);
			fs.removeDir("/a");
			Assertions.assertTrue(false);
		} catch(Exception e) {
		}

		try {
			fs.createFile("/c/d");
			ls = fs.listDirectory("c/");
			Assertions.assertEquals(ls.size(), 1);
			fs.removeFile("/c");
			Assertions.assertTrue(false);
		} catch(Exception e) {
		}
	}
	
	@Test
	public void testMove() {
		InMemoryFS fs = new InMemoryFS();
		List<String> ls;
		try {
			fs.createFile("/a");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);
			fs.appendFile("hello", "/a");
			Assertions.assertEquals(fs.displayFile("/a"), "hello");

			fs.createDirectory("/b");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 2);
			
			fs.moveFile("/a", "b");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);
			ls = fs.listDirectory("/b");
			Assertions.assertEquals(ls.size(), 1);
			Assertions.assertEquals(fs.displayFile("/b/a"), "hello");

			
			fs.createDirectory("/c/d");
			fs.moveDir("/b", "/c");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);
			ls = fs.listDirectory("/c");
			Assertions.assertEquals(ls.size(), 2);
			Assertions.assertEquals(fs.displayFile("/c/b/a"), "hello");
		} catch(Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	public void testCopyFile() {
		InMemoryFS fs = new InMemoryFS();
		List<String> ls;
		try {
			fs.createFile("/a");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);
			fs.appendFile("hello", "/a");
			Assertions.assertEquals(fs.displayFile("/a"), "hello");

			fs.createDirectory("/b");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 2);
			
			fs.copyFile("/a", "b");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 2);
			ls = fs.listDirectory("/b");
			Assertions.assertEquals(ls.size(), 1);
			Assertions.assertEquals(fs.displayFile("/b/a"), "hello");

			fs.removeFile("/a");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);
			Assertions.assertEquals(fs.displayFile("/b/a"), "hello");
		} catch(Exception e) {
			Assertions.assertTrue(false);
		}
	}

	@Test
	public void testCopyFileNegative() {
		InMemoryFS fs = new InMemoryFS();
		List<String> ls;
		try {
			fs.createFile("/a/b/f1");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);
			fs.createFile("/a/b/f1/f2");
			Assertions.assertTrue(false);
		} catch(Exception e) {
		}
		
		try {
			fs.appendFile("hello", "/a/b/f2");
			Assertions.assertTrue(false);
		} catch(Exception e) {
		}

	}

	@Test
	public void testCopyDir() {
		InMemoryFS fs = new InMemoryFS();
		List<String> ls;
		try {
			fs.createFile("/a/b/c/f1");
			ls = fs.listDirectory("/");
			Assertions.assertEquals(ls.size(), 1);
			fs.appendFile("hello1", "/a/b/c/f1");
			fs.createFile("/a/b/f2");
			fs.appendFile("hello2", "/a/b/f2");
			ls = fs.listDirectory("/a/b");
			Assertions.assertEquals(ls.size(), 2);
			Assertions.assertEquals(fs.displayFile("/a/b/c/f1"), "hello1");
			Assertions.assertEquals(fs.displayFile("/a/b/f2"), "hello2");
			
			fs.createFile("/a/d1/f3");
			fs.appendFile("hello3", "/a/d1/f3");
			ls = fs.listDirectory("/a/d1");
			Assertions.assertEquals(ls.size(), 1);
			Assertions.assertEquals(fs.displayFile("/a/d1/f3"), "hello3");
			
			fs.copyDirectory("/a/b", "/a/d1");			
			ls = fs.listDirectory("/a");
			Assertions.assertEquals(ls.size(), 2);

			ls = fs.listDirectory("/a/d1");
			Assertions.assertEquals(ls.size(), 2);
			
			// nothing changed for the old files
			Assertions.assertEquals(fs.displayFile("/a/b/c/f1"), "hello1");
			Assertions.assertEquals(fs.displayFile("/a/b/f2"), "hello2");
			Assertions.assertEquals(fs.displayFile("/a/d1/f3"), "hello3");
			
			// can read the coopied files
			Assertions.assertEquals(fs.displayFile("/a/d1/b/c/f1"), "hello1");
			Assertions.assertEquals(fs.displayFile("/a/d1/b/f2"), "hello2");

		} catch(Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	public void testCopyDirRecursive() {
		InMemoryFS fs = new InMemoryFS();
		
		try {
			fs.createDirectory("/a/b");
			fs.moveDir("/a", "/a/b");
			Assertions.assertTrue(false);
		} catch(Exception e) {
			// can't move parent inside child directory
		}
	}

	@Test
	public void testfind() {
		InMemoryFS fs = new InMemoryFS();
		
		try {
			fs.createDirectory("/a/b/f");
			fs.createDirectory("/a/b/c/f/d");
			fs.createDirectory("/a/b/c/f/d/f");
			fs.changeDir("/a/b");
			Set<String> res = fs.findNode("f");
			Assertions.assertEquals(res.size(), 3);
			Assertions.assertTrue(res.contains("./f"));
			Assertions.assertTrue(res.contains("./c/f"));
			Assertions.assertTrue(res.contains("./c/f/d/f"));
		} catch(Exception e) {
		}
	}

	@Test
	public void testfindFiles() {
		InMemoryFS fs = new InMemoryFS();
		
		try {
			fs.createDirectory("/a/b/f");
			fs.createFile("/a/b/a");
			Set<String> res = fs.findNode("a");
			Assertions.assertEquals(res.size(), 2);
			Assertions.assertTrue(res.contains("./a"));
			Assertions.assertTrue(res.contains("./a/b/a"));
		} catch(Exception e) {
		}
	}
	
	@Test
	public void testfindNegative() {
		InMemoryFS fs = new InMemoryFS();
		
		try {
			fs.createDirectory("/a/b/f");
			fs.createFile("/a/b/a");
			fs.findNode("z");
			Assertions.assertTrue(false);
		} catch(Exception e) {
		}
	}

}
