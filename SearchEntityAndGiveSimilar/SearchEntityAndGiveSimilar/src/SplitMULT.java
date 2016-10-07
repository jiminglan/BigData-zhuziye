/* 先把用户的多级问句进行切分
 * 再根据提问模式，判断每一个小句子是陈述句还是问句
 * 并记录每一个问句可能的提问模式和该提问模式对应的去除提问词后的剩余句子
 */

import java.io.IOException;
import java.util.ArrayList;

public class SplitMULT {
	String[] signal = { "，", "？", " ", "。"};
	ArrayList<String> all = new ArrayList<String>(); //切分
	String ps = ""; //保存用户提问中的陈述补充部分
	
	ArrayList<ArrayList<String>> questionList = new ArrayList<ArrayList<String>>(); //问题列表
	ArrayList<ArrayList<String>> typeList = new ArrayList<ArrayList<String>>(); //类型列表
	
	public String split(String inputContent) throws IOException {
		int temp = 0;
		for (int j = 0; j < inputContent.length(); j++) {
			for (String str : signal) {
				if (j == inputContent.length() - 1
						&& !inputContent.substring(inputContent.length() - 1, inputContent.length() ).equals(str)) {
					all.add(inputContent.substring(temp, inputContent.length() ));
					break;
				}else if (inputContent.substring(j, j + 1).equals(str)) {
					all.add(inputContent.substring(temp, j));
					temp = j + 1;
				}
			}
		}
		judge();
		return ps;
	}
	public void judge() throws IOException{
		for (String str : all){
			System.out.println(str);
			PatternMatches pm = new PatternMatches();
			pm.patternMatches(str);
			ArrayList<String> tList = new ArrayList<String>();
			ArrayList<String> strList = new ArrayList<String>();
			
			if(pm.result.get(0).equals("12")){
				ps = ps.concat(str);
				ps = ps.concat("  ");
			}else{
				for(int i=0;i<pm.result.size();i++){
					tList.add(pm.result.get(i)); //一个小问题的所有可能的问题模式
					i++;
					strList.add(pm.result.get(i));//一个小问题的所有可能的问题模式对应的去除提问词以后的字符串
				}
				typeList.add(tList); //把类型添加到类型列表
				questionList.add(strList);//把问题添加到问题列表
			}	
		}
		System.out.println(typeList);
		System.out.println(questionList);		
		if(ps.equals("")){
			ps = "无";
		}
		System.out.println("陈述部分："+ ps);
	}
}
