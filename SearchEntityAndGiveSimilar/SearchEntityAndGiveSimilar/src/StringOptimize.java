

public class StringOptimize {
	public String strOptimize(String str) {// 字符串紧致化方法
		String[] toreplace = { "的", "这", "和", "“", "”", "‘", "’", "。", "，", "；", "：", "？", "！", "……", "—", "～", "（",
				"）", "《", "》", "\"", "\"", "'", "'", ".", ",", ";", ":", "?", "!", "…", "-", "~", "(", ")", "<", ">",
				" " };// 将需要除去冗余字符添加至此
		for (int i = 0; i < toreplace.length; i++) {
			str = str.replace(toreplace[i], "");// 逐个剔除冗余字符
		}

		/*
		 * int count = 0; for (int i = 0; i < str.length(); i++) { if
		 * (str.substring(i, i + 1).equals("。")) { ArrayList<String> al = new
		 * ArrayList<>(); String a = str.substring(count, i); count = i + 1;
		 * al.add(a); }
		 * 
		 * }
		 */
		return str;
	}

}
