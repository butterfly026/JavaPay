package com.qqqq.excleport.listener;

import org.apache.poi.ss.usermodel.Sheet;

public interface SheetListener {

    /**
     * 向sheet添加数据，用于支持数据分页查询导出，不需要骚操作将此方法返回值设置为-1即可。每次处理一个sheet
     *
     * @param sheet    sheet对象
     * @param rowIndex 数据插入行索引
     * @param totalRow sheet最大允许多少行数据
     * @return 返回值 >> -1：执行此方法后，继续执行程序自带的添加数据的方法，其他值跳过自带方法。  0：数据全部处理完   1：数据未处理完
     */
    public int addDataToSheet(Sheet sheet, int rowIndex, int totalRow);
}
