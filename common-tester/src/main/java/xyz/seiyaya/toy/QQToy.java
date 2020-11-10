package xyz.seiyaya.toy;

import xyz.seiyaya.common.helper.DBParam;
import xyz.seiyaya.common.helper.ExcelHelper;
import xyz.seiyaya.common.helper.HttpHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/23 9:48
 */
public class QQToy {

    public static void main(String[] args) {
        DBParam dbParam = ExcelHelper.readExcel(new File("D:/wangjia/1.xlsx"));
        List<DBParam> sheet = dbParam.getT("Sheet1", List.class);

        HttpHelper httpUtils = HttpHelper.getHttpUtils();
        for(int i=1;i<sheet.size();i++){
            DBParam model = sheet.get(i);
            String o = model.getString("6");
            byte[] bytes = httpUtils.sendGetByte(String.format("http://qlogo1.store.qq.com/qzonelogo/%s/1/0", o));
            File file = new File(String.format("D:/wangjia/qqImages/%s.jpg", o));
            if(!file.exists()){
                try {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(bytes,0,bytes.length);
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
