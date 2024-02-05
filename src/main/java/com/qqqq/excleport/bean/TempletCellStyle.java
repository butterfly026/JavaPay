package com.qqqq.excleport.bean;

/**
 * 模板单元格样式对象
 *
 * @author nb
 */
public class TempletCellStyle {

    /**
     * 样式名称
     */
    private String name;

    /**
     * 所在行，索引从0开始
     */
    private Integer row;

    /**
     * 所在列，索引从1开始
     */
    private Integer col;

    public TempletCellStyle() {
    }

    public TempletCellStyle(String name, Integer row, Integer col) {
        super();
        this.name = name;
        this.row = row;
        this.col = col;
    }

    /**
     * @return 样式名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 样式名称
     *
     * @param name 样式名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 所在行，索引从0开始
     */
    public Integer getRow() {
        return row == null ? 0 : row;
    }

    /**
     * 设置 所在行，索引从0开始
     *
     * @param row 所在行，索引从0开始
     */
    public void setRow(Integer row) {
        this.row = row;
    }

    /**
     * @return 所在列，索引从1开始
     */
    public Integer getCol() {
        return col == null ? 0 : col;
    }

    /**
     * 设置 所在列，索引从1开始
     *
     * @param col 所在列，索引从1开始
     */
    public void setCol(Integer col) {
        this.col = col;
    }
}
