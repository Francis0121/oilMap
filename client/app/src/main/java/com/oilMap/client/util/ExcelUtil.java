package com.oilMap.client.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by SungGeun on 2015-11-22.
 */
public class ExcelUtil {

    private static String TAG = ExcelUtil.class.getSimpleName();

    public static final String ABSOLUTE_PATH = Environment.getExternalStorageDirectory().getPath();
    public static final String filePath = ABSOLUTE_PATH + "/Download/note.xls";

    public static Integer engineLoad = 25;
    public static Integer rpm = 1100;

    private static final Object countLock = new Object();

    public static void readDataFromExcel(){
        try {
            File file = new File(filePath);
            Workbook workbook = Workbook.getWorkbook(file);

            Integer row = 0;
            while(true) {
                Sheet sheet = workbook.getSheet(0);
                Cell rpmCell = sheet.getCell(0, row);
                Cell engineLoadCell = sheet.getCell(1, row);

                Log.d(TAG, Integer.toString(sheet.getRows()));
                if(row >= sheet.getRows()|| rpmCell == null || rpmCell.getContents() == null || "".equals(rpmCell.getContents()) ||
                        engineLoadCell == null || engineLoadCell.getContents() == null || "".equals(engineLoadCell.getContents())){
                    break;
                }

                synchronized (countLock) {
                    if (rpmCell.getType() == CellType.NUMBER) {
                        Integer parseRpm = Integer.parseInt(rpmCell.getContents());
                        rpm = parseRpm;
                    }

                    if(engineLoadCell.getType() == CellType.NUMBER){
                        Integer parseEngineLoad = Integer.parseInt(engineLoadCell.getContents());
                        engineLoad = parseEngineLoad;
                    }
                }
                row++;
                Log.d(TAG, "rpm : " + rpm + ", engineLoad : " + engineLoad);
                Thread.sleep(2500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
