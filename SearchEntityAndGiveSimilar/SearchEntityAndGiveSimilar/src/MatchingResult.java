import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class MatchingResult {

	// 最佳匹配
	ArrayList<String> listEntity = new ArrayList<String>();// entity
	ArrayList<String> listAttribute = new ArrayList<String>();// attribute
	ArrayList<String> listValue = new ArrayList<String>();// value
	String strOdd = ""; // str为string去停用词去实体剩余的部分

	WeightMatching wMatching = new WeightMatching(); // 权重模糊匹配
	CosineMatching cMatching = new CosineMatching(); // 相似度模糊匹配
	HashMap<Integer, TreeMap<Double, String>> mapWE = new HashMap<Integer, TreeMap<Double, String>>(); // 权重模糊匹配实体
	HashMap<Integer, TreeMap<Double, String>> mapI = new HashMap<Integer, TreeMap<Double, String>>(); // 余弦相似度模糊匹配属性
	String matcher = null;
	Double similar = 0.0;
	TreeMap<Double, String> similarAndMatcher = new TreeMap<Double, String>();

	public void matchingResult(String string) throws Exception {

		ArrayList<String> listWE = new ArrayList<String>();
		ArrayList<String> listIA = new ArrayList<String>();

		string = string.toLowerCase(); // 所有大写字母转小写
		ArrayList<String> strList = DoToString.prepare(string); // strList为去完停用词的列表

		// ＊＊＊＊＊＊＊＊＊＊＊＊权重模糊匹配查找实体＊＊＊＊＊＊＊＊＊＊＊＊
		mapWE = wMatching.wMatching(strList);
		for (int i = 0; i < 1; i++) { // 选取最大权重相似度的实体
			similarAndMatcher = mapWE.get(i);
			similar = similarAndMatcher.firstKey().doubleValue();
			matcher = similarAndMatcher.get(similar);
			if (similar < 1) // 如果相似度小于1则放弃
				break;
			listWE.add(matcher);
			// System.out.println(similar + matcher);
		}
		System.out.println("权重模糊匹配实体" + listWE);

		if (!listWE.isEmpty()) {// 如果包含实体，去除用户问句中的实体部分
			ArrayList<String> matcherList = IKsegment.IKAnalysis(listWE.get(0));
			ArrayList<String> sList = new ArrayList<String>(); //去除用户问句中的实体部分后的部分
			for (String s : strList) { // 深克隆
				sList.add(s);
			}
			for (String s1 : matcherList) {
				for (String s2 : strList) {
					if (s1.equals(s2)) {
						sList.remove(s2);
					}
				}
			}
			System.out.println("去除用户问句中的实体部分后：" + sList);

			if (sList.isEmpty()) { // 用户问句除了提问模式和实体以外为：空，则直接给出实体的简介
				listIA.add("简介");
				System.out.println("用户问句除了提问模式和实体以外为：空，则直接给出实体的简介");
			} else {// 用户问句除了提问模式和实体以外不为空，则模糊匹配属性
				ArrayList<String> attrList = new ArrayList<String>();
				ArrayList<String> valueList = new ArrayList<String>();
				ProcessingMode p = new ProcessingMode();
				p.getAttrValueList(listWE.get(0), attrList, valueList);

				// ＊＊＊＊＊＊＊＊＊＊＊＊相似模糊匹配查找属性----小单元
				for (String s : sList) {
					strOdd = strOdd.concat(s);
					mapI = cMatching.iMatching(s, attrList);
					similarAndMatcher = mapI.get(0);
					similar = similarAndMatcher.firstKey().doubleValue();
					matcher = similarAndMatcher.get(similar);
					System.out.println("相似模糊匹配查找属性----小单元：" + similar + matcher);
					if (similar > 0.4) // 如果相似度小于0.4则放弃
						listIA.add(matcher);
				}

				// ＊＊＊＊＊＊＊＊＊＊＊＊相似模糊匹配查找属性----大单元
				mapI = cMatching.iMatching(strOdd, attrList);
				similarAndMatcher = mapI.get(0);
				similar = similarAndMatcher.firstKey().doubleValue();
				matcher = similarAndMatcher.get(similar);
				System.out.println("相似模糊匹配查找属性----大单元：" + similar + matcher);
				if (similar > 0.4) // 如果相似度小于0.4则放弃
					listIA.add(matcher);

				System.out.println("模糊匹配属性" + listIA);

				if (listIA.isEmpty()) { // 如果模糊匹配属性失败，则模糊匹配属性值
					// System.out.println(strOdd);
					// System.out.println(valueList);
					matchingVaule(strOdd, valueList);
				}
			}
		} else {
			System.out.println("未找到实体");
		}

		// ＊＊＊＊＊＊＊＊＊＊＊＊最后选定效果最好的匹配方式＊＊＊＊＊＊＊＊＊＊＊＊
		listEntity = listWE;
		listAttribute = listIA;
	}

	public void matchingVaule(String str, ArrayList<String> valueList) throws Exception {
		mapI = cMatching.iMatching(str, valueList);
		for (int i = 0; i < 3; i++) { // 选取相似度最大的3个属性值
			similarAndMatcher = mapI.get(i);
			similar = similarAndMatcher.firstKey().doubleValue();
			matcher = similarAndMatcher.get(similar);
			
			 if (!similar.isNaN()){
			 listValue.add(matcher);
			 System.out.println("相似模糊匹配查找属性值：" + similar + matcher);
			 }
		}
		System.out.println("相似模糊匹配查找属性值：" + listValue);
	}
}
