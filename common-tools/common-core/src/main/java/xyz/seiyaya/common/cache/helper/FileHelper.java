package xyz.seiyaya.common.cache.helper;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
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


    /**
     * 将content写入到file对应的路径下面
     * @param content
     * @param filePath
     */
    public static void writeFile(String content,String filePath){
        FileWriter fileWriter = null;
        try {
            if (createFile(filePath)) {
                fileWriter = new FileWriter(filePath, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(content);
                bufferedWriter.close();
                fileWriter.close();
            } else {
                System.err.println("生成失败，文件已存在！");
            }
        } catch (Exception e) {
            log.error("写入文件发生异常",e);
        }finally{
            if (fileWriter!=null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    log.error("关闭流出现异常",e);
                }
            }
        }
    }

    /**
     * 指定路径创建创建文件
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            log.error("文件路径:{}已经存在该文件",filePath);
            return false;
        }
        if (filePath.endsWith(File.separator)) {
            log.error("{}为目录，不能创建文件",filePath);
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                log.error("创建文件所在的目录失败!");
                return false;
            }
        }

        // 创建文件
        try {
            if (file.createNewFile()) {
                System.err.println(filePath + " 文件创建成功!");
                log.info("{}文件创建成功",filePath);
                return true;
            } else {
                log.error("{}文件创建失败",filePath);
                return false;
            }
        } catch (Exception e) {
            log.error("{}文件创建失败!",filePath,e);
            return false;
        }
    }
}
