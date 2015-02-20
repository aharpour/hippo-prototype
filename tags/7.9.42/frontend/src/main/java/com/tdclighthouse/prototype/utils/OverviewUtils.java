package com.tdclighthouse.prototype.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentIterator;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.core.component.HstRequest;

import com.tdclighthouse.prototype.componentsinfo.PaginatedInfo;

public class OverviewUtils {

	private OverviewUtils() {
	}

	public static int getPageNumber(HstRequest request) {
		int result = 1;
		String pageString = getNamespacedOrSimpleParameter(request, Constants.ParametersConstants.PAGE);
		if (StringUtils.isNotBlank(pageString)
				&& StringUtils.isNumeric(pageString)) {
			result = Integer.parseInt(pageString);
		}
		return result;
	}

	public static int getPageSize(HstRequest request, Object parametersInfo) {
		int result = 25;
		String pageSzieString = getNamespacedOrSimpleParameter(request, Constants.ParametersConstants.PAGE_SIZE);
		if (StringUtils.isNotBlank(pageSzieString)
				&& StringUtils.isNumeric(pageSzieString)) {
			result = Integer.parseInt(pageSzieString);
		} else if (parametersInfo instanceof PaginatedInfo) {
			result = ((PaginatedInfo) parametersInfo).getDefaultPageSize();
		}

		return result;
	}

    public static String getNamespacedOrSimpleParameter(HstRequest request, String parameterName) {
        String parameter = request.getParameter(parameterName);
		if (StringUtils.isBlank(parameter)) {
		    parameter = request.getRequestContext().getServletRequest()
		            .getParameter(parameterName);
		    
		}
        return parameter;
    }

	public static PaginatorWidget getPaginator(HstRequest request,
			int defaultPageSzie, int totalRows) {
		return new PaginatorWidget(totalRows, getPageNumber(request),
				defaultPageSzie);
	}
	
    public static List<HippoDocumentBean> getItemsFromResultSet(HippoFolderBean resultSet, PaginatorWidget paginatorWidget) {
        HippoDocumentIterator<HippoDocumentBean> documentIterator = resultSet
                .getDocumentIterator(HippoDocumentBean.class);
        return getItemsFromHippoDocumentIterator(documentIterator, paginatorWidget);
    }

    public static <T> List<T> getItemsFromResultSet(HstQueryResult resultSet, PaginatorWidget paginatorWidget) {
        HippoBeanIterator hippoBeans = resultSet.getHippoBeans();
        return getItemsFromHippoBeanIterator(hippoBeans, paginatorWidget);
    }
    
    public static <T> List<T> getItemsFromHippoDocumentIterator(HippoDocumentIterator<T> documentIterator,
            PaginatorWidget paginatorWidget) {
        List<T> items = new ArrayList<T>(paginatorWidget.getRowsPerPage());
        int start = (paginatorWidget.getPage() - 1) * paginatorWidget.getRowsPerPage();
        documentIterator.skip(start);
        for (int i = 0; (i < paginatorWidget.getRowsPerPage()) && documentIterator.hasNext(); i++) {
            items.add(documentIterator.next());
        }
        return items;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> List<T> getItemsFromHippoBeanIterator(HippoBeanIterator documentIterator,
            PaginatorWidget paginatorWidget) {
        List<T> items = new ArrayList<T>(paginatorWidget.getRowsPerPage());
        int start = (paginatorWidget.getPage() - 1) * paginatorWidget.getRowsPerPage();
        documentIterator.skip(start);
        for (int i = 0; (i < paginatorWidget.getRowsPerPage()) && documentIterator.hasNext(); i++) {
            items.add((T) documentIterator.next());
        }
        return items;
    }
}
