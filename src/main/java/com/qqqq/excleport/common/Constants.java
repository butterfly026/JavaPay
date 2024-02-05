package com.qqqq.excleport.common;

/**
 * 常量类
 *
 * @author nb
 */
public class Constants {

    /**
     * 默认的标题单元格样式名
     */
    public final static String DEFAULT_TITLE_CELLSTYLE_NAME = "defTitleCellStyle";

    /**
     * 默认的数据单元格样式名
     */
    public final static String DEFAULT_DATA_CELLSTYLE_NAME = "defDataCellStyle";

    /**
     * excel07-2003(xls)一个sheet最多65536行
     */
    public final static int SHEET_MAX_SIZE_HSSF = 65536;

    /**
     * excel后续版本(xlsx)一个sheet最多1048576行
     */
    public final static int SHEET_MAX_SIZE_XSSF = 1048576;

    /**
     * 超出一个sheet允许的最大行数时采取的模式：创建新sheet
     */
    public final static int ROW_OVERFLOW_MODEL_NEWSHEET = 1;

    /**
     * 超出一个sheet允许的最大行数时采取的模式：创建新excel文件,生成压缩包
     */
    public final static int ROW_OVERFLOW_MODEL_NEWFILE = 2;

    /**
     * excel导出类型为xls
     */
    public final static int EXCEL_TYPE_HSSF = 1;

    /**
     * excel导出类型为xlsx
     */
    public final static int EXCEL_TYPE_XSSF = 2;

    /**
     * 多excel导出模式的默认文件名
     */
    public final static String DEFAULT_MODEL_NEWFILE_FILENAME = "excel";

    /**
     * Excel导出模式：普通模式，支持xls,xlsx，适用于数据量不大的文件
     */
    public final static int MODEL_OUTPUT_EXCEL_NORMAL = 10000;

    /**
     * Excel导出模式：大文件模式，节约内存，只支持xlsx，适用于数据量非常大的文件。使用此模式时，数据应当分批处理。
     */
    public final static int MODEL_OUTPUT_EXCEL_LARGE = 10001;

    /**
     * 文件生成模式，普通模式，直接生成指定文件
     */
    public final static int MODEL_FILE_CREATE_NORMAL = 10002;

    /**
     * 文件生成模式，随机目录模式，会在指定目录下按时间生成一个随机目录
     */
    public final static int MODEL_FILE_CREATE_RANDOM = 10003;

}
