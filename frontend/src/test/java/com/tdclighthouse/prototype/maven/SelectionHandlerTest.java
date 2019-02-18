package com.tdclighthouse.prototype.maven;

import net.sourceforge.mavenhippo.gen.HandlerResponse;
import net.sourceforge.mavenhippo.gen.ImportRegistry;
import net.sourceforge.mavenhippo.model.ContentTypeBean;
import net.sourceforge.mavenhippo.model.ContentTypeBean.Item;
import net.sourceforge.mavenhippo.model.ContentTypeBean.Template;
import net.sourceforge.mavenhippo.utils.exceptions.GeneratorException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class SelectionHandlerTest {

    @Test
    public void normalCaseTest() throws GeneratorException {
        SelectionHandler selectionHandler = new SelectionHandler(null, null, null, null);

        ContentTypeBean contentTypeBean = EasyMock.createMock(ContentTypeBean.class);
        Item item = createMockItem(contentTypeBean, "ns:myField", "myField", false, "DynamicDropdown");
        Template template = createMockTemplate("/path/to/value/list");
        EasyMock.expect(contentTypeBean.getTemplate(item)).andReturn(template);

        EasyMock.replay(contentTypeBean, template);

        ImportRegistry importRegistry = new ImportRegistry();
        HandlerResponse handle = selectionHandler.handle(item, importRegistry);
        Assert.assertEquals(1, handle.getPropertyGenerators().size());
        Assert.assertEquals(1, handle.getMethodGenerators().size());
        Assert.assertEquals("private SelectionBean myField;", handle.getPropertyGenerators().get(0).getFragment());
        Assert.assertEquals(
                "public SelectionBean getMyField() {\n    if (this.myField == null) {\n        this.myField = getSelectionBean(\"ns:myField\", \"/path/to/value/list\");\n    }\n    return this.myField;\n}",
                handle.getMethodGenerators().get(0).getFragment());
    }

    @Test
    public void normalMultivalueCaseTest() throws GeneratorException {
        SelectionHandler selectionHandler = new SelectionHandler(null, null, null, null);

        ContentTypeBean contentTypeBean = EasyMock.createMock(ContentTypeBean.class);
        Item item = createMockItem(contentTypeBean, "ns:myField", "myField", true, "DynamicDropdown");
        Template template = createMockTemplate("/path/to/value/list");
        EasyMock.expect(contentTypeBean.getTemplate(item)).andReturn(template);

        EasyMock.replay(contentTypeBean, template);

        ImportRegistry importRegistry = new ImportRegistry();
        HandlerResponse handle = selectionHandler.handle(item, importRegistry);
        Assert.assertEquals(1, handle.getPropertyGenerators().size());
        Assert.assertEquals(1, handle.getMethodGenerators().size());
        Assert.assertEquals("private SelectionBean myField;", handle.getPropertyGenerators().get(0).getFragment());
        Assert.assertEquals(
                "public SelectionBean getMyField() {\n    if (this.myField == null) {\n        this.myField = getSelectionBean(\"ns:myField\", \"/path/to/value/list\");\n    }\n    return this.myField;\n}",
                handle.getMethodGenerators().get(0).getFragment());
    }

    @Test
    public void noTemplateTest() throws GeneratorException {
        SelectionHandler selectionHandler = new SelectionHandler(null, null, null, null);

        ContentTypeBean contentTypeBean = EasyMock.createMock(ContentTypeBean.class);
        Item item = createMockItem(contentTypeBean, "ns:myField", "myField", false, "DynamicDropdown");
        EasyMock.expect(contentTypeBean.getTemplate(item)).andReturn(null);

        EasyMock.replay(contentTypeBean);

        ImportRegistry importRegistry = new ImportRegistry();
        HandlerResponse handle = selectionHandler.handle(item, importRegistry);
        Assert.assertEquals(null, handle);
    }

    @Test
    public void NoADynamicDropDownTest() throws GeneratorException {
        SelectionHandler selectionHandler = new SelectionHandler(null, null, null, null);

        ContentTypeBean contentTypeBean = EasyMock.createMock(ContentTypeBean.class);
        Item item = createMockItem(contentTypeBean, "ns:myField", "myField", false, "String");
        EasyMock.expect(contentTypeBean.getTemplate(item)).andReturn(null);

        EasyMock.replay(contentTypeBean);

        ImportRegistry importRegistry = new ImportRegistry();
        HandlerResponse handle = selectionHandler.handle(item, importRegistry);
        Assert.assertEquals(null, handle);
    }

    private Template createMockTemplate(String source) {
        Template template = EasyMock.createMock(Template.class);
        EasyMock.expect(template.getOptionsValue("source")).andReturn(source);
        return template;
    }

    public Item createMockItem(ContentTypeBean contentTypeBean, String relativePath, String simpleName,
            boolean isMultible, String type) {
        Item item = EasyMock.createMock(Item.class);
        EasyMock.expect(item.getType()).andReturn(type).anyTimes();
        EasyMock.expect(item.getContentType()).andReturn(contentTypeBean).anyTimes();
        EasyMock.expect(item.getRelativePath()).andReturn(relativePath).anyTimes();
        EasyMock.expect(item.getSimpleName()).andReturn(simpleName).anyTimes();
        EasyMock.expect(item.isMultiple()).andReturn(isMultible).anyTimes();
        EasyMock.replay(item);
        return item;
    }

}
