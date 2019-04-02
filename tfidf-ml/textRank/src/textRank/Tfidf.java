package textRank;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tfidf {
	/**
	 * �ĵ������͵�������
	 */
	static Map<String, Integer> docIndex=new HashMap<String, Integer>();
	static Map<String, Integer> wordIndex=new LinkedHashMap<String, Integer>();
	
	/**
	 * ����ÿ���ĵ���tfֵ
	 * @param wordAll
	 * @return
	 */
	public static Map<String,Float> tfCalculate(String wordAll){
		//��ţ����ʣ�����������
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		//��ţ����ʣ����ʴ�Ƶ��
		HashMap<String, Float> tf = new HashMap<String, Float>();
		int wordCount=0;
		
		/**
		 * ͳ��ÿ�����ʵ�����������ŵ�map��ȥ
		 * �����Ժ����ÿ�����ʵĴ�Ƶ
		 * ���ʵ�tf=�õ��ʳ��ֵ�����n/�ܵĵ�����wordCount
		 */
		for(String word:wordAll.split(" ")){
			wordCount++;
			if(dict.containsKey(word)){
				dict.put(word,  dict.get(word)+1);
			}else{
				dict.put(word, 1);
			}
		}
		
		for(Map.Entry<String, Integer> entry:dict.entrySet()){
			float wordTf=(float)entry.getValue()/wordCount;
			tf.put(entry.getKey(), wordTf);
		}
		return tf;
	} 
	
	/**
	 * 
	 * @param D ���ĵ���
	 * @param doc_words ÿ���ĵ���Ӧ�ķִ�
	 * @param tf ����õ�tf
	 * @return ÿ���ĵ��еĵ��ʵ�tfidf��ֵ
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static Map<String,Float> tfidfCalculate(int D, Map<String,String> doc_words,Map<String,Float> tf) throws FileNotFoundException, IOException{

		HashMap<String,Float> tfidf=new HashMap<String, Float>();
		for(String key:tf.keySet()){
            int Dt=0;
			for(Map.Entry<String, String> entry:doc_words.entrySet()){
				
				String[] words=entry.getValue().split(" ");
				
				List<String> wordlist=new ArrayList<String>();
				for(int i=0;i<words.length;i++){
					wordlist.add(words[i]);
					
				}
				if(wordlist.contains(key)){
					Dt++;
				}
			}
			float idfvalue=(float) Math.log(Float.valueOf(D)/Dt);
			tfidf.put(key, idfvalue * tf.get(key));
			
		}		
		return tfidf;
	}
	
	/**
	 * ����
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void saveIndex(String wordfile,String docfile,Map<String,String> doc_words) throws FileNotFoundException, IOException{
		//�����������ĵ�����
		int doc_num=1;
		int word_num=1;
		String wordok="";
		String docok="";
		for(Map.Entry<String, String> entry:doc_words.entrySet()){
			
			String filename=entry.getKey();
			File filea=new File(filename);
			docIndex.put(filea.getName(), doc_num);
			
			docok =filea.getName()+"\t"+doc_num+"\n";
			
			FileUtils.save(docfile, docok, false);
			
			for(String word:entry.getValue().split(" ")){
				if(!wordIndex.containsKey(word)){
					wordIndex.put(word, word_num);					
					wordok=word+"\t"+word_num+"\n";
					FileUtils.save(wordfile, wordok, false);
					word_num++;				
				}
			}
			
			doc_num++;
			
		}
		
	}
	
}
