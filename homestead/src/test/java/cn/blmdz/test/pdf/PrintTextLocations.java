package cn.blmdz.test.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PrintTextLocations extends PDFTextStripper {

    public PrintTextLocations() throws IOException {
        super.setSortByPosition(true);
    }

    public static Set<String> set = new HashSet<>();

    public static void main(String[] args) throws Exception {

        PDDocument document = null;
        
        try {
            File input = new File("c:/001.pdf");
            document = PDDocument.load(input);
            PrintTextLocations printer = new PrintTextLocations();
            printer.getText(document);
            System.out.println(set.toString());
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                ImageIO.write(new PDFRenderer(document).renderImageWithDPI(i, 250, ImageType.RGB), "jpg", new FileOutputStream(new File("c:/xxx" + i +".jpg")));
            }

        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
    
    @Override
    protected void processTextPosition(TextPosition text) {
        set.add(text.getFont().getName());
        System.out.println(text.getFont().getName() + " String[" + text.getXDirAdj() + "," + text.getYDirAdj() + " fs=" + text.getFontSize()
                + " xscale=" + text.getXScale() + " height=" + text.getHeightDir() + " space=" + text.getWidthOfSpace()
                + " width=" + text.getWidthDirAdj() + "]" + text.toString());
        super.processTextPosition(text);
    }
}
