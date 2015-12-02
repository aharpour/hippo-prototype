package com.tdclighthouse.prototype.beanmapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
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
import com.tdclighthouse.prototype.utils.ImportException;
import com.tdclighthouse.prototype.utils.PluginConstants;
import com.tdclighthouse.prototype.utils.PluginConstants.NodeType;
import com.tdclighthouse.prototype.utils.PluginConstants.PropertyName;

public class HtmlFieldUpdater implements DynamicNodeUpdater {

    private final ValueFactory valueFactory = ValueFactoryImpl.getInstance();

    private static final Logger LOG = LoggerFactory.getLogger(HtmlFieldUpdater.class);

    // <img(\s[^>]*)>
    private static final Pattern IMAGE_PATTERN = Pattern.compile("<img(\\s[^>]*)>");

    // .*src="([^'|^"]*)".*
    private static final Pattern IMAGE_SOURCE_PATTERN = Pattern.compile(".*src=\"([^'|^\"]*)\".*");

    // <a\s[^>]*href=["|']([^"^']*)["|'][^>]*>((?!</a).*)</a>
    private static final Pattern LINK_PATTERN = Pattern
            .compile("<a\\s[^>]*href=[\"|']([^\"^']*)[\"|'][^>]*>((?!</a).*)</a>");

    @Override
    public DynamicNode update(DynamicNode dynamicNode) throws ImportException {
        Set<Entry<String, List<DynamicNode>>> entrySet = dynamicNode.getSubnodes().entrySet();
        for (Entry<String, List<DynamicNode>> entry : entrySet) {
            for (DynamicNode n : entry.getValue()) {
                if ("hippostd:html".equals(n.getType())) {
                    updateHtmlNode(n);
                } else {
                    update(n);
                }
            }
        }
        return dynamicNode;
    }

    private void updateHtmlNode(DynamicNode htmlNode) throws ImportException {
        try {
            Value htmlProperty = (Value) htmlNode
                    .getPropertyByRelativePath(PluginConstants.PropertyName.HIPPOSTD_CONTENT);
            String html = htmlProperty.getString();
            List<MatchedItem> matchedItems = new ArrayList<HtmlFieldUpdater.MatchedItem>();
            processImageTags(htmlNode, html, matchedItems);
            processLinkTags(htmlNode, html, matchedItems);
            if (!matchedItems.isEmpty()) {
                String updatedHtml = replace(html, matchedItems);
                htmlNode.overrideProperty(PluginConstants.PropertyName.HIPPOSTD_CONTENT,
                        valueFactory.createValue(updatedHtml));
            }
        } catch (ParseException | RepositoryException e) {
            throw new ImportException(e);
        }
    }

    private void processLinkTags(DynamicNode htmlNode, String html, List<MatchedItem> matchedItems)
            throws ImportException {
        Matcher matcher = LINK_PATTERN.matcher(html);
        while (matcher.find()) {
            String link = matcher.group(1);
            if (!isExternal(link)) {
                String nodeName = addFacetSelectNode(link, htmlNode);
                String replacement;
                if (nodeName != null) {
                    replacement = nodeName;
                } else {
                    replacement = "";
                }
                matchedItems.add(new MatchedItem(matcher.start(1), matcher.end(1), replacement));
            }
        }

    }

    private boolean isExternal(String link) {
        boolean result = link.startsWith("http://");
        result = result || link.startsWith("https://");
        result = result || link.startsWith("news://");
        result = result || link.startsWith("ftp://");
        result = result || link.startsWith("mailto:");
        return result;
    }

    private void processImageTags(DynamicNode htmlNode, String html, List<MatchedItem> matchedItems)
            throws ImportException {
        Matcher matcher = IMAGE_PATTERN.matcher(html);
        while (matcher.find()) {
            String attributes = matcher.group(1);
            Matcher sourceMatcher = IMAGE_SOURCE_PATTERN.matcher(attributes);
            if (sourceMatcher.matches()) {

                String nodeName = addFacetSelectNode(sourceMatcher.group(1), htmlNode);
                String replacement;
                if (nodeName != null) {
                    replacement = "<img src=\"" + nodeName + "/{_document}/hippogallery:original\"/>";
                } else {
                    replacement = "";
                }

                matchedItems.add(new MatchedItem(matcher.start(), matcher.end(), replacement));
            }

        }
    }

    private String replace(String input, List<MatchedItem> matchedItems) {
        StringBuilder sb = new StringBuilder(input);
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
     * @throws ImportException
     */
    private String addFacetSelectNode(String filePath, DynamicNode parent) throws ImportException {
        try {
            String nodeName = null;
            String fileName = FileUtils.getFileName(filePath);
            if (StringUtils.isNotBlank(fileName) && StringUtils.isNotBlank(filePath)) {
                nodeName = fileName;
                if (parent.getNodeByRelativePath(fileName) == null) {
                    DynamicNode facetSelectNode = new DynamicNode(NodeType.HIPPO_FACETSELECT, parent);

                    facetSelectNode.addProperty(PathParserUtil.parse(PropertyName.HIPPO_DOCBASE),
                            valueFactory.createValue(filePath));
                    facetSelectNode.addProperty(PathParserUtil.parse(PropertyName.HIPPO_FACETS), new Value[0]);
                    facetSelectNode.addProperty(PathParserUtil.parse(PropertyName.HIPPO_MODES), new Value[0]);
                    facetSelectNode.addProperty(PathParserUtil.parse(PropertyName.HIPPO_VALUES), new Value[0]);

                    parent.addSubnode(parent, facetSelectNode, nodeName);
                }
            } else {
                if (StringUtils.isNotBlank(fileName)) {
                    LOG.error("the given file path \"" + filePath + "\" does not follow the required syntax");
                } else {
                    LOG.error("could not file a file at \"" + filePath
                            + "\" therefore the image tag in the html is going to be removed");
                }

            }
            return nodeName;
        } catch (ParseException e) {
            throw new ImportException(e);
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
