
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GiveSimilar {

	private static List<File> files = new ArrayList<File>();

	/*
	 * 使用dom4j解析XML，比较输入字符串和得到的各段的相似度 ， 排序输出 str为输入的要比较的字符串 返回比较后的map
	 * 存放比较的double结果的key和对应的问题段落的值
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public Map<Double, Map<String, String>> readXml(String str) throws Exception {
		// 创建saxReader对象
		SAXReader reader = new SAXReader();
		// 通过read方法读取一个文件 转换成Document对象
		String directoryPath = "lib";
		File file = new File(directoryPath);
		getAllFile(file);
		List<Element> nodes = new ArrayList<Element>();
		List<Element> nodeAns = new ArrayList<Element>();
		List<Element> nodeExcel = new ArrayList<Element>();// 读取e://xml文件夹下所有xml
		for (File f : files) {
			// 打印每个xml文件名字
			// System.out.println(f.getName());
			Document document = reader.read(f);
			// 获取根节点
			Element root = document.getRootElement();
			// 获取根节点下的所有问题节点，并遍历
			nodes.addAll(root.selectNodes("//question"));
			nodeAns.addAll(root.selectNodes("//ans"));
			// excel
			nodeExcel.addAll(root.selectNodes("//row"));
		}

		// 实例化CosineSimilarAlgorithm 余弦定理比较
		CosineSimilarAlgorithm similar = new CosineSimilarAlgorithm();
		Map<Double, Map<String, String>> map = new HashMap<Double, Map<String, String>>();
		// word
		for (int i = 0; i < nodes.size(); i++) {
			Element elm = (Element) nodes.get(i);
			Element elmAns = (Element) nodeAns.get(i);
			// 获取节点文字,text是问题 textAns是答案
			String text = elm.getText();
			String textAns = elmAns.getText();

			Map<String, String> mapAns = new HashMap<String, String>();
			mapAns.put(text, textAns);

			ArrayList<String> strArray = new ArrayList<String>(); // 建立字符串数组存储word中的子字符串
			int vcount = 0;
			for (int j = 1; j < textAns.length(); j++) {// 以word文档中一段话的长度为循环次数
				// 如果在这段话中出现了“，”、“。”，就拆分字符串
				// 按照一个一个字符逐个找是否有，和。找到后把这一个小段放到数组中，再另j指向标点符号下一个字符
				if (textAns.substring(j - 1, j).equals("。") || textAns.substring(j - 1, j).equals("，")) {
					strArray.add(textAns.substring(vcount, j - 1));
					vcount = j;
				}
			}
			ArrayList<Double> strA = new ArrayList<Double>();// 建立字符串存储每个字符串匹配相似度算法后的值
			strA.add(similar.getSimilarity(str, text)); // 匹配问题的相似度值
			for (int k = 0; k < strArray.size(); k++) { // 匹配答案的相似度值
				Double result = similar.getSimilarity(str, strArray.get(k));
				strA.add(result);
			}
			// System.out.println(strArray.size());
			for (int x = 1; x < strA.size(); x++) { // 找到所有相似度值中最大的那个值，放在数组最后
				Double a = strA.get(x - 1);
				Double b = strA.get(x);
				if (a > b) {
					strA.set(x, a);
				}
			}
			Double result = strA.get(strA.size() - 1); // 把数组最后的那个值给result
			// System.out.println(result);
			/*
			 * String1 st = new String1(); String res = st.strOptimize(str);
			 * Double result = similar.getSimilarity(res, textAns);
			 */
			map.put(result, mapAns);
		}
		// excel
		for (int i = 0; i < nodeExcel.size(); i++) {
			Element elm = (Element) nodeExcel.get(i);
			String row = "";
			List<Element> cellList = elm.elements("cell");
			// cellList.addAll(elm.elements("line-txt"));
			for (Element cell : cellList) {
				row += cell.getText();
			}
			if ("".equals(row))
				continue;
			for (Element cell : cellList) {
				Map<String, String> mapAns = new HashMap<String, String>();
				if ("".equals(cell.getText()))
					continue;
				mapAns.put(cell.getText(), row);
				StringOptimize st = new StringOptimize();
				String res = st.strOptimize(str);
				Double result = similar.getSimilarity(res, cell.getText());
				if (!"NAN".equals(result) && result > 0.2)
					map.put(result, mapAns);
			}
		}
		return map;
	}

	public static void getAllFile(File f) {
		// 是否是文件夹
		if (f.isDirectory()) {
			// 获得该文件夹下所有子文件和子文件夹
			File[] f1 = f.listFiles();
			// 循环处理每个对象
			int len = f1.length;
			for (int i = 0; i < len; i++) {
				// 递归调用，处理每个文件对象
				getAllFile(f1[i]);
			}
		} else if (f.isFile() && f.getName().endsWith(".xml")) {
			files.add(f);
		}
	}

	@SuppressWarnings("resource")
	public String readXML(String sta) throws Exception {
		GiveSimilar readXML = new GiveSimilar();
		Map<Double, Map<String, String>> resMap = readXML.readXml(sta);
		Iterator<Double> keys = resMap.keySet().iterator();
		List<Double> listd = new ArrayList<>();
		while (keys.hasNext()) {
			listd.add(keys.next());
		}
		Collections.sort(listd);// 排序(升序)
		String result = "";
		
		for (int i = listd.size() - 1; i >= listd.size() - 1; i--) {
			Map<String, String> queAns = resMap.get(listd.get(i));
			for (String key : queAns.keySet()) {
				result = queAns.get(key);
			}
		}	
		System.out.println(result);
		return result;
	}

}
