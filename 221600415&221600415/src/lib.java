import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class lib {
	String regex="\\s+|[^A-Za-z0-9]+";//匹配分隔符，空格和非字母数字
	String wordRegex="[A-Za-z]{4,}[A-Za-z0-9]*";
	/**
	 * 统计字符数,行数,单词数在论文里
	 * @param inputPath
	 * @return
	 */
	public Map<String,Integer> countLettersInPapers(String inputPath)throws Exception {
		FileInputStream fileInputStream = new FileInputStream(new File(inputPath));
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
		String itemStr;
		int charCount=0,lines=0,wordCount=0;
		while ((itemStr = br.readLine()) != null) {
			if(itemStr.startsWith("Title:")||itemStr.startsWith("Abstract:")){//是标题或摘要
				++lines;
				if(itemStr.startsWith("Title:")){
					++charCount;
				}
				String valiStr=itemStr.substring(itemStr.indexOf(':')+2);//有效字符串
				int length=valiStr.length();
				for(int i=0;i<length;++i){
					if(i+1<length){
						if(valiStr.charAt(i)==13&&valiStr.charAt(i+1)==10){
							++i;
							++charCount;
							continue;
						}
					}
					char ch=valiStr.charAt(i);
					if(ch>=0&&ch<=127){
						++charCount;
					}
				}
				String[] strList=valiStr.split(regex);
				for(String item:strList){
					if(item.matches(wordRegex)){
						++wordCount;
					}
				}
			}
		}
		br.close();
		Map<String,Integer> map=new HashMap<>();
		map.put("charCount",charCount);
		map.put("lines",lines);
		map.put("wordCount",wordCount);
		return map;
	}


	/**
	 * 转小写包含数字的字符串
	 * @param s
	 * @return
	 */
	public String toLower(String s){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<s.length();++i){
			char ch=s.charAt(i);
			if(ch>=65&&ch<=90){
				ch+=32;
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * 统计前十权重单词
	 * @param inputPath
	 * @return
	 */
	public Map<String,Integer> getTopTenWeightWords(String inputPath,int arrayNum,boolean isWeight)throws Exception{
		Map<String,Integer> map=new HashMap<>();
		FileInputStream fileInputStream=new FileInputStream(new File(inputPath));
		InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
		String itemStr;
		while((itemStr=br.readLine())!=null){
			if(itemStr.startsWith("Title:")||itemStr.startsWith("Abstract:")){
				String[] strList=itemStr.split(regex);
				int weight=1;
				if(strList[0].equals("Title")){
					if(isWeight){
						weight=10;
					}
				}
				else if(strList[0].equals("Abstract"))
					weight=1;
				for(int i=1;i<strList.length;++i){
					String key=strList[i];
					if(key.matches(wordRegex)){//是一个单词
						key=toLower(key);
						map.put(key,map.getOrDefault(key,0)+weight);
					}
				}
			}
		}
		br.close();
		return getOrderTopTenMap(map,arrayNum);
	}
	/**
	 * 统计前十的数组
	 * @param inputPath
	 * @return
	 * @throws Exception
	 */
	public Map<String,Integer> getTopTenWordarray(String inputPath,int num,int lines,boolean isWeight)throws Exception{
		Map<String,Integer> map=new HashMap<>();
		FileInputStream fileInputStream=new FileInputStream(new File(inputPath));
		InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
		String itemStr;
		while((itemStr=br.readLine())!=null){
			if(itemStr.startsWith("Title:")||itemStr.startsWith("Abstract:")){
				String[] strList=itemStr.split(regex);
				int weight=1;
				if(strList[0].equals("Title")){
					if(isWeight){
						weight=10;
					}
					for(int i=1;i<strList.length;++i){
						boolean isArray=true;
						StringBuilder sb=new StringBuilder();
						int j;
						for(j=i;j<i+num&&j<strList.length;++j){
							String key=strList[j];
							key=toLower(key);
							if(!key.matches(wordRegex)){//遇到无效单词
								i=j;
								isArray=false;
								break;
							}
							else{
								if(sb.length()>0){
									sb.append(" "+key);
								}
								else{
									sb.append(key);
								}
							}
						}
						if(isArray&&j==num+i) {
							map.put(sb.toString(), map.getOrDefault(sb.toString(), 0) + weight);
						}
					}
				}
				else if(strList[0].equals("Abstract")){
					weight=1;
					for(int i=1;i<strList.length;++i){
						boolean isArray=true;
						StringBuilder sb=new StringBuilder();
						int j;
						for(j=i;j<num+i&&j<strList.length;++j){
							String key=strList[j];
							key=toLower(key);
							if(!key.matches(wordRegex)){//遇到无效单词
								i=j;
								isArray=false;
								break;
							}
							else{
								if(sb.length()>0){
									sb.append(" "+key);
								}
								else{
									sb.append(key);
								}
							}
						}
						if(isArray&&j==num+i){
							map.put(sb.toString(), map.getOrDefault(sb.toString(), 0) + weight);
						}
					}
				}
			}
		}
		br.close();
		return getOrderTopTenMap(map,lines);
	}

	/**
	 * map按频率和字典序排序
	 * @param m
	 * @return
	 */
	public Map<String,Integer>getOrderTopTenMap(Map<String, Integer>m,Integer arrayNum){
		Map<String, Integer> map=sortMapByValue(m);//通过Value排序;
		int order=0;
		int numInten=0;
		for(Integer item:map.values()){
			++order;
			if(order==arrayNum){
				numInten=item;
			}
		}

		Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Integer> entry = it.next();
			if(entry.getValue()<numInten)
				it.remove();//使用迭代器的remove()方法删除元素
		}

		Set<Integer> valueSet=new TreeSet<>();//有序
		for(String key:map.keySet()){//看看有几种值结果
			valueSet.add(map.get(key));
		}

		Map<Integer,Integer> sameMap=new HashMap<>();//每种结果有多少个
		for(Integer value:valueSet){
			for(String key:map.keySet()){
				if(map.get(key)==value){
					sameMap.put(value,sameMap.getOrDefault(value,0)+1);
				}
			}
		}
		//相同频率拆段
		List<Map<String,Integer> > mapList=new ArrayList<>();
		for(Integer item:valueSet){
			Map<String,Integer> mapItem=new HashMap<>();
			for(String s:map.keySet()){
				if(map.get(s)==item){
					mapItem.put(s,map.get(s));
				}
			}
			mapList.add(mapItem);
		}

		List<Map<String,Integer> > mapList2=new ArrayList<>();
		//频率相同排序
		for(int i=mapList.size()-1;i>=0;--i){//set升序，频率最高的段在后面
			mapList2.add(sortMapByKey(mapList.get(i)));
		}
		Map<String,Integer>result=new LinkedHashMap<>();
		int num=0;
		for(int i=0;i<mapList2.size();++i){
			for(String key: mapList2.get(i).keySet()){
				++num;
				if(num<=arrayNum)
					result.put(key,mapList2.get(i).get(key));
				else
					break;
			}
		}
		return result;
	}


	/**
	 * 使用 Map按key进行排序
	 * @param map
	 * @return
	 */
	public Map<String, Integer> sortMapByKey(Map<String, Integer> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Integer> sortMap = new TreeMap<>(new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}

	/**
	 * 使用 Map按value进行排序
	 * @param
	 * @return
	 */
	public Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, Integer> sortedMap = new LinkedHashMap<>();
		List<Map.Entry<String, Integer>> entryList = new ArrayList<>(oriMap.entrySet());
		Collections.sort(entryList, new MapValueComparator());

		Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
		Map.Entry<String, Integer> tmpEntry;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}
	class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {
		@Override
		public int compare(Map.Entry<String, Integer> me1, Map.Entry<String, Integer> me2) {
			return -1*me1.getValue().compareTo(me2.getValue());
		}
	}
	class MapKeyComparator implements Comparator<String>{
		@Override
		public int compare(String str1, String str2) {
			return str1.compareTo(str2);
		}
	}
}