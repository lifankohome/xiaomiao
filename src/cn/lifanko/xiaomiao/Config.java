package cn.lifanko.xiaomiao;
/**
 * Created By IntelliJ IDEA 15.0.2
 * Author lifanko lee
 * Date 2017/10/28
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Config {
    public static void save(String filePath, String stuId) {
        String content = "This is a configuration file of Innovative Developing Platform. "
                + "Do not change any character of the file, or your key information "
                + "would be lost and your computer may halt. " + stuId;

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("新建配置文件成功");
                } else {
                    System.out.println("新建配置文件失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (file.delete()) {
                System.out.println("配置文件存在-删除成功");
            } else {
                System.out.println("配置文件存在-删除失败");
            }
        }

        try {
            Runtime.getRuntime().exec("attrib " + "\"" + file.getAbsolutePath() + "\"" + " +H");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            assert out != null;
            out.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } // \r\n即为换行
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }// 把缓存区内容压入文件
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } // 最后记得关闭文件

        System.out.println("配置文件已更新");
    }

    public static String read(String filePath){
        String lineText = null;
        String encoding = "GBK";
        File file = new File(filePath);
        if (file.isFile() && file.exists()) { // 判断文件是否存在
            InputStreamReader read = null;
            try {
                read = new InputStreamReader(new FileInputStream(file), encoding);
            } catch (UnsupportedEncodingException | FileNotFoundException e) {
                e.printStackTrace();
            }  // 考虑到编码格式

            assert read != null;
            BufferedReader bufferedReader = new BufferedReader(read);
            try {
                lineText = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                read.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lineText = "文件不存在";
        }
        return lineText;
    }
}
