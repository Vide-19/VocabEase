package com.javastudy.vocabease_common.utils;

import com.javastudy.vocabease_common.exception.BusinessException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * excel工具类，处理文件导入导出
 */
public class ExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static List<List<String>> readExcel(MultipartFile file, String[] title, Integer startRowIndex) {
        InputStream is = null;
        int rowIndex = 0;
        try {
            is = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(is);
            List<List<String>> listData = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null)
                throw new BusinessException("excel文件解析错误");
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                rowIndex = i;
                if (i < startRowIndex)
                    continue;
                List<String> rowData = new ArrayList<>();
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                int maxColCount = title.length;
                boolean isAllEmpty = true;
                if (row.getLastCellNum() < maxColCount)
                    throw new BusinessException("数据错误，请重新按照模板上传数据");
                for (int j = 0; j < maxColCount; j++) {
                    Cell cell = row.getCell(j);
                    String cellValue = getCellValue(cell);
                    rowData.add(cellValue);
                    if (!StringTools.isEmpty(cellValue))
                        isAllEmpty = false;
                }
                if (i == startRowIndex) {
                    String dataTitle = rowData.stream().collect(Collectors.joining("_"));
                    String titleStr = Arrays.asList(title).stream().collect(Collectors.joining("_"));
                    if (!dataTitle.equalsIgnoreCase(titleStr))
                        throw new BusinessException("数据错误，请重新按照模板上传数据");
                }
                if (isAllEmpty)
                    continue;
                if (i > startRowIndex)
                    listData.add(rowData);
            }
            return listData;
        } catch (BusinessException e) {
            logger.error("文件解析错误，第{}行", rowIndex + 1, e);
            throw e;
        } catch (Exception e) {
            logger.error("文件解析错误，第{}行", rowIndex + 1, e);
            throw new BusinessException("文件第" + (rowIndex + 1) + "行解析错误");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("输入流关闭失败", e);
                }
            }
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null)
            return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(cell.getNumericCellValue());
        } else {
            String cellValue = cell.getStringCellValue();
            if (cellValue == null || cellValue.isEmpty())
                return "";
            else return cellValue;
        }
    }
}
