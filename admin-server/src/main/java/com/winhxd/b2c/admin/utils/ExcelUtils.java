package com.winhxd.b2c.admin.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;

public class ExcelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 导出excel
     *
     * @param list
     * @return
     */

    public static ResponseEntity<byte[]> exp(List list) {

        return exp(list, "data.xls");
    }


    /**
     * 导出excel
     *
     * @param list
     * @return
     */
    public static ResponseEntity<byte[]> exp(List list, String fileName) {

        if (list.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            try {
                return new ResponseEntity<byte[]>("导出数据为空！".getBytes("utf-8"), headers, HttpStatus.OK);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        HttpHeaders headers = new HttpHeaders();
        Class c = (list.get(0)).getClass();
        Workbook w = exportExcel("导出数据", c, list);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            w.write(baos);
            byte[] b = baos.toByteArray();
            fileName = new String(fileName.getBytes("gbk"), "ISO-8859-1");
            headers.setContentDispositionFormData("attachment", fileName + ".xls");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
        } catch (IOException e) {
            LOGGER.error("导出数据错误", e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e1) {
                    LOGGER.error("导出数据错误", e1);

                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    LOGGER.error("导出数据错误", e);
                }
            }
        }

        return null;
    }

    public static ResponseEntity<byte[]> exp(List list, String fileName, String other) {

        if (list.isEmpty()) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        Class c = (list.get(0)).getClass();
        Workbook w = exportExcel("导出数据", c, list/* ,other */);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            w.write(baos);
            byte[] b = baos.toByteArray();
            headers.setContentDispositionFormData("attachment", fileName + ".xls");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
        } catch (IOException e) {
            LOGGER.error("导出数据错误", e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e1) {
                    LOGGER.error("导出数据错误", e1);

                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    LOGGER.error("导出数据错误", e);
                }
            }
        }

        return null;
    }

    /**
     * @param sheetName sheet名称
     * @param pojoClass
     * @param dataSet   导出数据集合
     * @return
     * @author yxb
     * @date 2017年4月13日 下午8:20:24
     * @Description 导出excel
     */
    public static Workbook exportExcel(String sheetName, Class<?> pojoClass, Collection<?> dataSet) {
        ExportParams params = new ExportParams();
        params.setSheetName(sheetName);
        return ExcelExportUtil.exportExcel(params, pojoClass, dataSet);
    }
}
