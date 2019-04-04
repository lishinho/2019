package externalSort;

import java.io.File;


public class test {
    public static void main(String[] args) throws Exception {
        long start=System.currentTimeMillis();
        File inputFile=new File("E:\\website\\myInputFile.txt");
        File outputFile=new File("E:\\website\\outputFile.txt");
        File tempFile=new File("E:\\website\\tempFile");
        if (outputFile.exists())
            outputFile.delete();
        main.test(inputFile,outputFile,tempFile);
        long end=System.currentTimeMillis();
        System.out.println(end-start);
    }
}
