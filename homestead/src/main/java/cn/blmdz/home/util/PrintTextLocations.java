package cn.blmdz.home.util;

import java.io.IOException;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PrintTextLocations extends PDFTextStripper {

    public PrintTextLocations() throws IOException {
        super.setSortByPosition(true);
    }

//    public static Set<String> set = new HashSet<>();
    
    @Override
    protected void processTextPosition(TextPosition text) {
//        set.add(text.getFont().getName());
//        System.out.println(text.getFont().getName() + " String[" + text.getXDirAdj() + "," + text.getYDirAdj() + " fs=" + text.getFontSize()
//                + " xscale=" + text.getXScale() + " height=" + text.getHeightDir() + " space=" + text.getWidthOfSpace()
//                + " width=" + text.getWidthDirAdj() + "]" + text.toString());
        super.processTextPosition(text);
    }
}
