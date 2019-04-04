package externalSort;

import java.io.File;


public class test {
    public static void main(String[] args) throws Exception {
        long start=System.currentTimeMillis();
        File inputFile=new File("C:\\newFile");
        File outputFile=new File("C:\\outputFile");
        File tempFile=new File("C:\\tempFile");
//        if (outputFile.exists())
//            outputFile.delete();
        main.test(inputFile,outputFile,tempFile);
        long end=System.currentTimeMillis();
        System.out.println(end-start);
    }
}
