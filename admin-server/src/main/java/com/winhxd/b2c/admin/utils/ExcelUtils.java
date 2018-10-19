package com.winhxd.b2c.admin.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.promotion.util.BaseExcelDomain;
import com.winhxd.b2c.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
                LOGGER.error("导出数据错误",e);
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


    /**
     *
     * @author yxb
     * @date  2017年4月13日 下午8:20:48
     * @Description 导入excel 返回校验结果对象
     * @param inputStream
     * @param pojoClass
     * @return
     * @throws Exception
     */
    public static <T> ImportResult<T> importExcelVerify(InputStream inputStream, Class<?> pojoClass) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerfiy(true);
        ExcelImportResult<T> result = ExcelImportUtil.importExcelMore(inputStream, pojoClass, params);

        ImportResult<T> res = new ImportResult<>();
        List<T> list = result.getList();
        BaseExcelDomain baseExcelDomain;
        StringBuilder errorMsg = new StringBuilder();
        int errorCount = 0;// 显示错误的数量
        for (int i = 0; i < list.size(); i++) {
            baseExcelDomain = (BaseExcelDomain) list.get(i);
            if (StringUtils.isNotBlank(baseExcelDomain.getErrorMsg())) {
                errorMsg.append((i + 1)).append("行：").append(baseExcelDomain.getErrorMsg()).append("\n");
                res.setErrorMsg(errorMsg.toString());
                errorCount++;
                if (errorCount == 5) {// 显示错误的数量
                    break;
                }
            }
        }
        res.setList(list);
        res.setVerfiyFail(result.isVerfiyFail());
        res.setWorkbook(result.getWorkbook());
        return res;
    }
}
