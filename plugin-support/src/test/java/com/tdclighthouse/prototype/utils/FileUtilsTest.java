package com.tdclighthouse.prototype.utils;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilsTest {
    
    @Test
    public void removeExtension() {
        Assert.assertEquals("file", FileUtils.removeExtension("file.xml"));
        Assert.assertEquals("my.file", FileUtils.removeExtension("my.file.pdf"));
        Assert.assertEquals("file", FileUtils.removeExtension("file"));
    }

}
