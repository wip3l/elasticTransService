package com.jh.elastictransservice.utils;

import com.jh.elastictransservice.common.pojo.FieldName;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * @author liqijian
 * @Description 数据解析工具类
 */
@Component
public class DataTransUtils {

    /**
     * @Description 中文列名转拼音
     * @param strings 原始列头
     * @return newStrings
     */
    public  String[] ch2py(String[] strings) {
        String[] newStrings = new String[strings.length];
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        for ( int i =0 ; i < strings.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            char[] hanYuArr = strings[i].trim().toCharArray();
            try {
                for (int j = 0, len = hanYuArr.length; j < len; j++) {
                    if (Character.toString(hanYuArr[j]).matches("[\\u4E00-\\u9FA5]+")) {
                        String[] pys = PinyinHelper.toHanyuPinyinStringArray(hanYuArr[j], format);
                        for (String py : pys) {
                            stringBuilder.append(py);
                        }
                    } else {
                        stringBuilder.append(hanYuArr[j]);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            newStrings[i] = String.valueOf(stringBuilder);
        }
        return newStrings;
    }

    /**
     * 把汉字名称转为拼音，并返回List<FieldName>，FieldName包含中文名称和拼音
     * @param strings
     * @return
     */
    public List<FieldName> ch2pyFieldName(String[] strings) {
        List<FieldName> list = new ArrayList<>();
        String[] newStrings = new String[strings.length];
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        for ( int i =0 ; i < strings.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            String chName = strings[i].trim();
            char[] hanYuArr = chName.toCharArray();
            try {
                for (int j = 0, len = hanYuArr.length; j < len; j++) {
                    if (Character.toString(hanYuArr[j]).matches("[\\u4E00-\\u9FA5]+")) {
                        String[] pys = PinyinHelper.toHanyuPinyinStringArray(hanYuArr[j], format);
                        for (String py : pys) {
                            stringBuilder.append(py);
                        }
                    } else {
                        stringBuilder.append(hanYuArr[j]);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            FieldName fieldName = new FieldName();
            fieldName.setEnName(String.valueOf(stringBuilder));
            fieldName.setChName(chName);
            list.add(fieldName);
        }
        return list;
    }


    /**
     * @Description 递归遍历所有文件
     * @param path 文件路径
     * @return Set<File>
     */
    public Set<File>  listAllFiles(String path){
        Set<File> files = new HashSet<>();
        File file = new File(path);
        if (file.isDirectory()) {
            Set<File> files1 = listNext(file);
            files.addAll(files1);
            files.remove(file);
            for (File f : files1) {
                if (f.isDirectory()) {
                    files.addAll(listNext(f));
                    files.remove(f);
                    for (File file1 :listNext(f)){
                        Set<File> files2 = listAllFiles(file1.getPath());
                        files.remove(file1);
                        files.addAll(files2);
                    }
                }
            }
        }else {
            files.add(file);
        }
        return files;
    }

    /**
     * @Description 遍历文件下一级
     * @param file 当前文件级
     * @return Set<File>
     */
    private  Set<File> listNext (File file) {
        File[] files1 = file.listFiles();
        assert files1 != null;
        return new HashSet<>(Arrays.asList(files1));
    }


    /**
     * @Description 获取目录下的文件
     * @param path 文件夹路径
     * @return fileSet
     */
    public Set<File> listFiles(String path){
        Set<File> fileSet = new HashSet<>();
        File file = new File(path);
        File[] fs = file.listFiles();
        assert fs != null;
        for(File f:fs){
            if(!f.isDirectory()) {
                fileSet.add(f);
            }
        }
        return fileSet;
    }
}
