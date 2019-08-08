package xyz.seiyaya.common.helper;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileHelper {

    /**
     * 读取文件的每一行
     * @param filePath
     * @return
     */
    public static List<String> readFile(String filePath) {
        List<String> tableNames = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while(line!=null){
                tableNames.add(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            log.error("读取文件出错：",e);
        }
        return tableNames;
    }
}
