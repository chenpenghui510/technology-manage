package org.jeecg.modules.system.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ExcelWriter {


	/**
	 * 
	 * [导出表格数据]<br>
	 *
	 * @2017年12月28日下午5:17:17<br>
	 * @param fileName
	 *            文件名 默认为导出日期.xls (例如：2017-12-28.xls)
	 * @param tableName
	 *            表格名 默认没有
	 * @param columnList
	 *            字段名 key为字段，value为字段名
	 * @param dataList
	 *            数据
	 */
	public static void exportExcel(String fileName, String tableName, List<Column> columnList,
								   List<Map<String, Object>> dataList, HttpServletRequest request, HttpServletResponse response) {
		run(fileName, false, tableName, columnList, dataList, false,request,response);
	}

	/*public static void exportExcel(String fileName, String tableName, List<Column> columnList, List<Map<String, Object>> dataList, boolean haveBorder) {
		run(fileName, false, tableName, columnList, dataList, haveBorder);
	}*/

	/**
	 * 
	 * [导出批量导入的模板]<br>
	 *
	 * @2018年1月10日上午10:39:34<br>
	 * @param fileName
	 *            模板文件名
	 * @param prompt
	 *            提示信息
	 * @param columnList
	 *            字段（字段，字段名，是否标红）
	 */
	/*public static void exportTemplate(String fileName, String prompt, List<Column> columnList) {
		run(fileName, true, prompt, columnList, null, false);
	}*/

	/*
	 * 暂定如果有提示信息的话就没有表名了
	 */
	private static void run(String fileName, boolean hasPrompt, String nameOrPrompt, List<Column> columnList,
 List<Map<String, Object>> dataList, boolean haveBorder,HttpServletRequest request,HttpServletResponse response) {
		// 判断文件名
		if(fileName == null || "".equals(fileName.trim())) {
			fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		// 判断表名
		boolean hasTableName = true;
		if(nameOrPrompt == null || "".equals(nameOrPrompt.trim())) {
			hasTableName = false;
		}
		try {
			// 判断与数据列数是否一致
		/*	if(dataList != null && dataList.size() > 0) {
				if(dataList.get(0).keySet().size() < columnList.size()) {
					throw new RuntimeException("数据集字段数小于列数，请认真核实");
				}
			}*/
			// 创建工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 头标题样式
			HSSFCellStyle style1 = createCellStyle(workbook, (short) 20, false, false, true, haveBorder);
			// 列标题样式
			HSSFCellStyle style2 = createCellStyle(workbook, (short) 14, true, false, true, haveBorder);
			// 普通数据样式
			HSSFCellStyle style3 = createCellStyle(workbook, (short) 11, false, false, true, haveBorder);
			// 是否列标题标红
			HSSFCellStyle style4 = createCellStyle(workbook, (short) 14, false, true, true, haveBorder);
			// 标红数据样式
			HSSFCellStyle style5 = createCellStyle(workbook, (short) 11, false, true, true, haveBorder);
			// 提示信息文字样式
			HSSFCellStyle promptStyle = createCellStyle(workbook, (short) 11, false, true, false, haveBorder);

			HSSFSheet sheet = workbook.createSheet();
			// 设置列宽
			for(int i = 0; i < columnList.size(); i++) {
				sheet.setColumnWidth(i, 256 * 25);
			}
			sheet.setDefaultRowHeightInPoints(20);
			// 3、创建行
			// 3.1、创建头标题行；并且设置头标题
			int indexRow = 0;
			if(hasTableName) {
				// 创建合并单元格对象
				CellRangeAddress cellRangeAddress1 = new CellRangeAddress(0, 0, 0, columnList.size() - 1);// 起始行号，结束行号，起始列号，结束列号
				// 加载合并单元格对象
				sheet.addMergedRegion(cellRangeAddress1);
				HSSFRow row1 = sheet.createRow(indexRow);
				row1.setHeightInPoints(28);
				HSSFCell cell1 = row1.createCell(0);
				// 加载单元格样式
				if(hasPrompt) {
					cell1.setCellStyle(promptStyle);
				} else {
					cell1.setCellStyle(style1);
				}
				cell1.setCellValue(nameOrPrompt);
				indexRow += 1;
			}
			// 加粗合并单元格边框
			// RegionUtil.setBorderLeft(1, cellRangeAddress1, sheet, workbook);
			// RegionUtil.setBorderBottom(1, cellRangeAddress1, sheet, workbook);
			// RegionUtil.setBorderRight(1, cellRangeAddress1, sheet, workbook);
			// RegionUtil.setBorderTop(1, cellRangeAddress1, sheet, workbook);

			// 3.2、创建列标题行；并且设置列标题
			HSSFRow row2 = sheet.createRow(indexRow);
			for(int i = 0; i < columnList.size(); i++) {
				HSSFCell cell2 = row2.createCell(i);
				Column column = columnList.get(i);
				String fieldName = column.getFieldName();
				// 加载单元格样式
				if(column.isFontRed()) {
					cell2.setCellStyle(style4);
				} else {
					cell2.setCellStyle(style2);
				}
				// 设置值
				cell2.setCellValue(fieldName);
			}
			indexRow += 1;
			// 4、操作单元格；将列表写入excel
			if(dataList != null) {
				for(int j = 0; j < dataList.size(); j++) {
					HSSFRow row = sheet.createRow(indexRow);
					for(int i = 0; i < columnList.size(); i++) {
						Column column = columnList.get(i);
						String field = column.getField();
						boolean fontRed = column.isFontRed();
						Object value = dataList.get(j).get(field);
						String cellValue = "";
						if(value != null) {
							cellValue = value.toString();
						}
						HSSFCell cell12 = row.createCell(i);
						if(fontRed) {
							cell12.setCellStyle(style5);
						} else {
							cell12.setCellStyle(style3);
						}
						cell12.setCellValue(cellValue);
					}
					indexRow += 1;
				}
			}
			// 设置表名
			// 解决表名的乱码问题
			if(fileName.contains(".")) {
				fileName = PoiUtils.processFileName( request,
						fileName.substring(0, fileName.indexOf(".")) + ".xls");
			} else {
				fileName = PoiUtils.processFileName( request, fileName + ".xls");
			}
			System.out.println(fileName);
			//HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			/*response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;filename="+fileName);*/

			// 输出流
			ServletOutputStream outputStream = response.getOutputStream();
			// 5、输出
			workbook.write(outputStream);
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException("导出表格异常：" + e.getMessage());
		}
	}

	/**
	 * 
	 * [创建单元格样式]<br>
	 *
	 * @2018年1月10日上午11:53:04<br>
	 * @param workbook
	 * @param fontSize
	 * @param isBold
	 * @param fontColour
	 * @param alignCenter
	 * @return
	 */
	private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontSize, boolean isBold, boolean fontColour, boolean alignCenter,
			boolean haveBorder) {
		HSSFCellStyle style = workbook.createCellStyle();
		// 居中
		if(alignCenter) {
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		} else {
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平居左
		}
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		// 创建字体
		HSSFFont font = workbook.createFont();
		// 加粗
		if(isBold) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗字体
		}
		// 大小
		font.setFontHeightInPoints(fontSize);
		// 标红
		if(fontColour) {
			font.setColor(HSSFFont.COLOR_RED);
		}
		// 加载字体
		style.setFont(font);
		// 加粗单元格边框
		// style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		// style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		// style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		// style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		if(haveBorder) {
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		}
		return style;
	}

}
