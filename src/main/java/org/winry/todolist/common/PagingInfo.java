package org.winry.todolist.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@SuppressWarnings("serial")
public class PagingInfo extends TagSupport {
    private static final Logger logger = LoggerFactory.getLogger(PagingInfo.class);

    private String name = null;
    private String href = null;
    private int totalRecordCount = 0;
    private int recordsPerPage = 0;
    private int currentPage = 1;
    private int startRecord = 0;
    private int startIndex = 0;
    private int endIndex = 0;
    private int lastIndex = 0;
    private List<Page> pages = new ArrayList<Page>();
    private Page firstPage = null;
    private Page lastPage = null;
    private Page previousPage = null;
    private Page nextPage = null;
    private int indexPerPage = 10;
    private String crud;
    private String del;

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getCrud() {
        return crud;
    }

    public void setCrud(String crud) {
        this.crud = crud;
    }

    public String getName() {
        return name;
    }

    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public int getIndexPerPage() {
        return indexPerPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public Page getFirstPage() {
        return firstPage;
    }

    public Page getLastPage() {
        return lastPage;
    }

    public Page getPreviousPage() {
        return previousPage;
    }

    public Page getNextPage() {
        return nextPage;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public void setRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public void setIndexPerPage(int indexPerPage) {
        this.indexPerPage = indexPerPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setFirstPage(Page firstPage) {
        this.firstPage = firstPage;
    }

    public void setLastPage(Page lastPage) {
        this.lastPage = lastPage;
    }

    public int getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }

    public int doStartTag() throws JspException {

        try {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

            findStartEndIndex();

            this.href = getHref(request);
            for (int i = startIndex; i <= endIndex; i++) {
                pages.add(new Page(i, getURL(i)));
            }

            firstPage = new Page(startIndex, getURL(1));
            lastPage = new Page(lastIndex, getURL(lastIndex));

            if (currentPage - 10 > 0) {
                previousPage = new Page(currentPage - 10, getURL(currentPage - 10));
            } else {
                previousPage = firstPage;
            }

            if (currentPage + 10 < endIndex || currentPage + 10 < lastIndex) {
                nextPage = new Page(currentPage + 10, getURL(currentPage + 10));
            } else {
                nextPage = lastPage;
            }

            pageContext.setAttribute(this.getName(), this);

        } catch (Exception e) {
            logger.error(e.toString());
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        pages.clear();
        firstPage = null;
        lastPage = null;
        previousPage = null;
        nextPage = null;
        return SKIP_BODY;
    }

    private void findStartEndIndex() {

        int interval = indexPerPage;
        lastIndex = totalRecordCount / recordsPerPage;
        if (totalRecordCount % recordsPerPage > 0) {
            lastIndex += 1;
        }

        // logger.debug("interval : {}", interval);
        // logger.debug("last index : {}", lastIndex);
        startIndex = 1;
        while (true) {
            endIndex = startIndex + interval - 1;
            if (currentPage >= startIndex && currentPage <= endIndex && endIndex <= lastIndex) {
                break;
            }

            if (endIndex > lastIndex) {
                endIndex = lastIndex;
                break;
            }
            startIndex = endIndex + 1;
        }

        if (endIndex == 0) {
            endIndex = 1;
        }
    }

    private String getURL(int index) {
        // return href + "&page=" + index;
        String concatUrl = (href.indexOf("?") == -1) ? "?" : "&";
        StringBuffer sb = new StringBuffer();
        sb.append(href).append(concatUrl).append("currentPage=").append(index);
        return sb.toString();
    }

    private String getHref(HttpServletRequest request) {

        String href = (String) request.getAttribute("javax.servlet.forward.request_uri");

        StringBuffer sb = new StringBuffer(256);
        sb.append(href);

        int i = 0;
        @SuppressWarnings("unchecked")
        Enumeration<String> e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String param = (String) e.nextElement();
            if (!param.equals("currentPage")) {
                String[] values = request.getParameterValues(param);
                for (String value : values) {
                    if (i++ == 0) {
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    try {
                        sb.append(param).append("=").append(URLEncoder.encode(value,"EUC-KR"));
                    } catch (UnsupportedEncodingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        }
        // logger.debug("sb.toString(): {}", sb.toString());
        return sb.toString();
    }

    public class Page {
        private int index = 0;
        private String href = null;

        private Page(int index, String href) {
            this.index = index;
            this.href = href;
        }

        public int getIndex() {
            return index;
        }

        public String getHref() {
            return href;
        }
    }
}