package com.aharpour.codelib;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class Crop {
    public static void main(String[] args) throws IOException {

        BufferedImage originalImage = ImageIO.read(ClassLoader.getSystemResource("image.jpg"));

        BufferedImage image = Scalr.crop(originalImage, 0, 0, originalImage.getWidth() / 2,
                originalImage.getHeight() / 2);
        File outputfile = new File("croppedImage.jpg");
        ImageIO.write(image, "jpg", outputfile);
        
    }
}
