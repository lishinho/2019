package files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class generateData {
    public static void main(String[] args) throws IOException {
        final int MAX=10000000;
        List<File> PFile = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
        	File f = File.createTempFile("myInputFile", ".txt", new File("C:\\newFile"));
        	if (f.exists())
                f.delete();
            BufferedWriter bufw=new BufferedWriter(new FileWriter(f));
            for (int j=0;j<MAX;++j){
                bufw.write(getRandomNumber());
                bufw.newLine();
            }
            PFile.add(f);
            bufw.flush();
            bufw.close();
        }
        
    }
    
    public static String getRandomNumber(){
        StringBuilder sb=new StringBuilder();
        Random random=new Random();
        int randint =(int)Math.floor((random.nextDouble()*Integer.MAX_VALUE));
        sb.append(randint);

        return sb.toString();
    }
}
