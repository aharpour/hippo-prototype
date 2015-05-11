package com.aharpour.codelib;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

public class Optimized {
    public static void main(String[] args) throws IOException {

        BufferedImage originalImage = ImageIO.read(ClassLoader.getSystemResource("image.jpg"));

        File outputfile = new File("optimizedImage.jpg");

        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = (ImageWriter) iter.next();
        
        ImageWriteParam iwp = writer.getDefaultWriteParam();

        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(0.85F);
        
        iwp.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);

        FileImageOutputStream output = new FileImageOutputStream(outputfile);
        writer.setOutput(output);
        IIOImage outimage = new IIOImage(originalImage, null, null);
        writer.write(null, outimage, iwp);
        writer.dispose();

    }
}
