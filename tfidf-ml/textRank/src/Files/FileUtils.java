package Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class FileUtils {
	
	/**
	 * 读取文件夹下的所有文件的内容	 * @param path
	 */
	public static void readFilesDir(String path){
		LinkedList<File> list = new LinkedList<File>();
		File dir = new File(path);
		File[] files = dir.listFiles(); 
		
		for(File file : files){
			if(file.isDirectory()){
				list.add(file);
				System.out.println(file.getAbsolutePath());
			}else{
				//澶勭悊鏂囦欢鍐呭
				System.out.println(file.getAbsolutePath());
			}
		}
		
		File temp;
		while(!list.isEmpty()){
			temp = list.removeFirst();
			if(temp.isDirectory()){
				files = temp.listFiles();
				if(files == null) continue;
				for(File file : files){
					if(file.isDirectory()){
						list.add(file);
					}else{
						//澶勭悊鏂囦欢鍐呭
						System.out.println(file.getAbsolutePath());
					}
				}
			}else{
				//澶勭悊鏂囦欢鍐呭,杩欑鎯呭喌濂藉儚涓嶄細鍙戠敓
				System.out.println("-------------");
			}
		}
	}
	
    /**
     * 璇诲彇鍗曚釜鏂囦欢鐨勫唴瀹�
     * @param file
     * @return 鏂囦欢鐨勫唴瀹癸紝String
     */
	public static String readContents(File file){
		StringBuilder res = new StringBuilder();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
			while(br.ready()){
				res.append(br.readLine() + "\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res.toString();
	}
	
	/**
	 * 淇濆瓨鏂囦欢
	 */
	public static void saveFiles(String contents, String output){
		File outputFile = new File(output);
		try {
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
			bw.write(contents);
			
			bw.flush();
			bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("淇濆瓨鎴愬姛锛侊紒锛�");
	}
	
	public static void main(String[] args) {
		String path = "F:/";
		
		readFilesDir(path);
				
	}
}
