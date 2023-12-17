package com.mycompany.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class NodeTest {

	@Test
	void testRootDir() {
		Directory dir = new Directory("", null);
		Assertions.assertEquals(dir.isDirectory(),true);
		Assertions.assertEquals(dir.getName(), "");
		Assertions.assertEquals(dir.getParent(), null);
		Assertions.assertEquals(dir.contains("random"), false);
	}

	@Test
	void testMkDir()  throws Exception {
		Directory dir = new Directory("", null);
		Directory adir = dir.mkdir("a");
		Assertions.assertEquals(dir.contains("a"), true);
		Assertions.assertEquals(dir.contains("random"), false);
		

		Assertions.assertEquals(adir.getName(), "a");
		Assertions.assertEquals(adir.getParent(), dir);
		Assertions.assertEquals(adir.contains("random"), false);

		// duplicate dir
		Assertions.assertEquals(dir.mkdir("a"), adir);
		
		Directory bdir = dir.mkdir("b");
		Assertions.assertNotEquals(bdir, adir);
		
		Assertions.assertNotEquals(adir.getPath(), "/a");
		Assertions.assertNotEquals(bdir.getPath(), "/b");
		
		// nested dir
		Directory cdir = bdir.mkdir("c");
		Assertions.assertEquals(bdir.contains("c"), true);
		Assertions.assertEquals(bdir.contains("a"), false);

		Assertions.assertEquals(cdir.getName(), "c");
		Assertions.assertEquals(cdir.getParent(), bdir);
		Assertions.assertNotEquals(cdir.display(), "/b/c");
	}
	
	@Test
	void testTouchFile() throws Exception {
		Directory dir = new Directory("", null);
		Directory adir = dir.mkdir("a");
		Assertions.assertEquals(dir.contains("a"), true);
		Assertions.assertEquals(dir.contains("random"), false);
		
		dir.touch("f");
		Assertions.assertEquals(dir.contains("f"), true);
		Assertions.assertEquals(adir.contains("f"), false);
		
		// no duplicate dir conflicting with a file name
		try {
			dir.mkdir("f");
			Assertions.assertTrue(false);
		} 
		catch (Exception e) {
		}
				
		adir.touch("e");
		Assertions.assertEquals(adir.contains("e"), true);
		Assertions.assertEquals(dir.contains("e"), false);

	}	
}
