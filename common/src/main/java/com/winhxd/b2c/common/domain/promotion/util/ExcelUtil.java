package com.winhxd.b2c.common.domain.promotion.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

/**
 * <p>
 * Number Operation Utils
 * </p>
 *
 * @author IceWee
 * @see Row
 * @see Cell
 */
public class ExcelUtil {

	private static final ThreadLocal<DataFormatter> excelDataFormatter = new ThreadLocal<DataFormatter>() {
		@Override
		protected DataFormatter initialValue() {
			return new DataFormatter();
		}
	};

	private ExcelUtil() {
		super();
	}

	/**
	 * <p>
	 * Checks excel(POI) {@code row} whether is empty
	 * </p>
	 * 
	 * @param row excel row
	 * @return {@code true} if all cell's is {@code null} or value is empty of
	 *         input row
	 */
	public static boolean isEmptyRow(final Row row) {
		if (row == null) {
			return true;
		}
		int firstCellNum = row.getFirstCellNum();
		int lastCellNum = row.getLastCellNum();
		int cellCount = lastCellNum;
		int emptyCellCount = 0; // 空单元格数量
		for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
			if (isEmptyCell(row.getCell(cellNum))) {
				emptyCellCount++;
			}
		}
		return cellCount == emptyCellCount;
	}

	/**
	 * <p>
	 * Checks excel(POI) {@code cell} whether is empty
	 * </p>
	 * 
	 * @param cell excel cell
	 * @return {@code true} if the cell is null or value is blank
	 */
	public static boolean isEmptyCell(final Cell cell) {
		return StringUtils.isBlank(getCellValue(cell));
	}

	/**
	 * <p>
	 * Get excel(POI) {@code cell}'s String value
	 * </p>
	 * 
	 * @param cell excel cell
	 * @return the value of cell, {@code null} if the cell is null
	 */
	public static String getCellValue(final Cell cell) {
		if (cell != null) {
			return StringUtils.trim(excelDataFormatter.get().formatCellValue(cell));
		}
		return null;
	}

	/**
	 * 
	 * @author yxb
	 * @date  2017年4月13日 下午8:18:58
	 * @Description 导入excel 返回对象列表 ，如果实现了IExcelEntity ，则含有错误信息 errorMsg
	 * @param file
	 * @param pojoClass
	 * @return 
	 */
	public static <T> List<T> importExcel(File file, Class<?> pojoClass) {
		ImportParams params = new ImportParams();
		params.setNeedVerfiy(true);
		return ExcelImportUtil.importExcel(file, pojoClass, params);
	}

	/**
	 * 
	 * @author yxb
	 * @date  2017年4月13日 下午8:19:53
	 * @Description 导入excel 返回对象列表 ，如果实现了IExcelEntity ，则含有错误信息 errorMsg
	 * @param inputStream
	 * @param pojoClass
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> importExcel(InputStream inputStream, Class<?> pojoClass) throws Exception {
		ImportParams params = new ImportParams();
		params.setNeedVerfiy(true);
		return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
	}

	/**
	 * 
	 * @author yxb
	 * @date  2017年4月13日 下午8:20:00
	 * @Description 导入excel 返回校验结果对象
	 * @param file
	 * @param pojoClass
	 * @return
	 * @throws Exception 
	 */
	public static <T> ImportResult<T> importExcelVerify(File file, Class<?> pojoClass) throws Exception {
		FileInputStream inputStream = new FileInputStream(file);
		return importExcelVerify(inputStream, pojoClass);
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

	/**
	 * 
	 * @author yxb
	 * @date  2017年4月13日 下午8:20:24
	 * @Description 导出excel
	 * @param sheetName sheet名称
	 * @param pojoClass
	 * @param dataSet 导出数据集合
	 * @return
	 */
	public static Workbook exportExcel(String sheetName, Class<?> pojoClass, Collection<?> dataSet) {
		ExportParams params = new ExportParams();
		params.setSheetName(sheetName);
		return ExcelExportUtil.exportExcel(params, pojoClass, dataSet);
	}

	// public static void main(String[] args) throws IOException {
	// List list = new ArrayList<>();
	// Workbook workbook = ExcelUtil.exportExcel("门店信息", SalerDTO.class, list);
	//
	//// ExportParams params = new ExportParams();
	// // params.setSheetName("门店信息");
	//// Workbook workbook = ExcelExportUtil.exportExcel(params, SalerDTO.class,
	// list);
	// // 输出Excel文件
	// FileOutputStream output = new FileOutputStream("d:\\workbook.xls");
	// workbook.write(output);
	// output.flush();
	// System.out.println(workbook.toString());
	// }
}
