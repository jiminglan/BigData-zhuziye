import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;


public class ProcessingMode {
	boolean getValue = false;
	String s = "";

	private void recGetXmlElementValue(Element ele) {
		// 如果是entity节点，输出entity的name
		if (ele.getName().equals("entity")) {
			Attribute attr = ele.attribute("name");
			s = attr.getText().concat("-------------------");
			SearchEntityXML.answerList.add(s);
			//System.out.println(name + "-------------------");
		}
		List eleList = ele.elements();
		if (eleList.size() == 0) { // 处理叶子节点，读取属性值
			s = ele.getName().concat(": ").concat(ele.getText());
			SearchEntityXML.answerList.add(s);
			//System.out.println(ele.getName() + ": " + ele.getText());
		} else { // 非叶子节点，则递归
			for (Iterator<org.dom4j.Element> iter = eleList.iterator(); iter.hasNext();) {
				Element element = iter.next();
				recGetXmlElementValue(element);
			}
		}
	}

	private void getElementValue(Element ele, String inputAttribute) { // 输出某个属性或者某个属性的所有子属性
		List eleList = ele.elements();
		if (eleList.size() == 0) {
		} else { // 处理非叶子节点
			if (ele.element(inputAttribute) != null) {
				Element att = ele.element(inputAttribute);
				recGetXmlElementValue(att);
			} else {
				for (Iterator<org.dom4j.Element> iter = eleList.iterator(); iter.hasNext();) {
					Element element = iter.next();
					getElementValue(element, inputAttribute);
				}
			}
		}
	}

	private void recGetAttrValue(Element ele, ArrayList<String> attrList, ArrayList<String> valueList) { // 实体对比时调用
		List eleList = ele.elements();
		if (eleList.size() == 0) { // 处理叶子节点，读取属性值
			if (ele.getText().length() > 0) {
				attrList.add(ele.getName());
				valueList.add(ele.getText());
			}
		} else { // 非叶子节点，则递归
			for (Iterator<org.dom4j.Element> iter = eleList.iterator(); iter.hasNext();) {
				Element element = iter.next();
				recGetAttrValue(element, attrList, valueList);
			}
		}
	}

	public void elementMethod() throws DocumentException { // 遍历xml找出各个节点的子节点和父节点
		Element root = PreTreatment.entity_doc.getRootElement();
		recGetXmlElementValue(root);
	}

	private Element entityFind(String inputEntity) {
		// 选取单个节点
		Element entity = (Element) PreTreatment.entity_doc.selectSingleNode("//entity[@name='" + inputEntity + "']");
		if (entity == null) // 实体查找失败，给出提示
		{
			s = "寻找实体失败：";
			SearchEntityXML.answerList.add(s.concat(inputEntity));
			// System.out.println("寻找实体“" + inputEntity + "”失败");
		}
		return entity;
	}

	private Element typeFind(String inputNode) {
		// 选取单个节点
		Element type = (Element) PreTreatment.relation_doc.selectSingleNode("//type[@name='" + inputNode + "']");
		if (type == null) // 节点查找失败，给出提示
		{
			s = "寻找节点失败：";
			SearchEntityXML.answerList.add(s.concat(inputNode));
			// System.out.println("寻找节点“" + inputNode + "”失败");
		}
		return type;
	}

	public void getAttrValueList(String inputEntity, ArrayList<String> attrList, ArrayList<String> valueList) { // 找到某个entity，输出它的所有属性和属性值
		Element entity = entityFind(inputEntity);
		if (entity != null)
			recGetAttrValue(entity, attrList, valueList);// 读取entity下的多级属性
		/*
		 * 属性不分层级时，循环输出属性和属性值 List<Element> attributeList = entity.elements();
		 * for (Element attribute : attributeList) {
		 * System.out.println(attribute.getName() + ": " + attribute.getText());
		 * }
		 */
	}

	public void entityValue(String inputEntity, String inputAttribute) { // 输出某个entity的某个属性
		Element entity = entityFind(inputEntity);
		if (entity != null)
			getElementValue(entity, inputAttribute);
	}

	public void typeValue(String inputNode, String inputAttribute) { // 输出关系库中某个type的某个属性
		Element type = typeFind(inputNode);
		if (type != null)
			getElementValue(type, inputAttribute);
	}

	public ArrayList<String> findFoC(String inputNode, String str) { // str为child或father
		ArrayList<String> list = new ArrayList<>();
		Element type = typeFind(inputNode);
		if (type != null) {
			// 获取element这个元素节点中，所有子节点名称为father或child节点
			List<Element> attributeList = type.elements();
			if (str.equals("father")) {
				for (Element attribute : attributeList) {
					if (attribute.getName().equals("father"))
						list.add(attribute.getText());
				}
			} else {
				for (Element attribute : attributeList) {
					if (attribute.getName().equals("child"))
						list.add(attribute.getText());
				}
			}
			// System.out.println(fatherList);
			// System.out.println(childList);
		}
		return list;
	}

	public void entityCompare(ArrayList<String> listKE) {
		// 查找实体的类型
		ArrayList<String> fatherList1 = findFoC(listKE.get(0), "father");
		ArrayList<String> fatherList2 = findFoC(listKE.get(1), "father");

		Element entity1 = entityFind(listKE.get(0));
		Element entity2 = entityFind(listKE.get(1));

		if (entity1 != null && entity2 != null) {
			ArrayList<String> sameFatherList = new ArrayList<>();
			for (String father1 : fatherList1) {
				for (String father2 : fatherList2) {
					if (father1.equals(father2)) { // 若要比较的实体属于同一个father
						s = "两个实体属于同一个类型：";
						SearchEntityXML.answerList.add(s.concat(father1));
						s = "------".concat(listKE.get(0)).concat("------").concat(listKE.get(1));
						SearchEntityXML.answerList.add(s);
						// System.out.println("两个实体属于同一个类型：" + father1);
						// System.out.println("------" + listKE.get(0) +
						// "---------" + listKE.get(1) + "-------------------");
						sameFatherList.add(father1);
						ArrayList<Integer> printedList1 = new ArrayList<Integer>(); // 记录实体1中已经打印过的属性
						ArrayList<Integer> printedList2 = new ArrayList<Integer>(); // 记录实体2中已经打印过的属性
						ArrayList<String> attrList1 = new ArrayList<String>(); // 记录实体1中的属性
						ArrayList<String> attrList2 = new ArrayList<String>(); // 记录实体2中的属性
						ArrayList<String> valueList1 = new ArrayList<String>(); // 记录实体1中的属性值
						ArrayList<String> valueList2 = new ArrayList<String>(); // 记录实体2中的属性值
						recGetAttrValue(entity1, attrList1, valueList1);
						recGetAttrValue(entity2, attrList2, valueList2);

						// 如果两个属性相同，则输出比较
						for (int i = 0; i < attrList1.size(); i++) {
							for (int j = 0; j < attrList2.size(); j++) {
								if (attrList1.get(i).equals(attrList2.get(j))) {
									s = attrList1.get(i).concat(":\t\t\t").concat(valueList1.get(i)).concat("\t\t\t")
											.concat(valueList2.get(j));
									SearchEntityXML.answerList.add(s);
									// System.out.println(attrList1.get(i) +
									// ":\t\t\t" + valueList1.get(i) + "\t\t\t"+
									// valueList2.get(j));
									printedList1.add(i);
									printedList2.add(j);
								}
							}
						}
						// 输出实体1单独拥有的属性和属性值
						for (int i = 0; i < attrList1.size(); i++) {
							int temp = 0;
							for (int j = 0; j < printedList1.size(); j++) {
								if (i == printedList1.get(j)) {
									temp++;
								}
							}
							if (temp == 0) {
								s = attrList1.get(i).concat(":\t\t\t").concat(valueList1.get(i));
								SearchEntityXML.answerList.add(s);
								//System.out.println(attrList1.get(i) + ":\t\t\t" + valueList1.get(i));
							}
						}
						// 输出实体2单独拥有的属性和属性值
						for (int i = 0; i < attrList2.size(); i++) {
							int temp = 0;
							for (int j = 0; j < printedList2.size(); j++) {
								if (i == printedList2.get(j)) {
									temp++;
								}
							}
							if (temp == 0) {
								s = attrList2.get(i).concat(":\t\t\t").concat(valueList2.get(i));
								SearchEntityXML.answerList.add(s);
								//System.out.println(attrList2.get(i) + ":\t\t\t" + valueList2.get(i));
							}
						}
					}
				}
			}
			if (sameFatherList.isEmpty()) { // 若要比较的实体不属于同一个father
				ArrayList<String> gFatherList1 = new ArrayList<>();
				ArrayList<String> gFatherList2 = new ArrayList<>();
				ArrayList<String> useFatherList1 = new ArrayList<>();
				ArrayList<String> useFatherList2 = new ArrayList<>();

				for (String father : fatherList1) {
					gFatherList1.addAll(findFoC(father, "father"));
				}
				for (String father : fatherList2) {
					gFatherList2.addAll(findFoC(father, "father"));
				}
				boolean sameGFather = false;
				for (String gFather1 : gFatherList1) {
					for (String gFather2 : gFatherList1) {
						if (gFather1.equals(gFather2)) { // 但是两个father是同一类
							sameGFather = true;
							for (String str : findFoC(gFather1, "child")) {
								for (String father : fatherList1) {
									if (str.equals(father)) {
										useFatherList1.add(father);
									}
								}
							}
							for (String str : findFoC(gFather2, "child")) {
								for (String father : fatherList2) {
									if (str.equals(father)) {
										useFatherList2.add(father);
									}
								}
							}
							s = listKE.get(0).concat("的类型：").concat(useFatherList1.toString());
							SearchEntityXML.answerList.add(s);
							//System.out.println(listKE.get(0) + "的类型：" + useFatherList1);
							
							s = listKE.get(1).concat("的类型：").concat(useFatherList2.toString());
							SearchEntityXML.answerList.add(s);
							//System.out.println(listKE.get(1) + "的类型：" + useFatherList2);
							
							s = "总类型：".concat(gFather1);
							SearchEntityXML.answerList.add(s);
							//System.out.println("总类型：" + gFather1);

							for (String father : useFatherList1) {
								s = father.concat("的child：");
								SearchEntityXML.answerList.add(s);
								//System.out.println(father + "的child：");
								for (String str : findFoC(father, "child")) {
									s = "-----".concat(str);
									SearchEntityXML.answerList.add(s);
									//System.out.println("-----" + str);
								}
							}
							for (String father : useFatherList2) {
								s = father.concat("的child：");
								SearchEntityXML.answerList.add(s);
								//System.out.println(father + "的child：");
								for (String str : findFoC(father, "child")) {
									s = "-----".concat(str);
									SearchEntityXML.answerList.add(s);
									//System.out.println("-----" + str);
								}
							}
						}
					}
				}
				if (sameGFather == false) {
					s = "您输入的实体不属于一类，它们的介绍信息为：";
					SearchEntityXML.answerList.add(s);
					//System.out.println("您输入的实体不属于一类，它们的介绍信息为：");
					for (String entity : listKE) {
						entityValue(entity, "产品介绍");
					}
				}
			}
		}
	}

	public void entityHowAbout(String inputEntity) {
		for (String father : findFoC(inputEntity, "father")) {
			s = inputEntity.concat("属于类别：").concat(father);
			SearchEntityXML.answerList.add(s);
			//System.out.println(inputEntity + "属于类别：" + father);
			
			s = "类别：".concat(father).concat("包含的实体有：");
			SearchEntityXML.answerList.add(s);
			//System.out.println("类别：" + father + "包含的实体有：");
			
			for (String entity : findFoC(father, "child")) {
				s = "-------".concat(entity);
				SearchEntityXML.answerList.add(s);
				//System.out.println("-------" + entity);
				entityValue(entity, "简介");
			}
		}
	}

	private void recEntitySelect(Element ele, String inputAttribute, float num, int t) {// 选取满足某个条件的实体时调用
		List eleList = ele.elements();
		if (eleList.size() == 0) { // 处理叶子节点，读取属性值
			if (ele.getName().equals(inputAttribute)) {
				String value = ele.getText();
				float value_num = getNum(value).get(0);
				if (t == 1) {
					if (value_num >= num) {
						getValue = true;
					}
				} else if (t == 2) {
					if (value_num <= num) {
						getValue = true;
					}
				} else {
					if (value_num == num) {
						getValue = true;
					}
				}
			}
		} else { // 非叶子节点，则递归
			for (Iterator<org.dom4j.Element> iter = eleList.iterator(); iter.hasNext();) {
				Element element = iter.next();
				recEntitySelect(element, inputAttribute, num, t);
			}
		}
	}

	public ArrayList<String> entitySelect(String inputAttribute, float num, int t) { // 选取满足某个条件的实体
		ArrayList<String> selectList = new ArrayList<>();
		List<Element> entityList = PreTreatment.entity_doc.selectNodes("//entity");
		for (Element entity : entityList) {
			recEntitySelect(entity, inputAttribute, num, t);
			if (getValue == true) {
				Attribute a = entity.attribute("name");
				selectList.add(a.getText());
			}
		}
		return selectList;
		// List entitys = root.selectNodes("//entity[月租<20]"); Xpath方法
	}


	public static ArrayList<Float> getNum(String str) {
		ArrayList<Float> numList = new ArrayList<Float>();
		for (String sss : str.replaceAll("[^0-9＋^0-9.^0-9]", ",").split(",")) {
			if (!sss.isEmpty())
				numList.add(Float.valueOf(sss));
		}
		// System.out.println(numList);
		return numList;
	}
}
