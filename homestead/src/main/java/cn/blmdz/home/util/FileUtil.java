package cn.blmdz.home.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

import cn.blmdz.home.enums.FileType;

public class FileUtil {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final Map<String, FileType> TYPES;
    
    static {
        TYPES = Maps.newHashMap();
        TYPES.put("rar", FileType.RAR);
        TYPES.put("zip", FileType.ZIP);
        TYPES.put("ios", FileType.IOS);
        TYPES.put("html", FileType.HTML);
        TYPES.put("rm", FileType.RM);
        TYPES.put("xls", FileType.XLS);
        TYPES.put("xlsx", FileType.XLS);
        TYPES.put("doc", FileType.DOC);
        TYPES.put("docx", FileType.DOC);
        TYPES.put("txt", FileType.TXT);
        TYPES.put("png", FileType.IMAGE);
        TYPES.put("jpg", FileType.IMAGE);
        TYPES.put("jpeg", FileType.IMAGE);
    }
    
    public static FileType fileType(String fileName) {
       int index = fileName.lastIndexOf(".");
       String fileType = fileName.substring(index).replace(".", "").toLowerCase();
       FileType type = TYPES.get(fileType);
       return type == null ? FileType.OTHER : type;
    }

    public static String newFileName(String fileName) {
       int index = fileName.lastIndexOf(".");
       String fileType = fileName.substring(index).replace(".", "");
       
       return DATE_FORMAT.format(new Date()) + (int)((Math.random() * 9.0D + 1.0D) * 10000.0D) + "." + fileType;
    }
 }