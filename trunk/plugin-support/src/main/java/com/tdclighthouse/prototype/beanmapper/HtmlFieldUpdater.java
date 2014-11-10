package com.tdclighthouse.prototype.beanmapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.value.ValueFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.hippo.beanmapper.DynamicNode;
import com.tdclighthouse.hippo.beanmapper.utils.PathParserUtil;
import com.tdclighthouse.prototype.beanmapper.DynamicNodeWriter.DynamicNodeUpdater;
import com.tdclighthouse.prototype.utils.FileUtils;
import com.tdclighthouse.prototype.utils.PluginConstants;
import com.tdclighthouse.prototype.utils.PluginConstants.NodeType;
import com.tdclighthouse.prototype.utils.PluginConstants.PropertyName;

public class HtmlFieldUpdater implements DynamicNodeUpdater {

    private final ValueFactory valueFactory = ValueFactoryImpl.getInstance();
    public static Logger log = LoggerFactory.getLogger(HtmlFieldUpdater.class);

    private static final Pattern IMAGE_PATTERN = Pattern
            .compile("<Image\\s*>\\s*<File\\s*>\\s*([^<]*)\\s*</File\\s*>\\s*</Image\\s*>");

    @Override
    public DynamicNode update(DynamicNode dynamicNode) throws RepositoryException {
        try {
            if ("estro:Abstract".equals(dynamicNode.getType())) {
                updateHtmlNode(dynamicNode.getNodeByRelativePath("estro:content(estro:AbstactContent)/estro:summary"));
                updateHtmlNode(dynamicNode.getNodeByRelativePath("estro:content(estro:AbstactContent)/estro:abstract"));
            }
            return dynamicNode;
        } catch (ParseException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }

    private void updateHtmlNode(DynamicNode htmlNode) throws ParseException, RepositoryException {
        Value htmlProperty = (Value) htmlNode.getPropertyByRelativePath(PluginConstants.PropertyName.HIPPOSTD_CONTENT);
        String html = htmlProperty.getString();
        Matcher matcher = IMAGE_PATTERN.matcher(html);
        List<MatchedItem> matchedItems = new ArrayList<HtmlFieldUpdater.MatchedItem>();
        while (matcher.find()) {
            String filePath = matcher.group(1);
            String nodeName = addFacetSelectNode(filePath, htmlNode);
            String replacement;
            if (nodeName != null) {
                replacement = "<img src=\"" + nodeName + "/{_document}/tdc:articleImage\"/>";
            } else {
                replacement = "";
            }

            matchedItems.add(new MatchedItem(matcher.start(), matcher.end(), replacement));
        }
        if (matchedItems.size() > 0) {
            String updatedHtml = replace(html, matchedItems);
            htmlNode.overrideProperty(PluginConstants.PropertyName.HIPPOSTD_CONTENT,
                    valueFactory.createValue(updatedHtml));
        }
    }

    private String replace(String input, List<MatchedItem> matchedItems) {
        StringBuffer sb = new StringBuffer(input);
        for (int i = matchedItems.size() - 1; i >= 0; i--) {
            MatchedItem matchedItem = matchedItems.get(i);
            sb.replace(matchedItem.start, matchedItem.end, matchedItem.replacement);
        }
        return sb.toString();
    }

    /**
     * @param filePath
     * @param parent
     * @return node name.
     */
    private String addFacetSelectNode(String filePath, DynamicNode parent) {
        try {
            String nodeName = null;
            String fileName = FileUtils.getFileName(filePath);
            if (StringUtils.isNotBlank(fileName) && StringUtils.isNotBlank(filePath)) {
                nodeName = fileName;
                DynamicNode facetSelectNode = new DynamicNode(NodeType.HIPPO_FACETSELECT, parent);

                facetSelectNode.addProperty(PathParserUtil.parse(PropertyName.HIPPO_DOCBASE),
                        valueFactory.createValue(filePath));
                facetSelectNode.addProperty(PathParserUtil.parse(PropertyName.HIPPO_FACETS), new Value[0]);
                facetSelectNode.addProperty(PathParserUtil.parse(PropertyName.HIPPO_MODES), new Value[0]);
                facetSelectNode.addProperty(PathParserUtil.parse(PropertyName.HIPPO_VALUES), new Value[0]);

                parent.addSubnode(parent, facetSelectNode, nodeName);
            } else {
                if (StringUtils.isNotBlank(fileName)) {
                    log.error("the given file path \"" + filePath + "\" does not follow the required syntax");
                } else {
                    log.error("could not file a file at \"" + filePath
                            + "\" therefore the image tag in the html is going to be removed");
                }

            }
            return nodeName;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    
    private static class MatchedItem {

        private final int start;
        private final int end;
        private final String replacement;

        public MatchedItem(int start, int end, String replacement) {
            this.start = start;
            this.end = end;
            this.replacement = replacement;
        }

    }

}
