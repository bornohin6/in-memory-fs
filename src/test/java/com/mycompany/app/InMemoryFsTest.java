package com.mycompany.app;

import java.util.List;

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

}
