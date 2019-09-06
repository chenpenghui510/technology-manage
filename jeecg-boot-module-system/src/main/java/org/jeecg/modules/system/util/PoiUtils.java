package org.jeecg.modules.system.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

/**
 * [POI导出工具类]
 * 
 * @author CaoSuYa
 * @date 2017年3月29日 上午10:08:02
 * @company Gainet
 * @version 1.0
 * @copyright copyright (c) 2017
 */
public class PoiUtils {
	/**
	 * 
	 * [处理导出文件名称乱码的问题] <br>
	 * 
	 * @author CaoSuYa <br>
	 * @date 2017年3月29日 上午10:08:34 <br>
	 * @param request
	 * @param fileNames
	 *            文件名称
	 * @return <br>
	 */
	public static String processFileName(HttpServletRequest request, String fileNames) {
		String codedfilename = null;
		try {
			String agent = request.getHeader("USER-AGENT");
			if(null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie

				String name = java.net.URLEncoder.encode(fileNames, "UTF8");

				codedfilename = name;
			} else if(null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等

				codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codedfilename;
	}

	// 对double值进行四舍五入，保留9位小数的几种方法
	public static String formatDouble9(double d) {
		String str = String.format("%.9f", d);
		return str;
	}

	// public static void exportExecl(HSSFWorkbook workbook, HSSFSheet sheet, int startRow, String tableName,
	// String[] colonumTitles, Map<String, Object> colonumKeys, List<Map<String, Object>> importList) {
	//
	// }

	/**
	 * 
	 * [创建表的表头以及列标题] <br>
	 * 
	 * @author CaoSuYa <br>
	 * @date 2017年12月7日 上午11:02:55 <br>
	 * @param workbook
	 *            表簿
	 * @param sheet
	 *            表格
	 * @param startRow
	 *            开始行数
	 * @param tableName
	 *            表头标题
	 * @param colonumTitles
	 *            列标题 <br>
	 */
	public static void exportExecl(HSSFWorkbook workbook, HSSFSheet sheet, int startRow, String tableName,
			String[] colonumTitles) {
		// 1.2、头标题样式
		HSSFCellStyle style1 = createCellStyle(workbook, (short) 16, true, true);
		// 1.3、列标题样式
		HSSFCellStyle style3 = createCellStyle(workbook, (short) 14, true, true);
		// 1.4、数据样式
		HSSFCellStyle style4 = createCellStyle(workbook, (short) 10, false, true);
		// 1.创建合并单元格对象--
		int endColNum = colonumTitles.length - 1;// 结束列号
		CellRangeAddress cellRangeAddress2 = new CellRangeAddress(startRow, startRow, 0, endColNum);// 起始行号，结束行号，起始列号，结束列号
		// 2.加载合并单元格对象
		sheet.addMergedRegion(cellRangeAddress2);
		// 2.2 加粗合并单元格边框
		RegionUtil.setBorderLeft(2, cellRangeAddress2, sheet, workbook);
		RegionUtil.setBorderBottom(2, cellRangeAddress2, sheet, workbook);
		RegionUtil.setBorderRight(2, cellRangeAddress2, sheet, workbook);
		RegionUtil.setBorderTop(2, cellRangeAddress2, sheet, workbook);
		// 3、创建行
		// 3.1、创建头标题行；并且设置头标题
		HSSFRow row1 = sheet.createRow(startRow);
		row1.setHeightInPoints(28);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellStyle(style1);
		cell1.setCellValue(tableName);// 设置头标题
		// ------------------制作 表格开始-----------------
		// 3.2、创建列标题行；并且设置列标题
		HSSFRow row2 = sheet.createRow(startRow + 1);// 从startRow（开始行数）的第2行开始
		String[] titles = colonumTitles;
		for(int i = 0; i < titles.length; i++) {
			HSSFCell cell2 = row2.createCell(i);
			// 加载单元格样式
			cell2.setCellStyle(style3);
			cell2.setCellValue(titles[i]);
		}
	}

	/**
	 * 创建单元格样式
	 * 
	 * @param workbook
	 *            工作簿
	 * @param fontSize
	 * 
	 * @param isBold
	 *            是否加粗字体
	 * @param isBold
	 *            是否居中
	 * @return 单元格样式
	 */
	public static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontSize, boolean isBold,
			boolean isCenter) {
		HSSFCellStyle style = workbook.createCellStyle();
		if(isCenter) {
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		}
		// 创建字体
		HSSFFont font = workbook.createFont();
		if(isBold) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗字体
		}
		font.setFontHeightInPoints(fontSize);
		font.setFontName("Arial");
		// 加载字
		style.setFont(font);
		// 加粗单元格边框
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		return style;
	}

	// public static void main(String args[]) {
	// double num;
	// Scanner in = new Scanner(System.in);
	// System.out.print("请输入一个浮点数：");
	// num = in.nextDouble();
	// String str = String.format("%.9f", num);
	// // double cnum = Math.ceil(num);
	// // System.out.println("大于" + num + "的最小数：" + cnum);
	// // double fnum = Math.floor(num);
	// // System.out.println("小于" + num + "的最大数：" + fnum);
	// // double rnum = Math.rint(num);
	// // System.out.println(num + "四舍五入得到浮点数：" + rnum);
	// // long lnum = Math.round(num);
	// System.out.println(num + "四舍五入得到长整数：" + str);
	// }
	/**
	 * 
	 * [设置单元格样式] <br>
	 * 
	 * @author CaoSuYa <br>
	 * @date 2017年12月8日 下午2:11:33 <br>
	 * @param workbook
	 *            工作簿
	 * @param fontSize
	 *            文字大小
	 * @param isBold
	 *            是否加粗
	 * @param isCenter
	 *            是否居中
	 * @param addColBlackColor
	 *            添加背景颜色
	 * @param addFontColor
	 *            添加字体颜色
	 * @return <br>
	 */
	public static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontSize, boolean isBold, boolean isCenter,
			boolean addColBlackColor, boolean addFontColor) {
		HSSFCellStyle style = workbook.createCellStyle();
		// 增加背景颜色
		if(addColBlackColor) {
			style.setFillForegroundColor(HSSFColor.LIME.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		// 是否居中
		if(isCenter) {
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		}
		// 创建字体
		HSSFFont font = workbook.createFont();
		if(isBold) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗字体
		}
		if(addFontColor) {
			font.setColor(HSSFColor.RED.index);// 设置字体颜色
		}
		font.setFontHeightInPoints(fontSize);
		font.setFontName("Arial");
		// 加载字
		style.setFont(font);
		// 加粗单元格边框
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		return style;
	}

	/**
	 * [设置合并单元格的边框] <br>
	 * 
	 * @author LiZhangLin<br>
	 * @date 2019年5月22日 上午11:21:55 <br>
	 * @return <br>
	 */
	public static void setBorder(CellRangeAddress cellRangeAddress, Sheet sheet, Workbook wb) {
		RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, wb);
		RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, wb);
		RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, wb);
		RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, wb);
	}
}
