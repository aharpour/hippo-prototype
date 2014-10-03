package com.tdclighthouse.prototype.components;

import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.junit.Assert;
import org.junit.Test;

public class NotFoundTest {

    @Test
    public void doBeforeRenderTest() {
        NotFound notFound = new NotFound();
        MockHstResponse response = new MockHstResponse();
        notFound.doBeforeRender(null, response);
        Assert.assertEquals(404, response.getStatusCode());
    }
}
