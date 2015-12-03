package com.tdclighthouse.prototype.utils;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilsTest {
    
    @Test
    public void removeExtention() {
        Assert.assertEquals("file", FileUtils.removeExtention("file.xml"));
        Assert.assertEquals("my.file", FileUtils.removeExtention("my.file.pdf"));
        Assert.assertEquals("file", FileUtils.removeExtention("file"));
    }

}
