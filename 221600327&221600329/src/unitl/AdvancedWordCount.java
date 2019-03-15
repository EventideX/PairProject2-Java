package unitl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Pattern;

import bean.Word;

public class AdvancedWordCount {
	/**
	 * 计算文件字符数
	 * @param fileName 文件名
	 * @return countOfCharacter 字符数
	 */
	public long characterCount(String fileName) {
		BufferedReader bufferedReader = null;
		StringBuffer stringBuffer = new StringBuffer();
		//读入文件数据
		bufferedReader = IOUtils.readFile(fileName);
		String line = null;
		long count = 0;
		//计算字符数
        try {
            while ((line = bufferedReader.readLine()) != null) {
            	if (Pattern.matches("Title: .*", line)) {
                	count += line.replaceAll("\r\n", "\n").length() - 6;
                } else if(Pattern.matches("Abstract: .*", line)) {
                    count += line.replaceAll("\r\n", "\n").length() - 9;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	if(bufferedReader!=null) {
            		bufferedReader.close();
            	}
            	
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
	return (count-1);
	}
	/**
	 * 计算文件单词数
	 * @param fileName 文件名
	 * @return countOfWord 单词数
	 */
	public long wordCount(String fileName) {
		BufferedReader bufferedReader = null;
		StringBuffer stringBuffer = new StringBuffer();
		//读入文件数据
		bufferedReader = IOUtils.readFile(fileName);
		int byteCode = -1;
		//转换为字符串 
		try {
			while((byteCode = bufferedReader.read()) != -1) {
				//将十进制ASCII码值，转化为字符，添加到data
				stringBuffer.append((char)byteCode);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//用分隔符"|"替换字符串中非字母数字的字符
		String noLetterOrDigitalRegex = "[^a-zA-Z0-9]";
		String updateString = stringBuffer
				.toString()
				.replaceAll("Title: ", "|")
				.replaceAll("Abstract: ", "|")
				.toLowerCase()
				.replaceAll(noLetterOrDigitalRegex, "|");
		long countOfWord = 0;
		int startPosition = 0;
		int endPosition = 0;
		String regex = "^[a-z]{4}[a-z0-9]*";
		//计算到次数
		for(int i = 0; i<updateString.length();) {
			startPosition = i;
			endPosition = i;
			while(Character.isLetterOrDigit(updateString.charAt(i++))) {
				endPosition++;
			}
			if(Pattern.matches(regex, updateString.substring(startPosition, endPosition))) {
				countOfWord++;
			}
		}
		return countOfWord;
	}

	/**
	 * 计算文件行数
	 * @param fileName 文件名
	 * @return count 行数
	 */
	public long lineCount(String fileName) {
		FileReader filereader = null;
		BufferedReader bufferedreader = null;
        //读入文件数据
		long count=0;
		//统计行数
		try {
			filereader = new FileReader(fileName);
			bufferedreader = new BufferedReader(filereader);

			String value = bufferedreader.readLine();
			//按行读取
			while (value != null) {
				//先判断是否为空
				if(!value.trim().isEmpty())
					//string.trim()去除空白字符
					count++;
				if(value.contains("Title: ")) {
					--count;
				}
				value = bufferedreader.readLine();
				//继续往下读
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
          finally { 
        	  try {
        		  if (bufferedreader != null)
        			  bufferedreader.close();
				  if (filereader != null)
					  filereader.close();
			      } catch (IOException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	/**
	 * 计算单词的(加权)词频和topK
	 * @param fileName 文件名
	 * @param topK 词频前K的单词
	 * @param isWeight 是否启用加权计算
	 * @return Word[] 有序单词数组
	 */
	public Word[] topKWordWeighting (String fileName, int topK, boolean isWeight) {
		BufferedReader bufferedReader = null;
		//读入文件数据
		bufferedReader = IOUtils.readFile(fileName);
		String line = null;
		//结果集
	    Word[] result = null;
	    
		try {
			//进行统计的散列表
			Map<String, Integer> countMap = new HashMap<>();
			while((line = bufferedReader.readLine()) != null) {
				String regexTile = "Title: .*";
				int weight = 1;
				if(Pattern.matches(regexTile, line)) {
					line = line.replaceAll("Title: ", "");
					weight = 10;
				}else {
					line = line.replaceAll("Abstract: ", "");
					weight = 1;
				}
				//用分隔符"|"替换字符串中非字母数字的字符
				String noLetterOrDigitalRegex = "[^a-zA-Z0-9]";
				String updateString = line.toLowerCase().replaceAll(noLetterOrDigitalRegex, "|");
				//按分隔符"|"，分割字符串
				String splitStrings[] =  updateString.split("\\|");
				String regex = "[a-z]{4}.*";
				//进行统计
				for(int i = 0; i < splitStrings.length; i++) {
					if(Pattern.matches(regex, splitStrings[i])) {
						Integer outValue = countMap.get(splitStrings[i]);
						if (null == outValue) {
						      outValue = 0;
						}
						if(isWeight) {
							outValue += weight;
						}else {
							outValue++;
						}
						countMap.put(splitStrings[i], outValue);
					}
				}
				
			}
			//求topK
			PriorityQueue<Word> topN = new PriorityQueue<>(topK, comp);
		    Iterator<Map.Entry<String, Integer>> iter = countMap.entrySet().iterator();
		    Map.Entry<String, Integer> entry;
		    while (iter.hasNext()) {
		      entry = iter.next();
		      if (topN.size() < topK) {
		        topN.offer(new Word(entry.getKey(), entry.getValue()));
		      } else {
		        // 如果当前数据比小顶堆的队头大，则加入，否则丢弃
		        if (topN.peek().getCountNum() < entry.getValue()) {
		          topN.poll();
		          topN.offer(new Word(entry.getKey(), entry.getValue()));
		        }else if(topN.peek().getCountNum() == entry.getValue()) {
			        if(topN.peek().getKey().compareTo(entry.getKey())>0){
			        	topN.poll();
				        topN.offer(new Word(entry.getKey(), entry.getValue()));
			        }
		        }
		      }
		    }
		    
		    int wordCount = countMap.size();
		    if(wordCount < topK) {
		    	result = new Word[(int) wordCount];
		    }else {
		    	result = new Word[topK];
		    }
		    topN.toArray(result);
		    //对top10单词排序
		    Arrays.sort(result, comp);

		} catch (IOException e) {
			System.out.println("文件不存在");
			e.printStackTrace();
		}
		return result;

	}
	
	/**
	 * 计算词组的(加权)词频和topK
	 * @param fileName 文件名
	 * @param groupNum 几个单词为一个组
	 * @param topK 词频前K的词组
	 * @param isWeight 是否启用加权计算
	 * @return Word[] 有序单词数组
	 */
	public Word[] topKWordsWeighting (String fileName, int groupNum,int topK, boolean isWeight) {
		BufferedReader bufferedReader = null;
		//读入文件数据
		bufferedReader = IOUtils.readFile(fileName);
		String line = null;
		//结果集
	    Word[] result = null;
	    
		try {
			//进行统计的散列表
			Map<String, Integer> countMap = new HashMap<>();
			while((line = bufferedReader.readLine()) != null) {
				String regexTile = "Title: .*";
				int weight = 1;
				if(Pattern.matches(regexTile, line)) {
					line = line.replaceAll("Title: ", "");
					weight = 10;
				}else {
					line = line.replaceAll("Abstract: ", "");
					weight = 1;
				}
				//用分隔符"|"替换字符串中非字母数字的字符
				line = line.toLowerCase();
				
				//按字符分析
				int groupnumber = 0;//词组单词个数
				int wordStratPosition = 0;//单词开始下标
				int wordEndPosition = 0;//单词开始下标
				int flagPosition = 0;//标记词组第一个单词的结束位置
				Queue<String> wordReadyQueue = new LinkedList<String>();
				for(int i = 0; i < line.length()-3;) {
					boolean flag = false;
					
					if(Character.isLetter(line.charAt(i))) {
						if(Character.isLetter(line.charAt(i+1))) {
							if(Character.isLetter(line.charAt(i+2))) {
								if(Character.isLetter(line.charAt(i+3))) {
									wordStratPosition = i;
									for(int j=i+4; j < line.length(); ++j) {
										if(!Character.isLetterOrDigit(line.charAt(j))) {
											wordEndPosition = j;
											//得到一个单词,加入到队列
											wordReadyQueue.add(line.substring(wordStratPosition, wordEndPosition));
//											System.out.println(line.substring(wordStratPosition, wordEndPosition));
											groupnumber++;//词组单词数+1
											if(groupnumber == 1) {
												flagPosition = j;
											}
											i=j;
											flag = true;
											break;
										}else if((j+1)==line.length()) {
											wordEndPosition = j+1;
											//得到一个单词,加入到队列
											wordReadyQueue.add(line.substring(wordStratPosition));
//											System.out.println(line.substring(wordStratPosition));
											groupnumber++;//词组单词数+1
											if(groupnumber == 1) {
												flagPosition = j;
											}
											i=j;
											flag = true;
											break;
										}
									}
								}else {
									if(!Character.isLetterOrDigit(line.charAt(i))) {
										wordReadyQueue.add(line.charAt(i)+"");
									}else {
										while(wordReadyQueue.poll()!=null) {}//清空队列

										while(Character.isLetterOrDigit(line.charAt(i++))) {
											if(i >= line.length()){
												break;
											}
										}
										groupnumber = 0;
										flag = true;
									}
								}
							}else {
								if(!Character.isLetterOrDigit(line.charAt(i))) {
									wordReadyQueue.add(line.charAt(i)+"");
								}else {
									while(wordReadyQueue.poll()!=null) {}//清空队列

									while(Character.isLetterOrDigit(line.charAt(i++))) {
										if(i >= line.length()){
											break;
										}
									}
									groupnumber = 0;
									flag = true;
								}
							}
						}else {
							if(!Character.isLetterOrDigit(line.charAt(i))) {
								wordReadyQueue.add(line.charAt(i)+"");
							}else {
								while(wordReadyQueue.poll()!=null) {}//清空队列

								while(Character.isLetterOrDigit(line.charAt(i++))) {
									if(i >= line.length()){
										break;
									}
								}
								groupnumber = 0;
								flag = true;
							}
	
						}
					}else {
						if(!Character.isLetterOrDigit(line.charAt(i))) {
							if(groupnumber!=0) {
								wordReadyQueue.add(line.charAt(i)+"");
							}
						}else {
							while(wordReadyQueue.poll()!=null) {}//清空队列
							while(Character.isLetterOrDigit(line.charAt(i++))) {
								if(i >= line.length()){
									break;
								}
							}
							groupnumber = 0;
							flag = true;
						}
					}
					if(groupnumber == groupNum) {//达到要求个数，加入散列表
						String wordGroup = "";
						String wordOfQueue = wordReadyQueue.poll();
						while(wordOfQueue != null) {
							wordGroup = wordGroup + wordOfQueue;
							wordOfQueue = wordReadyQueue.poll();
						}
						groupnumber = 0;
						Integer outValue = countMap.get(wordGroup.toString());
						if (null == outValue) {
						      outValue = 0;
						}
						if(isWeight) {
							outValue += weight;
						}else {
							outValue++;
						}
						countMap.put(wordGroup.toString(), outValue);		
						i = flagPosition;
					}
					if(!flag) {
						++i;
					}
				}
			}
			//求topK
			PriorityQueue<Word> topN = new PriorityQueue<>(topK, comp);
		    Iterator<Map.Entry<String, Integer>> iter = countMap.entrySet().iterator();
		    Map.Entry<String, Integer> entry;
		    while (iter.hasNext()) {
		      entry = iter.next();
		      if (topN.size() < topK) {
		        topN.offer(new Word(entry.getKey(), entry.getValue()));
		      } else {
		        // 如果当前数据比小顶堆的队头大，则加入，否则丢弃
		        if (topN.peek().getCountNum() < entry.getValue()) {
		          topN.poll();
		          topN.offer(new Word(entry.getKey(), entry.getValue()));
		        }else if(topN.peek().getCountNum() == entry.getValue()) {
			        if(topN.peek().getKey().compareTo(entry.getKey())>0){
			        	topN.poll();
				        topN.offer(new Word(entry.getKey(), entry.getValue()));
			        }
		        }
		        }
		    }
		    
		    int wordCount = countMap.size();
		    if(wordCount < topK) {
		    	result = new Word[(int) wordCount];
		    }else {
		    	result = new Word[topK];
		    }
		    topN.toArray(result);
		    //对top10单词排序
		    Arrays.sort(result, comp);

		} catch (IOException e) {
			System.out.println("文件不存在");
			e.printStackTrace();
		}
		return result;

	}
	/**
	 * Comparator接口实现小顶堆排序
	 */
	private Comparator<Word> comp =
			(o1, o2) -> {
				if (o1.getCountNum() > o2.getCountNum()) {
					return 1;
				} else if (o1.getCountNum() < o2.getCountNum()) {
					return -1;
				}else if(o1.getCountNum() == o2.getCountNum()) {
					if(o1.getKey().compareTo(o2.getKey())<0) {	//字典序排序
						return 1;
					}else {
						return -1;
					}
				}
				return 0;
			};
	
}
