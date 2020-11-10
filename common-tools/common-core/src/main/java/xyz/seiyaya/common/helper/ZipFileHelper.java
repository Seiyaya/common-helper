package xyz.seiyaya.common.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zip文件操作
 *
 * @author wangjia
 * @version 1.0
 * @date 2020/3/27 11:02
 */
public class ZipFileHelper {


    /**
     * 解压文件
     *
     * @param zipFile
     */
    public static void unzipFile(File zipFile) throws Exception {
        String parent = zipFile.getParent();
        String dirName = zipFile.getName().split(".zip")[0];
        File directory = new File(parent, dirName);
        unzipFile(zipFile, directory);
    }

    /**
     * 解压文件到指定目录
     *
     * @param zipFile
     * @param dir
     */
    public static void unzipFile(File zipFile, File dir) throws Exception {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry = null;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            outputFileFromStream(zipInputStream, zipEntry, dir);
        }
        zipInputStream.close();
    }

    /**
     * 输出文件到指定目录
     *
     * @param zipInputStream
     * @param zipEntry
     * @param dir
     */
    private static void outputFileFromStream(ZipInputStream zipInputStream, ZipEntry zipEntry, File dir) throws IOException {
        File outFile = new File(dir, zipEntry.getName());
        (new File(outFile.getParent())).mkdirs();
        if (zipEntry.isDirectory()) {
            outFile.mkdir();
        } else {
            outFile.createNewFile();
            FileOutputStream out = new FileOutputStream(outFile);
            byte[] buf = new byte[10 * 1024];
            int len;
            while ((len = zipInputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.close();
        }
    }
}
