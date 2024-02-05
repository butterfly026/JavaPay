package com.qqqq.excleport.bean;

public class CellField {
    /**
     * 字段名
     */
    private String field;
    /**
     * 单元格样式
     */
    private String cellStyle;

    public CellField(String field, String cellStyle) {
        super();
        this.field = field;
        this.cellStyle = cellStyle;
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
     * @return 单元格样式
     */
    public String getCellStyle() {
        return cellStyle;
    }

    /**
     * 设置 单元格样式
     *
     * @param cellStyle 单元格样式
     */
    public void setCellStyle(String cellStyle) {
        this.cellStyle = cellStyle;
    }
}
