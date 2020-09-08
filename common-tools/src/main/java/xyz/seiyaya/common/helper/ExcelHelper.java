package xyz.seiyaya.common.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author seiyaya
 * @date 2019/8/8 19:59
 */
@Slf4j
public class ExcelHelper {


    public static DBParam readExcel(File file) {
        return readExcel(file, null, null, false, null);
    }

    public static DBParam readExcel(File file, String[] fieldsName) {
        return readExcel(file, null, fieldsName, false, null);
    }

    public static DBParam readExcel(File file, String[] fieldsName, boolean hasTitle) {
        return readExcel(file, null, fieldsName, hasTitle, null);
    }

    public static DBParam readExcel(File file, int[] columnNum, String[] fieldsName, boolean hasTitle) {
        return readExcel(file, columnNum, fieldsName, hasTitle, null);
    }

    /**
     * 读取excel
     *
     * @param file
     * @param columnNum
     * @param fieldsName
     * @param hasTitle
     * @param defaultValue
     * @return
     */
    public static DBParam readExcel(File file, int[] columnNum, String[] fieldsName, boolean hasTitle, DBParam defaultValue) {
        if (file.isFile()) {
            DBParam result = null;
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                result = readExcel(fileInputStream, columnNum, fieldsName, hasTitle, defaultValue);
            } catch (Exception e) {
                log.error("解析文件异常:", e);
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (Exception e) {
                    log.error("释放资源异常:", e);
                }
            }
            return result;
        } else {
            log.error("需要解析的不是文件:{}", file.getAbsoluteFile());
        }
        return null;
    }

    public static DBParam readExcel(FileInputStream fileInputStream, int[] columnNum,
                                    String[] fieldsName, boolean hasTitle,
                                    DBParam defaultValue) throws Exception {
        DBParam sheetParam = new DBParam();
        Workbook wb = createWorkbook(fileInputStream);
        //获取excel的页数
        int sheetNum = wb.getNumberOfSheets();
        for (int i = 0; i < sheetNum; i++) {
            List<DBParam> list = new ArrayList<>();
            Sheet sheet = wb.getSheetAt(i);
            //获取一页的行数
            int rowNum = sheet.getLastRowNum();
            for (int j = 0; j < rowNum; j++) {
                if (hasTitle && j == 0) {
                    //不将标题头加入到结果集
                    continue;
                }
                DBParam sheetMap = new DBParam();
                Row row = sheet.getRow(j);
                if (row != null) {
                    //获取列的长度
                    int cellNum = row.getLastCellNum();
                    if (columnNum != null && columnNum.length > 0) {
                        for (int k = 0; k < columnNum.length; k++) {
                            Cell cell = row.getCell(columnNum[k]);
                            if (cell != null) {
                                String key = (fieldsName != null && fieldsName.length >= k) ? fieldsName[k] : String.valueOf(k);
                                sheetMap.put(key, getCellValue(cell, key, defaultValue));
                            }
                        }
                    } else {
                        //通过顺序取值
                        for (int k = 0; k < cellNum; k++) {
                            Cell cell = row.getCell(k);
                            if (cell != null) {
                                String key = (fieldsName != null && fieldsName.length >= k) ? fieldsName[k] : String.valueOf(k);
                                sheetMap.put(key, getCellValue(cell, key, defaultValue));
                            }
                        }
                    }
                }
                list.add(sheetMap);
            }
            sheetParam.put(sheet.getSheetName(), list);
        }
        return sheetParam;
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @param key
     * @param defaultValue
     * @return
     */
    private static String getCellValue(Cell cell, String key, DBParam defaultValue) {
        String value = cell.toString();
        if (StringHelper.isEmpty(value) && defaultValue != null && !defaultValue.isEmpty()) {
            //列为空  则添加默认值
            value = defaultValue.getString(key);
        }
        return value;
    }

    /**
     * 读取excel到list中
     *
     * @param file
     * @return
     */
    public static List<List<String>> readExcelToList(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Workbook workbook = createWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int start = sheet.getFirstRowNum();
            int end = sheet.getLastRowNum();
            List<List<String>> sheetList = new ArrayList<>();
            //读取行
            for (int i = start; i <= end; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                short startCell = row.getFirstCellNum();
                short endCell = row.getLastCellNum();
                List<String> rowList = new ArrayList<>();
                //读取列
                for (int j = startCell; j < endCell; j++) {
                    Cell cell = row.getCell(j);
                    rowList.add(getCellValue(cell));
                }
                sheetList.add(rowList);
            }
            return sheetList;
        } catch (Exception e) {
            log.error("解析excel出错:", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭流出错:", e);
                }
            }
        }
        return null;
    }

    private static String getCellValue(Cell cell) {
        String value = null;
        if (cell != null) {
            int type = cell.getCellType();
            switch (type) {
                case Cell.CELL_TYPE_NUMERIC:
                    value = new DataFormatter().formatCellValue(cell);
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString();
                    break;
            }
        }
        return value;
    }

    private static Workbook createWorkbook(InputStream inputStream) throws Exception {
        if (!inputStream.markSupported()) {
            inputStream = new PushbackInputStream(inputStream, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(inputStream)) {
            return new HSSFWorkbook(inputStream);
        } else if (POIXMLDocument.hasOOXMLHeader(inputStream)) {
            return new XSSFWorkbook(OPCPackage.open(inputStream));
        }

        throw new IllegalArgumentException("该excel版本目前poi解析不了");
    }

    public static HSSFWorkbook createExcel(List<Object> list, String[] titles, String[] mappingFileds, int[] columnWidth,
                                           boolean hasIndex) {
        return createExcel(null, list, titles, mappingFileds, columnWidth, null, null, null, null, null, null, hasIndex);
    }

    public static HSSFWorkbook createExcel(HSSFWorkbook workbook, List list, String[] titles, String[] mappingFileds,
                                           int[] columnWidth, HSSFCellStyle titleStyle, int[] rows, HSSFCellStyle[] rowsStyle, int[] column,
                                           HSSFCellStyle[] columnsStyle, HSSFCellStyle otherStyle, boolean hasIndex) {
        if (list.isEmpty()) {
            return null;
        } else {
            if (hasIndex) {
                titles = arrayInsert(titles, 0, "序号");
                columnWidth = arrayInsert(columnWidth, 0, 50);
            }

            if (workbook == null) {
                workbook = new HSSFWorkbook();
            }

            HSSFSheet sheet = workbook.createSheet();
            int rowNum = 0;
            if (titles != null && titles.length > 0) {
                HSSFRow row = sheet.createRow(rowNum);
                if (ArrayUtils.contains(titles, ":")) {
                    for (int i = 0; i < titles.length; i++) {
                        if (columnWidth != null && columnWidth.length > 0 && i < columnWidth.length) {
                            sheet.setColumnWidth(i, columnWidth[i]);
                        }

                        String title = titles[i];
                        if (title.contains(":")) {
                            String[] groupTitles = title.split(":");
                            if (groupTitles.length != 2) {
                                log.error("标题格式错误");
                                return null;
                            }

                            String pTitle = groupTitles[0];
                            String cTitle = groupTitles[1];
                            if (cTitle.contains(",")) {
                                rowNum++;
                                String[] cTitles = cTitle.split(",");
                                HSSFCell cell = row.createCell(i);
                                cell.setCellValue(pTitle);

                                HSSFRow row2 = sheet.createRow(rowNum);
                                for (int j = 0; j < cTitles.length; j++) {
                                    HSSFCell ccell = row2.createCell(i);
                                    ccell.setCellValue(pTitle);
                                    setTitleStyle(workbook, ccell, titleStyle);
                                    workbook.setRepeatingRowsAndColumns(workbook.getSheetIndex(sheet), 0, 0, i, i + j + 1);
                                }
                            } else {
                                log.error("标题格式错误");
                                return null;
                            }
                        } else {
                            HSSFCell cell = row.createCell(i);
                            workbook.setRepeatingRowsAndColumns(workbook.getSheetIndex(sheet), i, i, 0, 1);
                            cell.setCellValue(title);
                            setTitleStyle(workbook, cell, titleStyle);
                        }
                    }
                } else {
                    for (int i = 0; i < titles.length; i++) {
                        String title = titles[i];
                        HSSFCell cell = row.createCell(i);
                        cell.setCellValue(title);
                        setTitleStyle(workbook, cell, titleStyle);
                    }
                }
                rowNum++;
            }
            //生成数据
            for (Iterator<DBParam> it = list.iterator(); it.hasNext(); ) {
                DBParam data = it.next();
                //创建行
                HSSFRow row = sheet.createRow(rowNum);

                if (hasIndex) {
                    HSSFCell cell = row.createCell(0);
                    int cellValue = rowNum;
                    if (ArrayUtils.contains(titles, ":")) {
                        cellValue = rowNum - 1;
                    }
                    cell.setCellValue(cellValue);
                    HSSFCellStyle cellStyle = workbook.createCellStyle();// 建立新的cell样式
                    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    cell.setCellStyle(cellStyle);
                }
                for (int k = 0; k < mappingFileds.length; k++) {
                    HSSFCell cell;
                    if (hasIndex) {
                        cell = row.createCell(k + 1);
                    } else {
                        cell = row.createCell(k);
                    }
                    Object obj = data.get(mappingFileds[k]);
                    if (obj instanceof BigDecimal) {
                        cell.setCellValue(data.getDouble(mappingFileds[k]));
                    } else {
                        cell.setCellValue(data.getString(mappingFileds[k]));
                    }
                    //设置其它样式
                    if (otherStyle != null) {

                    }
                }
                rowNum++;
            }
            //设置行样式
            if (rows != null) {
                if (rowsStyle != null) {

                }
            }

            //设置列样式
            if (columnsStyle != null) {
            }
            return workbook;
        }
    }

    private static void setTitleStyle(HSSFWorkbook workbook, HSSFCell cell, HSSFCellStyle titleStyle) {
        if (titleStyle == null) {
            HSSFCellStyle cellStyle = workbook.createCellStyle();// 建立新的cell样式
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFFont bFont = workbook.createFont();
            bFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            cellStyle.setFont(bFont);
            cell.setCellStyle(cellStyle);
        } else {
            cell.setCellStyle(titleStyle);
        }
    }

    private static int[] arrayInsert(int[] columnWidth, int index, int width) {
        if (columnWidth == null) {
            return null;
        }
        return ArrayUtils.insert(index, columnWidth, width);
    }

    private static String[] arrayInsert(String[] titles, int index, String str) {
        if (titles == null) {
            return null;
        }
        return ArrayUtils.insert(index, titles, str);
    }

    /**
     * 创建excel文件
     *
     * @param workbook
     * @param fileName
     */
    public static void createFile(HSSFWorkbook workbook, String fileName) {
        if (StringHelper.isNotEmpty(fileName) && !fileName.endsWith(".xls")) {
            fileName += ".xls";
        }
        File file = new File(fileName);
        try {
            boolean newFile = file.createNewFile();
            if (newFile) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                workbook.write(fileOutputStream);

                fileOutputStream.close();
                fileOutputStream.flush();
            }
        } catch (IOException e) {
            log.error("生成excel出错", e);
        }
    }

}
