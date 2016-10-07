/*
 * 准备工作：读停用词表，实体名，属性，类型
 * 读实体库，关系库
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultDocument;

public class PreTreatment {
	static ArrayList<String> stopWord_list = new ArrayList<String>();
	static ArrayList<String> entity_list = new ArrayList<String>();
	static ArrayList<String> attribute_list = new ArrayList<String>();
	static ArrayList<String> type_list = new ArrayList<String>();
	static HashMap<String, Integer> entiytWeight_map = new HashMap<>();

	SAXReader sax = new SAXReader();
	static Document entity_doc = new DefaultDocument();
	static Document relation_doc = new DefaultDocument();

	public void getEntityMap() {
		BufferedReader bre = null;
		String str = "";
		try {
			bre = new BufferedReader(new FileReader("lib/test/WordWeight.txt"));
			while ((str = bre.readLine()) != null) {
				String[] aa = str.split(",");
				int i = Integer.parseInt(aa[1]); // String转int
				entiytWeight_map.put(aa[0], i);
			}
			bre.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(map);
	}

	public static ArrayList<String> getList(String filePath) {
		BufferedReader bre = null;
		String str = "";
		ArrayList<String> alist = new ArrayList<String>();
		try {
			bre = new BufferedReader(new FileReader(filePath));
			while ((str = bre.readLine()) != null) {
				alist.add(str);
			}
			bre.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alist;
	}

	public void getKeyList() {
		entity_list = getList("lib/test/EntityName.txt");
		attribute_list = getList("lib/test/AttributeName.txt");
		//type_list = getList("lib/test/type.txt");
		stopWord_list = getList("lib/test/stopword.txt");
	}

	public void preTreatment() throws DocumentException {
		getKeyList();
		getEntityMap();
		// 通过read方法读取一个文件 转换成Document对象
		entity_doc = sax.read(new File("lib/test/EntityBase.xml"));
		relation_doc = sax.read(new File("lib/test/FatherChildRelation.xml"));
	}
}
