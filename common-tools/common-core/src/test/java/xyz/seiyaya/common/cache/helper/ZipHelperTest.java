package xyz.seiyaya.common.cache.helper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/27 11:10
 */
@Slf4j
public class ZipHelperTest {

    @Test
    public void testUnZipFile() throws Exception {
        String itemFile = "D:/wangjia/ApplicationPackage/elasticsearch-5.6.8.zip";

        ZipFileHelper.unzipFile(new File(itemFile));

        log.info("解压完成");
    }
}
