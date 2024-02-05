package com.qqqq.excleport.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qqqq.excleport.bean.CellProperty;
import com.qqqq.excleport.bean.TempletCellStyle;

/**
 * 工具类
 *
 * @author nb
 */
public class ExcelUtil {

    /**
     * 将json字符串转换为单元格对象
     *
     * @param json json字符串格式:[[{},{}...]...]
     * @return
     */
    public static List<CellProperty[]> jsonToListData(String json) {
//		JSONArray jsonArray=JSONArray.fromObject(json);
//		List<CellProperty[]> prosList=new ArrayList<CellProperty[]>();
//		for(int i=0;i<jsonArray.size();i++){
//			JSONArray array1=jsonArray.getJSONArray(i);
//			CellProperty[] cellPros=(CellProperty[])JSONArray.toArray(array1, CellProperty.class);
//			prosList.add(cellPros);
//		}
//		return prosList;

        //gson请注释上面的代码，换成下面的
        return new Gson().fromJson(json, new TypeToken<List<CellProperty[]>>() {
        }.getType());
    }

    /**
     * 通过json字符串初始化单元格样式对象
     *
     * @param json 格式 [{name:'test',row:1,col:1}...]
     * @return
     */
    public static List<TempletCellStyle> jsonToCellStyles(String json) {
//		JSONArray jsonArray=JSONArray.fromObject(json);
//		return JSONArray.toList(jsonArray, TempletCellStyle.class);

        //gson请注释上面的代码，换成下面的
        return new Gson().fromJson(json, new TypeToken<List<TempletCellStyle>>() {
        }.getType());
    }

    /***
     * 获取默认标题单元格样式
     * @param workbook
     * @return
     */
    public static CellStyle getDefaultTitleCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
//		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
//		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
//		cellStyle.setBorderRight(CellStyle.BORDER_THIN);

        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /***
     * 获取默认数据单元格样式
     * @param workbook
     * @return
     */
    public static CellStyle getDefaultDataCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    /**
     * 获取目录名
     *
     * @return
     */
    public static String getFolderName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmssS");
        return sdf.format(new Date()) + "-" + UUID.randomUUID().toString().replace("-", "") + sdf1.format(new Date());
    }

    /**
     * 获取Excel类型
     *
     * @param fileName  文件名，返回xls类型
     * @param excelType 预设置的excelType
     * @return 如果预设置的excelType为-1，根据文件后缀名自动获取，类型无法识别或文件名为空时，返回xls类型
     */
    public static int getExcelType(String fileName, int excelType) {
        if (excelType == -1) {
            if (StringUtils.isBlank(fileName)) {
                return Constants.EXCEL_TYPE_HSSF;
            }
            if (fileName.toLowerCase().endsWith(".xlsx")) {
                excelType = Constants.EXCEL_TYPE_XSSF;
            } else {
                excelType = Constants.EXCEL_TYPE_HSSF;
            }
        }
        if (excelType != Constants.EXCEL_TYPE_XSSF && excelType != Constants.EXCEL_TYPE_HSSF) {
            excelType = Constants.EXCEL_TYPE_HSSF;
        }
        return excelType;
    }

    /**
     * 根据文件名获取Workbook对象
     *
     * @param fileName
     * @return
     */
    public static Workbook getWorkbook(String fileName) {
        int excelType = getExcelType(fileName, -1);
        Workbook workbook = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
            if (excelType == Constants.EXCEL_TYPE_HSSF) {
                workbook = new HSSFWorkbook(fis);
            } else {
                workbook = new XSSFWorkbook(fis);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return workbook;
    }
}
