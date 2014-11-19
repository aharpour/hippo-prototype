package com.tdclighthouse.prototype.utils;

import org.junit.Assert;
import org.junit.Test;

public class PathUtilsTest {
    
    @Test
    public void normalizeTest() {
        Assert.assertEquals("test/something", PathUtils.normalize("test/something/"));
        Assert.assertEquals("test/something", PathUtils.normalize("/test/something"));
        Assert.assertEquals("test/something", PathUtils.normalize("/test/something/"));
        Assert.assertEquals("test/something", PathUtils.normalize("//test/something///"));
    }

}
