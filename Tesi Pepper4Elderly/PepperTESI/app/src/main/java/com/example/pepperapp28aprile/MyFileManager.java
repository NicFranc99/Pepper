package com.example.pepperapp28aprile;

import com.github.wihoho.jama.Matrix;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/*
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster; */


public class MyFileManager {
    // Convert PGM to Matrix
    public static Matrix convertPGMtoMatrix(String address) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(address);
        Scanner scan = new Scanner(fileInputStream);


        // Discard the magic number
        //String firstLineString = scan.nextLine();

        scan.nextLine();

        int picWidth = scan.nextInt();
        int picHeight = scan.nextInt();

       /* if(firstLineString.length() >3){
            System.out.println(firstLineString);
            System.out.println(firstLineString.split("\\s").toString());
            String[] firstLineArray = firstLineString.split(" ");
            System.out.println(firstLineArray.toString());
            picWidth = Integer.getInteger(firstLineArray[1]);
            picHeight = Integer.getInteger(firstLineArray[2]);
        }
        else{
            picWidth = scan.nextInt();
            picHeight = scan.nextInt();
        }*/

        // Read pic width, height and max value
       /* Float picWidthFloat = scan.nextFloat();
        Float picHeightFloat = scan.nextFloat();
        int picWidth = picWidthFloat.intValue();
        int picHeight = picHeightFloat.intValue();*/

        fileInputStream.close();

        // Now parse the file as binary data
        fileInputStream = new FileInputStream(address);
        DataInputStream dis = new DataInputStream(fileInputStream);

        // look for 4 lines (i.e.: the header) and discard them
        int numnewlines = 3;
        while (numnewlines > 0) {
            char c;
            do {
                c = (char) (dis.readUnsignedByte());
            } while (c != '\n');
            numnewlines--;
        }

        // read the image data
        double[][] data2D = new double[picHeight][picWidth];
        for (int row = 0; row < picHeight; row++) {
            for (int col = 0; col < picWidth; col++) {
                data2D[row][col] = dis.readUnsignedByte();
            }
        }

        return new Matrix(data2D);
    }

    // Convert Matrix to PGM with numbers of row and column
    public static Matrix normalize(Matrix input) {
        int row = input.getRowDimension();

        for (int i = 0; i < row; i++) {
            input.set(i, 0, 0 - input.get(i, 0));

        }

        double max = input.get(0, 0);
        double min = input.get(0, 0);

        for (int i = 1; i < row; i++) {
            if (max < input.get(i, 0))
                max = input.get(i, 0);

            if (min > input.get(i, 0))
                min = input.get(i, 0);

        }

        Matrix result = new Matrix(112, 92);
        for (int p = 0; p < 92; p++) {
            for (int q = 0; q < 112; q++) {
                double value = input.get(p * 112 + q, 0);
                value = (value - min) * 255 / (max - min);
                result.set(q, p, value);
            }
        }

        return result;

    }

/*
    //convert matrices to images
    public static void convertMatricetoImage(Matrix x, int featureMode) throws IOException {
        int row = x.getRowDimension();
        int column = x.getColumnDimension();

        for (int i = 0; i < column; i++) {
            Matrix eigen = normalize(x.getMatrix(0, row - 1, i, i));


            BufferedImage img = new BufferedImage(92, 112, BufferedImage.TYPE_BYTE_GRAY);
            WritableRaster raster = img.getRaster();

            for (int m = 0; m < 112; m++) {
                for (int n = 0; n < 92; n++) {
                    int value = (int) eigen.get(m, n);
                    raster.setSample(n, m, 0, value);
                }
            }

            File file = null;
            if (featureMode == 0)
                file = new File("Eigenface" + i + ".bmp");
            else if (featureMode == 1)
                file = new File("Fisherface" + i + ".bmp");
            else if (featureMode == 2)
                file = new File("Laplacianface" + i + ".bmp");

            if (!file.exists())
                file.createNewFile();

            ImageIO.write(img, "bmp", file);
        }

    }

    //convert single matrix to an image
    public static void convertToImage(Matrix input, int name) throws IOException {
        File file = new File(name + " dimensions.bmp");
        if (!file.exists())
            file.createNewFile();

        BufferedImage img = new BufferedImage(92, 112, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = img.getRaster();

        for (int m = 0; m < 112; m++) {
            for (int n = 0; n < 92; n++) {
                int value = (int) input.get(n * 112 + m, 0);
                raster.setSample(n, m, 0, value);
            }
        }

        ImageIO.write(img, "bmp", file);

    }
*/
}
