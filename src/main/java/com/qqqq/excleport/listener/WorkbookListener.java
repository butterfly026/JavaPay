package com.qqqq.excleport.listener;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * poi工作簿的监听器
 *
 * @author nb
 */
public interface WorkbookListener {

    /***
     * 添加单元格样式，此方法在创建workbook对象时调用
     * @param workbook 工作簿对象
     * @param cellStyles 单元格样式集合，样式put到这个集合中即可
     */
    public void addCellStyle(Workbook workbook, Map<String, CellStyle> cellStyles);

    /**
     * 自定义标题单元格处理，此方法在创建并合并完成单元格后触发，可用来设置合并单元格样式
     *
     * @param sheet  Sheet对象
     * @param cell   单元格对象
     * @param left   单元格横向起始位置索引
     * @param right  单元格横向结束位置索引
     * @param top    单元格纵向起始位置索引
     * @param bottom 单元格纵向结束位置索引
     */
    public void updateTitleCell(Sheet sheet, Cell cell, int left, int right, int top, int bottom);

    /***
     * 工作簿创建完成时调用此方法，自定义处理。
     * 此方法执行完成后程序才会生成excel文件到指定路径，返回true表示自定义处理完成，终止程序(不需要工具类生成excel文件)，false表示后续操作继续进行
     * @param workbook 工作簿对象
     * @return true/false
     */
    public boolean workbookComplete(Workbook workbook);

    /**
     * 自定义生成的excel文件名，仅在多文件模式有效
     *
     * @param index 值为 当前创建的excel文件个数-1
     * @return excel文件名
     */
    public String setFileName(int index);

    /**
     * 自定义生成sheet名，仅在多sheet模式有效
     *
     * @param index 值为 当前创建的sheet个数-1
     * @return sheet名
     */
    public String setSheetName(int index);

}
