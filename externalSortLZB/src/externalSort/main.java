package externalSort;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class main {
    static final int maxSize = 8000000;//内存每次最多放8000000条记录
    static final char[] maxKey = {255, 255, 255, 255, 255, 255, 255, 255};
    static final int MAX=10000000;
	static int cnt = 0;


    public static void test(File inputFile, File outputFile, File tempFile) throws Exception {

    	int page = 0;
    	File[] inputFiles = inputFile.listFiles();
    	List<File> input = new ArrayList<>();
    	for(File f:inputFiles) {
    		input.add(f);
    	}
        @SuppressWarnings("resource")
		BufferedReader bufr = new BufferedReader(new FileReader(input.get(page++)));

        String[] heapArray = new String[maxSize];
        String line = null;//用来存放每次从缓冲区读入的一条记录
        int i = 0;//统计向缓冲区读入了记录条数
        List<File> tempFiles = new ArrayList<>();
        int heapSize = 0;
//replaceSelection begin
        while ((line = bufr.readLine()) != null || page < input.size()) {
        	if((line = bufr.readLine()) == null) {
        		bufr = new BufferedReader(new FileReader(input.get(page++)));
        		line = bufr.readLine();
        	}	
        	heapArray[i++] = line;
            if (i == maxSize)
                break;
        }
        while (i == maxSize) {
            heapSize = maxSize;
            File newTempFile = File.createTempFile("tempFile", ".txt", tempFile);
            tempFiles.add(newTempFile);
            BufferedWriter bufw = new BufferedWriter(new FileWriter(newTempFile));
            buildHeap(heapArray, heapSize, 0);
            while (heapSize != 0 && (line = bufr.readLine()) != null) {
                bufw.write(heapArray[0]);
                bufw.newLine();
//                line = bufr.readLine();
                if (compareStr(line, heapArray[0]) > 0) {
                    heapArray[0] = line;
                } else {
                    heapArray[0] = heapArray[heapSize - 1];
                    heapArray[heapSize - 1] = line;
                    heapSize--;
                }
                siftDown(heapArray, 0, heapSize);
            }
            if (heapSize != 0) {//file input is completed
                i = i - heapSize;
                while (heapSize != 0) {
                    bufw.write(heapArray[0]);
                    bufw.newLine();
                    heapArray[0] = heapArray[heapSize - 1];
                    heapSize--;
                    siftDown(heapArray, 0, heapSize);
                }
            }
            bufw.close();
        }
        //continue to read the rest data in buffer
        if (i != 0) {
            heapSize = i;
            File newTempFile = File.createTempFile("tempFile", ".txt", tempFile.getParentFile());
            tempFiles.add(newTempFile);
            BufferedWriter bufw = new BufferedWriter(new FileWriter(newTempFile));
            int offset = maxSize - heapSize;
            buildHeap(heapArray, heapSize, offset);
            while (heapSize != 0) {
                bufw.write(heapArray[offset]);
                bufw.newLine();
                heapArray[offset] = heapArray[offset + heapSize - 1];
                heapSize--;
                siftDown(heapArray, offset, heapSize);

            }
            bufw.close();
        }
//replaceSelection end,all data are sorted into some separate temFile,all temFile are in tempFiles list.
        //release memory
        heapArray = null;
        System.gc();
        //begin MultiWayMergeSort
        multiWayMergeSort(tempFiles, File.createTempFile("myOutputFile", ".txt",new File(outputFile.getAbsolutePath())));

//delete tempFiles
        for (File file : tempFiles) {
            file.delete();
        }
    }


    private static void buildHeap(String heapArray[], int size, int start) {
        for (int i = size / 2 - 1; i >= start; i--) {
            siftDown(heapArray, i, size);
        }
    }

    private static void siftDown(String[] heapArray, int i, int size) {
        int j = 2 * i + 1;
        String temp = heapArray[i];
        while (j < size) {
            if (j < size - 1 && (compareStr(heapArray[j], heapArray[j + 1])> 0))
                ++j;
            if (compareStr(temp, heapArray[j])> 0) {
                heapArray[i] = heapArray[j];
                i = j;
                j = 2 * j + 1;
            } else break;
        }
        heapArray[i] = temp;
    }

    private static void multiWayMergeSort(List<File> files, File outputFile) throws IOException {
    	int ways = files.size();
        int length_per_run = maxSize / ways;
        Run[] runs = new Run[ways];
        for (int i = 0; i < ways; i++) {
            runs[i] = new Run(length_per_run);
        }
        List<BufferedReader> rList = new ArrayList<>();
        //read files' data into runs' buffer
        for (int i = 0; i < ways; i++) {
            BufferedReader bufr = new BufferedReader(new FileReader(files.get(i)));
            rList.add(i, bufr);
            int j = 0;
            while ((runs[i].buffer[j] = bufr.readLine()) != null) {
                ++j;
                if (j == length_per_run)
                    break;
            }
            runs[i].length = j;
            runs[i].index = 0;
        }
        //merge the files and write to outputFile
        int[] ls = new int[ways];//loser tree
        createLoserTree(ls, runs, ways);
        BufferedWriter bufw = new BufferedWriter(new FileWriter(outputFile));
        int liveRuns = ways;
        while (liveRuns > 0) {
        	if(cnt > MAX) {
        		File newOutput = File.createTempFile("myOutputFile", ".txt", new File(outputFile.getAbsolutePath()));
        		bufw.flush();
                bufw=new BufferedWriter(new FileWriter(newOutput));
        		cnt = 0;
        	}
            bufw.write(runs[ls[0]].buffer[runs[ls[0]].index++]);
            cnt++;
            bufw.newLine();
            if (runs[ls[0]].index == runs[ls[0]].length) {
                //reload
                int j = 0;
                while ((runs[ls[0]].buffer[j] = rList.get(ls[0]).readLine()) != null) {
                    j++;
                    if (j == length_per_run) {
                        break;
                    }
                }
                runs[ls[0]].length = j;
                runs[ls[0]].index = 0;
            }
            if (runs[ls[0]].length == 0) {
                liveRuns--;
                String maxString = new String(maxKey);
                runs[ls[0]].buffer[runs[ls[0]].index] = maxString;
            }
            adjust(ls, runs, ways, ls[0]);
        }
        bufw.flush();
        bufw.close();
        for (BufferedReader bufr : rList) {
            bufr.close();
        }

    }

    private static void createLoserTree(int[] ls, Run[] runs, int n) {
        //ways equals to the number of nodes in loserTree

        for (int i = 0; i < n; i++) {
            ls[i] = -1;
        }
        for (int i = n - 1; i >= 0; i--) {
            adjust(ls, runs, n, i);
        }
    }

    private static void adjust(int[] ls, Run[] runs, int n, int s) {
        int t = (s + n) / 2;
        int temp = 0;
        while (t != 0) {
            if (s == -1)
                break;
            if (ls[t] == -1 || compareStr(runs[s].buffer[runs[s].index], runs[ls[t]].buffer[runs[ls[t]].index]) > 0) {
                temp = s;
                s = ls[t];
                ls[t] = temp;
            }
            t /= 2;
        }
        ls[0] = s;
    }
    
    private static int compareStr(String s1, String s2) {
    	if(s1 == null || s2 == null)
    		return 0;
    	if(s1.length() != s2.length())
    		return s1.length()>s2.length() ? 1 : -1;
    	return s1.compareTo(s2);
    }

    static class Run {
        String[] buffer;
        int length;
        int index;

        Run(int length) {
            this.length = length;
            buffer = new String[length];
        }
    }
}
