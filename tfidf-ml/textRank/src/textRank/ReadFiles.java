package textRank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReadFiles {
	
	/**
	 * ��ȡ�ļ��µ��ļ�
	 * �����ļ�·���б�
	 * @param filepath
	 * @return
	 */
	public List<String> readFile(String filepath){
		File file=new File(filepath);
		List<String> fileLists=new ArrayList<String>();
		if(!file.isDirectory()){
			System.out.println("����Ĳ���Ӧ��Ϊ[�ļ�����]");
		}else if(file.isDirectory()){
			String[] fileList=file.list();
			for(int i=0;i<fileList.length;i++){
				File readfile=new File(filepath+"\\"+fileList[i]);
				if(readfile.isFile()){
					fileLists.add(readfile.getAbsolutePath());
				}
			}
		}
		return fileLists;
	}
	
	/**
	 * �����е��ĵ�����һ����һ��mapȥ�洢
	 * Map<String,String>===>(filename,content)
	 * @param filePath
	 * @return Map<String,String>
	 */
	public Map<String,String> readFileAllContent(String filePath){
		Map<String,String> doc_content=new HashMap<String, String>();
		List<String> fileList=readFile(filePath);
	    for(String filename:fileList){
	    	String filecontent=readContent(filename);
	    	doc_content.put(filename, filecontent);
	    }    
	    return doc_content;
	}
	/**
	 * ��ȡ�ļ���һ��content
	 * @param file
	 * @return String
	 */
	public String readContent(String file){
		String result="";
	    InputStreamReader is;
		try {
			is = new InputStreamReader(new FileInputStream(file), "gbk");
			BufferedReader br = new BufferedReader(is);
		    String line = br.readLine(); 
			while(line !=null){ 	
			    line=br.readLine();
			    result+=line;
			}
			br.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
			
		return result;
	}
	/**
	 * ����ȡ�õ��ı���doc��content������ִʴ���
	 * ���ص���һ���ִʺõ�map===����docname,wordsContent��
	 * @param doc_words
	 * @return Map<String,String>
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Map<String,String> cutWordtoMap(HashMap<String,String> doc_words) throws FileNotFoundException, IOException{
		Map<String,String> wordsmap=new HashMap<String, String>();
		Map<String,String> words_d_stopword=new HashMap<String, String>();
		NLPIR nlpir = new NLPIR("f:/nlpir/lib/win64/NLPIR","f:/nlpir/");
		wordsmap=nlpir.segMapValue(doc_words, 1);
		
		for(Map.Entry<String, String> entry:wordsmap.entrySet()){
			String key=entry.getKey();
			String words=entry.getValue();
			
			String words_delete_stopword=WordFiltering.removeSentenceStopWords(WordFiltering.filterWordsByPOS(words," ", ""), " ","f:/stopword/stop_words_hit.hit");
			words_d_stopword.put(key, words_delete_stopword);
		}
			
	    return words_d_stopword;
	}

}

