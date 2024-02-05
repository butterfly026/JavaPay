package com.qqqq.excleport.bean;

import java.util.List;

import com.qqqq.excleport.common.ExcelUtil;

/**
 * excel模板对象，用于按照模板导出文件以及从模板读取单元格样式
 *
 * @author nb
 */
public class TempletExcel {
    /**
     * 模板文件路径
     */
    private String templetFilePath;

    /**
     * 是否根据模板导出,默认为false
     */
    private boolean exportModel = false;

    /**
     * 模板单元格样式对象集合
     */
    private List<TempletCellStyle> templetCellStyles;

    /**
     * 写入数据的起始行索引，设置根据模板导出为true时生效，不设置自动获取
     */
    private Integer startRowIndex;

    public TempletExcel() {
    }

    /**
     * 构造方法
     *
     * @param templetFilePath 模板路径
     */
    public TempletExcel(String templetFilePath) {
        super();
        this.templetFilePath = templetFilePath;
    }

    /**
     * 构造方法
     *
     * @param templetFilePath 模板路径
     * @param exportModel     是否按照模板导出
     */
    public TempletExcel(String templetFilePath, boolean exportModel) {
        super();
        this.templetFilePath = templetFilePath;
        this.exportModel = exportModel;
    }

    /**
     * 构造方法
     *
     * @param templetFilePath   模板路径
     * @param exportModel       是否按照模板导出
     * @param templetCellStyles 模板单元格样式
     */
    public TempletExcel(String templetFilePath, boolean exportModel, List<TempletCellStyle> templetCellStyles) {
        super();
        this.templetFilePath = templetFilePath;
        this.exportModel = exportModel;
        this.templetCellStyles = templetCellStyles;
    }

    /**
     * 构造方法
     *
     * @param templetFilePath 模板路径
     * @param exportModel     是否按照模板导出
     * @param json            模板单元格样式json字符串
     */
    public TempletExcel(String templetFilePath, boolean exportModel, String json) {
        super();
        this.templetFilePath = templetFilePath;
        this.exportModel = exportModel;
        this.templetCellStyles = ExcelUtil.jsonToCellStyles(json);
    }

    /**
     * 构造方法
     *
     * @param templetFilePath   模板路径
     * @param exportModel       是否按照模板导出
     * @param startRowIndex     写入数据的起始行索引，设置根据模板导出为true时生效，不设置自动获取
     * @param templetCellStyles 模板单元格样式
     */
    public TempletExcel(String templetFilePath, boolean exportModel, Integer startRowIndex, List<TempletCellStyle> templetCellStyles) {
        super();
        this.templetFilePath = templetFilePath;
        this.exportModel = exportModel;
        this.startRowIndex = startRowIndex;
        this.templetCellStyles = templetCellStyles;
    }

    /**
     * 构造方法
     *
     * @param templetFilePath 模板路径
     * @param exportModel     是否按照模板导出
     * @param startRowIndex   写入数据的起始行索引，设置根据模板导出为true时生效，不设置自动获取
     * @param json            模板单元格样式json字符串
     */
    public TempletExcel(String templetFilePath, boolean exportModel, Integer startRowIndex, String json) {
        super();
        this.templetFilePath = templetFilePath;
        this.exportModel = exportModel;
        this.startRowIndex = startRowIndex;
        this.templetCellStyles = ExcelUtil.jsonToCellStyles(json);
    }

    /**
     * @return 模板文件路径
     */
    public String getTempletFilePath() {
        return templetFilePath;
    }

    /**
     * 设置 模板文件路径
     *
     * @param templetFilePath 模板文件路径
     */
    public void setTempletFilePath(String templetFilePath) {
        this.templetFilePath = templetFilePath;
    }

    /**
     * @return 模板单元格样式对象集合
     */
    public List<TempletCellStyle> getTempletCellStyles() {
        return templetCellStyles;
    }

    /**
     * 设置 模板单元格样式对象集合
     *
     * @param templetCellStyles 模板单元格样式对象集合
     */
    public void setTempletCellStyles(List<TempletCellStyle> templetCellStyles) {
        this.templetCellStyles = templetCellStyles;
    }

    /**
     * 通过json字符串初始化单元格样式对象，偷懒用。
     *
     * @param json 格式 [{name:'test',row:1,col:1}...]
     */
    public void setTempletCellStyles(String json) {
        this.templetCellStyles = ExcelUtil.jsonToCellStyles(json);
    }

    /**
     * @return 根据模板导出
     */
    public boolean isExportModel() {
        return exportModel;
    }

    /**
     * 设置 根据模板导出
     *
     * @param exportModel 根据模板导出
     */
    public void setExportModel(boolean exportModel) {
        this.exportModel = exportModel;
    }

    /**
     * @return 写入数据的起始行索引，设置根据模板导出为true时生效，不设置自动获取
     */
    public Integer getStartRowIndex() {
        return startRowIndex;
    }

    /**
     * 设置 写入数据的起始行索引，设置根据模板导出为true时生效，不设置自动获取
     *
     * @param startRowIndex 起始行索引，设置根据模板导出为true时生效，不设置自动获取
     */
    public void setStartRowIndex(Integer startRowIndex) {
        this.startRowIndex = startRowIndex;
    }
}
