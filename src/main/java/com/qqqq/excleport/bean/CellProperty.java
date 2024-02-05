package com.qqqq.excleport.bean;

/**
 * @author nb
 */
public class CellProperty {

    /**
     * 字段名
     */
    private String field;

    /**
     * 标题
     */
    private String title;

    /**
     * 单元格宽度
     */
    private Integer width;

    /**
     * 列数：单元格跨多少列
     */
    private Integer colspan;

    /**
     * 行数：单元格跨多少行
     */
    private Integer rowspan;

    /**
     * 做为表头时的样式
     */
    private String titleStyle;

    /**
     * 做为单元格时的样式,仅对设置了field值的对象有效
     */
    private String cellStyle;

    /**
     * 批注
     */
    private String comment;

    /**
     * @return 批注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置 批注
     *
     * @param comment 批注
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return 字段名
     */
    public String getField() {
        return field;
    }

    /**
     * 设置 字段名
     *
     * @param field 字段名
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置 标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return 单元格宽度
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * 设置 单元格宽度
     *
     * @param width 单元格宽度
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return 列数：单元格跨多少列
     */
    public Integer getColspan() {
        return colspan;
    }

    /**
     * 设置 列数：单元格跨多少列
     *
     * @param colspan 列数：单元格跨多少列
     */
    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    /**
     * @return 行数：单元格跨多少行
     */
    public Integer getRowspan() {
        return rowspan;
    }

    /**
     * 设置 行数：单元格跨多少行
     *
     * @param rowspan 行数：单元格跨多少行
     */
    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

    /**
     * @return 做为表头时的样式
     */
    public String getTitleStyle() {
        return titleStyle;
    }

    /**
     * 设置 做为表头时的样式
     *
     * @param titleStyle 做为表头时的样式
     */
    public void setTitleStyle(String titleStyle) {
        this.titleStyle = titleStyle;
    }

    /**
     * @return 做为单元格时的样式仅对设置了field值的对象有效
     */
    public String getCellStyle() {
        return cellStyle;
    }

    /**
     * 设置 做为单元格时的样式仅对设置了field值的对象有效
     *
     * @param cellStyle 做为单元格时的样式仅对设置了field值的对象有效
     */
    public void setCellStyle(String cellStyle) {
        this.cellStyle = cellStyle;
    }
}
