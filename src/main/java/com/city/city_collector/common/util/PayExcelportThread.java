package com.city.city_collector.common.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.pay.entity.PayExcelexport;
import com.city.city_collector.admin.pay.service.PayExcelexportService;
import com.qqqq.excleport.Poi4ExcelExportManager;
import com.qqqq.excleport.common.Constants;

//excel导出线程
public class PayExcelportThread extends Thread {

    PayExcelexportDataService payExcelexportDataService;

    PayExcelexportService payExcelexportService;

    String name;

    String domain;

    String excelCols;

    String staticFolder;

    Map<String, Object> param;
    String[] orders;
    Long userId;
    Integer source;

    /**
     *
     */
    public PayExcelportThread(String name, String domain, String excelCols, String staticFolder, Long userId,
                              PayExcelexportDataService payExcelexportDataService, PayExcelexportService payExcelexportService, Map<String, Object> param, String[] orders, Integer source) {
        this.name = name;
        this.domain = domain;
        this.excelCols = excelCols;
        this.payExcelexportDataService = payExcelexportDataService;
        this.payExcelexportService = payExcelexportService;
        this.staticFolder = staticFolder;
        this.param = param;
        this.orders = orders;
        this.userId = userId;
        this.source = source;
    }

    @Override
    public void run() {
        PayExcelexport payExcelexport = new PayExcelexport();
        payExcelexport.setRemark("");
        try {
            long l1 = System.currentTimeMillis();

            payExcelexport.setName(name);
            payExcelexport.setDealStatus(0);
            payExcelexport.setDownUrl(domain);
            payExcelexport.setRemark("导出数据准备中,请稍候...");
            payExcelexport.setSource(source);
            payExcelexport.setUserId(userId);
            payExcelexportService.addSave(payExcelexport);

            List<Map<String, Object>> queryExportList = payExcelexportDataService.getExprotData(param, orders);

            payExcelexport.setDealStatus(1);
            payExcelexport.setRemark("本次导出共导出" + queryExportList.size() + "条数据,查询数据耗时" + ((System.currentTimeMillis() - l1) / 1000) + "秒");
            payExcelexportService.editSave(payExcelexport);

            Poi4ExcelExportManager pem = Poi4ExcelExportManager.Builder()
                    .fileName(name)
                    .outModel(Constants.MODEL_OUTPUT_EXCEL_LARGE)
                    .excelSaveFolder(staticFolder + "excelExport")
                    .cellPropertys(excelCols)
                    .fileModel(Constants.MODEL_FILE_CREATE_RANDOM);
            String filePath = pem.createExcel(queryExportList);

            String url = domain + filePath.substring(staticFolder.length()).replace("\\", "/");

            File file = new File(filePath);
            DecimalFormat df = new DecimalFormat("######0.00");
            String fileSize = "";
            long fileLen = file.length();
            if (fileLen < 1024L)
                fileSize = fileLen + "B";
            else if (fileLen < 1048576L)
                fileSize = df.format(fileLen / 1024.0D) + "KB";
            else if (fileLen < 1073741824L)
                fileSize = df.format(fileLen / 1024.0D / 1024.0D) + "MB";
            else {
                fileSize = df.format(fileLen / 1024.0D / 1024.0D / 1024.0D) + "GB";
            }

            payExcelexport.setDownUrl(url);
            payExcelexport.setFilePath(filePath);
            payExcelexport.setDealStatus(2);
            payExcelexport.setRemark(payExcelexport.getRemark() + ",总耗时" + ((System.currentTimeMillis() - l1) / 1000) + "秒,生成Excel文件大小约为" + fileSize);
            payExcelexportService.editSave(payExcelexport);

            l1 = System.currentTimeMillis();
            System.out.println("开始清理...");
            queryExportList.clear();
            queryExportList = null;
            System.gc();
            System.out.println("清理完成:" + (System.currentTimeMillis() - l1));
        } catch (Exception e) {
            e.printStackTrace();

            payExcelexport.setDealStatus(-1);
            payExcelexport.setRemark(payExcelexport.getRemark() + "导出异常:" + e.getMessage());
            payExcelexportService.editSave(payExcelexport);
        }

    }

}
