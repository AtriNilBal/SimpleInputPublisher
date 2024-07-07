package org.atrinils.utils;

import com.creditdatamw.zerocell.Reader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.atrinils.model.TestData;
import org.atrinils.model.TestDataZeroCellModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class ExcelReader {

    //with library POI
    //with library ZeroCell

    //I have a excel file with test data in it in a path "path ending with filename with extension"

    public String readCellData(String sheetName, String path, int rowNum, int cellNum) throws IOException {
        FileInputStream fi = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(fi);
        XSSFSheet sheet = workbook.getSheet(sheetName);
        XSSFRow row = sheet.getRow(rowNum);
        XSSFCell cell = row.getCell(cellNum);

        DataFormatter dataFormatter = new DataFormatter();
        String data;
        try {
            data = dataFormatter.formatCellValue(cell);
        } catch(Exception e) {
            System.out.printf("Exception caught during data read and format " +
                    "Sheet/Row/Cell - %s/%d/%d\n", sheetName, rowNum, cellNum);
            data = "";
        }
        workbook.close();
        fi.close();
        return data;
    }

    public TestData excelToPojoConverter(String sheetName, String path) throws IOException {
        TestData testData = new TestData();
        testData.setTestCaseId(Integer.valueOf(readCellData(sheetName, path, 1,0)));
        testData.setCurrency(readCellData(sheetName, path, 1, 1));
        testData.setName(readCellData(sheetName, path, 1, 2));
        testData.setExecute(Boolean.getBoolean(readCellData(sheetName, path, 1, 3)));
        return testData;
    }

    public void readExcelDataIntoJson(String sheetName, String path) throws IOException {
        TestData testData = excelToPojoConverter(sheetName, path);
        String jsonFilePath = "testData.json";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(jsonFilePath), testData);
    }

    public static void main(String[] args) {
        ExcelReader excelReader = new ExcelReader();
        String sheetName = "Sheet1";
        String path = "src/test/testData/TestData.xlsx";
        System.out.println(Instant.now());
        try {
            excelReader.readExcelDataIntoJson(sheetName, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Instant.now());

        System.out.println("|******************************|");

        System.out.println(Instant.now());

        List<TestDataZeroCellModel> listOfTestData = Reader.of(TestDataZeroCellModel.class)
                .from(new File(path))
                .sheet(sheetName)
                .list();
        listOfTestData.forEach(
                System.out::println
        );
        System.out.println(Instant.now());
    }

}
