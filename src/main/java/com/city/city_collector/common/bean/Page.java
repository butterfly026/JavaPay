package com.city.city_collector.common.bean;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Page implements Serializable {
    private static final long serialVersionUID = 2443500191815163353L;

    public static final int DEFAULT_PAGE_NUMBER = 1;

    public static final int DEFAULT_PAGE_SIZE = 20;

    private static final int MAX_PAGE_SIZE = 100000;

    private Integer currentPage = Integer.valueOf(1);

    private Integer pageSize = Integer.valueOf(20);

    private long total = 0L;

    private int pageCount = 0;

    private List<Map<String, Object>> results;

    public Page() {
    }

    public Page(Integer currentPage, Integer pageSize) {
        if ((currentPage == null) || (currentPage.intValue() <= 0))
            this.currentPage = Integer.valueOf(1);
        else {
            this.currentPage = currentPage;
        }

        if ((pageSize == null) || (pageSize.intValue() <= 0) || (pageSize.intValue() > 100000))
            this.pageSize = Integer.valueOf(20);
        else
            this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        if ((currentPage == null) || (currentPage.intValue() <= 0))
            this.currentPage = Integer.valueOf(1);
        else
            this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if ((pageSize == null) || (pageSize.intValue() <= 0) || (pageSize.intValue() > 100000))
            this.pageSize = Integer.valueOf(20);
        else
            this.pageSize = pageSize;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
        this.pageCount = (int) (total % this.pageSize.intValue() == 0L ? total / this.pageSize.intValue()
                : total / this.pageSize.intValue() + 1L);
        if ((this.pageCount > 0) && (this.currentPage.intValue() > this.pageCount))
            this.currentPage = Integer.valueOf(this.pageCount);
    }

    public List<Map<String, Object>> getResults() {
        return this.results;
    }

    public void setResults(List<Map<String, Object>> results) {
        if (results != null && !results.isEmpty()) {
            for (Map map : results) {
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (entry.getValue() != null)
                        if ((entry.getValue() instanceof Timestamp)) {
                            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                map.put(entry.getKey(), sdf.format(entry.getValue()));
                            } catch (Exception localException) {
                            }
                        } else if ((entry.getValue() instanceof Date)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                map.put(entry.getKey(), sdf.format(entry.getValue()));
                            } catch (Exception localException1) {
                            }
                        }
                }
            }
        }
        this.results = results;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void resetcurrentPage() {
        if (this.currentPage.intValue() > this.pageCount)
            this.currentPage = Integer.valueOf(this.pageCount);
    }

    public int getStartRow() {
        return (this.currentPage.intValue() - 1) * this.pageSize.intValue();
    }

    public int getEndRow() {
        return this.currentPage.intValue() * this.pageSize.intValue();
    }

    public boolean isLastPage() {
        return this.currentPage.intValue() == this.pageCount;
    }
}
