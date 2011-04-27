package org.hygga.junit;

import org.junit.Test;

import junit.framework.Assert;

public class SimpleUnitTestCase {

    
    @Test
    public void testTrue(){
	Assert.assertEquals("test", "test");
    }
}
