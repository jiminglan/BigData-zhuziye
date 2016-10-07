import java.util.ArrayList;

public class SearchEntityXML {

	ProcessingMode processingMode = new ProcessingMode(); // 检索模式
	GiveSimilar giveSimilar = new GiveSimilar();
	static ArrayList<String> answerList = new ArrayList<String>();

	public void search(String inputContent) throws Exception {
		answerList.clear();
		answerList.add(inputContent);

		// 多级问句分解
		String ps = ""; // 称述部分
		ArrayList<ArrayList<String>> questionList = new ArrayList<ArrayList<String>>(); // 问题列表
		ArrayList<ArrayList<String>> type_PMList = new ArrayList<ArrayList<String>>(); // 类型列表

		ArrayList<String> listEntity_ps = new ArrayList<>();
		ArrayList<String> listAttribute_ps = new ArrayList<>();

		// 切分用户的提问并判断用户的提问的各段是问句还是陈述句
		SplitMULT splitMULT = new SplitMULT();
		ps = splitMULT.split(inputContent);
		questionList = splitMULT.questionList; // 问题列表
		type_PMList = splitMULT.typeList; // 类型列表

		// ＊＊＊＊＊＊＊＊＊＊＊＊处理陈述部分＊＊＊＊＊＊＊＊＊＊＊＊
		if (!ps.equals("无")) {
			// 模糊匹配陈述部分实体和属性
			MatchingResult mResult_ps = new MatchingResult(); // 匹配
			mResult_ps.matchingResult(ps);
			listEntity_ps = mResult_ps.listEntity;
			listAttribute_ps = mResult_ps.listAttribute;
		}

		// ＊＊＊＊＊＊＊＊＊＊＊＊处理问句＊＊＊＊＊＊＊＊＊＊＊＊
		for (int i = 0; i < questionList.size(); i++) {
			ArrayList<String> strList = questionList.get(i); // 一个小问句的所有可能问题模式对应的去除提问词后的字符串
			ArrayList<String> tList = type_PMList.get(i);

			// 按照问题模式处理问句，tList中保存了一个问句的可能问题模式
			for (int j = 0; j < tList.size(); j++) {
				String type_PM = tList.get(j);
				String str = strList.get(j);
				System.out.println(type_PM + ":------------------------");

				// 模糊匹配每个小问句的实体和属性
				MatchingResult mResult = new MatchingResult(); // 匹配
				mResult.matchingResult(str);
				ArrayList<String> listEntity = mResult.listEntity;
				ArrayList<String> listAttribute = mResult.listAttribute;
				ArrayList<String> listValue = mResult.listValue;

				if (type_PM.equals("1-1")) { // 什么是、是什么
					if (!listEntity.isEmpty()) {
						if (!listAttribute.isEmpty()) { // 实体＋属性
							for (String attribute : listAttribute) {
								processingMode.entityValue(listEntity.get(0), attribute);
							}
						} else if (!listAttribute_ps.isEmpty()) {
							for (String attribute : listAttribute_ps) {
								processingMode.entityValue(listEntity.get(0), attribute);
							}
						} else {
							processingMode.entityValue(listEntity.get(0), "简介");
						}
					} else if (!listEntity_ps.isEmpty()) {
						if (!listAttribute.isEmpty()) { // 实体＋属性
							for (String attribute : listAttribute) {
								processingMode.entityValue(listEntity_ps.get(0), attribute);
							}
						} else if (!listAttribute_ps.isEmpty()) {
							for (String attribute : listAttribute_ps) {
								processingMode.entityValue(listEntity_ps.get(0), attribute);
							}
						} else {
							System.out.println("1-1，相似度搜索问答库");
							answerList.add(giveSimilar.readXML(inputContent));
						}
					} else {
						System.out.println("1-1，相似度搜索问答库");
						answerList.add(giveSimilar.readXML(inputContent));
					}
				} else if (type_PM.equals("1-2") || type_PM.equals("1-3") || type_PM.equals("1") || type_PM.equals("5")
						|| type_PM.equals("6-2") || type_PM.equals("7") || type_PM.equals("8") || type_PM.equals("11")
						|| type_PM.equals("2")) {
					if (!listEntity.isEmpty()) {
						if (!listAttribute.isEmpty()) { // 实体＋属性
							for (String attribute : listAttribute) {
								processingMode.entityValue(listEntity.get(0), attribute);
							}
						} else if (!listAttribute_ps.isEmpty()) {
							for (String attribute : listAttribute_ps) {
								processingMode.entityValue(listEntity.get(0), attribute);
							}
						} else {
							// 全文检索实体
							for(String s:listValue){
								answerList.add(s);
							}
							 //System.out.println(listValue);
						}
					} else if (!listEntity_ps.isEmpty()) {
						if (!listAttribute.isEmpty()) { // 实体＋属性
							for (String attribute : listAttribute) {
								processingMode.entityValue(listEntity_ps.get(0), attribute);
							}
						} else if (!listAttribute_ps.isEmpty()) {
							for (String attribute : listAttribute_ps) {
								processingMode.entityValue(listEntity_ps.get(0), attribute);
							}
						} else {
							System.out.println("1-1，相似度搜索问答库");
							answerList.add(giveSimilar.readXML(inputContent));
						}
					} else {
						System.out.println("1-1，相似度搜索问答库");
						answerList.add(giveSimilar.readXML(inputContent));
					}
				} else if (type_PM.equals("1-4")) { // 包含什么
					if (!listEntity.isEmpty()) {
						for (String entity : listEntity) {
							processingMode.entityValue(entity, "基础包");
						}
					} else if (!listEntity_ps.isEmpty()) {
						for (String entity : listEntity_ps) {
							processingMode.entityValue(entity, "基础包");
						}
					} else {
						System.out.println("1-4，相似度处理用户问句");
					}
				} else if (type_PM.equals("2-1")) { // how
					if (!listEntity.isEmpty()) {
						for (String entity : listEntity) {
							processingMode.entityValue(entity, "办理方式");
						}
					} else if (!listEntity_ps.isEmpty()) {
						for (String entity : listEntity_ps) {
							processingMode.entityValue(entity, "办理方式");
						}
					} else {
						System.out.println("2-1，相似度处理用户问句");
						answerList.add(giveSimilar.readXML(inputContent));
					}
				} else if (type_PM.equals("2-2")) { // how
					if (!listEntity.isEmpty()) {
						for (String entity : listEntity) {
							processingMode.entityValue(entity, "关闭方法");
						}
					} else if (!listEntity_ps.isEmpty()) {
						for (String entity : listEntity_ps) {
							processingMode.entityValue(entity, "关闭方法");
						}
					} else {
						System.out.println("2-2，其他情况待处理");
						answerList.add(giveSimilar.readXML(inputContent));
					}
				} else if (type_PM.equals("3")) { // where
					if (!listEntity.isEmpty()) {
						for (String entity : listEntity) {
							processingMode.entityValue(entity, "活动地区");
						}
					} else if (!listEntity_ps.isEmpty()) {
						for (String entity : listEntity_ps) {
							processingMode.entityValue(entity, "活动地区");
						}
					} else {
						System.out.println("3，其他情况待处理");
						answerList.add(giveSimilar.readXML(inputContent));
					}
				} else if (type_PM.equals("4")) { // when
					if (!listEntity.isEmpty()) {
						for (String entity : listEntity) {
							processingMode.entityValue(entity, "生效时间");
							processingMode.entityValue(entity, "结束时间");
						}
					} else if (!listEntity_ps.isEmpty()) {
						for (String entity : listEntity_ps) {
							processingMode.entityValue(entity, "生效时间");
							processingMode.entityValue(entity, "结束时间");
						}
					} else {
						System.out.println("4，其他情况待处理");
						answerList.add(giveSimilar.readXML(inputContent));
					}
				} else if (type_PM.equals("6-1")) { // which-人
					if (!listEntity.isEmpty()) {
						for (String entity : listEntity) {
							processingMode.entityValue(entity, "适用客户");
						}
					} else if (!listEntity_ps.isEmpty()) {
						for (String entity : listEntity_ps) {
							processingMode.entityValue(entity, "适用客户");
						}
					} else {
						System.out.println("6-1，其他情况待处理");
						answerList.add(giveSimilar.readXML(inputContent));
					}
				} else if (type_PM.equals("6-2-大于")) {
					// processingMode.elementMethod();
					if (!listAttribute.isEmpty()) {
						ArrayList<Float> num = ProcessingMode.getNum(str);
						if (!num.isEmpty()) {
							ArrayList<String> list = processingMode.entitySelect(listAttribute.get(0), num.get(0), 1);
							if (list.isEmpty()) {
								System.out.println("未找到满足条件的实体");
							} else {
								System.out.println(list);
							}
						} else {
							System.out.println("该问句中找不到比较值");
						}
					} else {
						System.out.println("该问句中找不到属性");
					}
				} else if (type_PM.equals("6-2-小于")) {
					if (!listAttribute.isEmpty()) {
						ArrayList<Float> num = ProcessingMode.getNum(str);
						if (!num.isEmpty()) {
							ArrayList<String> list = processingMode.entitySelect(listAttribute.get(0), num.get(0), 2);
							if (list.isEmpty()) {
								System.out.println("未找到满足条件的实体");
							} else {
								System.out.println(list);
							}
						} else {
							System.out.println("该问句中找不到比较值");
						}
					} else {
						System.out.println("该问句中找不到属性");
					}
				} else if (type_PM.equals("6-2-等于")) {
					if (!listAttribute.isEmpty()) {
						ArrayList<Float> num = ProcessingMode.getNum(str);
						if (!num.isEmpty()) {
							ArrayList<String> list = processingMode.entitySelect(listAttribute.get(0), num.get(0), 3);
							if (list.isEmpty()) {
								System.out.println("未找到满足条件的实体");
							} else {
								System.out.println(list);
							}
						} else {
							System.out.println("该问句中找不到比较值");
						}
					} else {
						System.out.println("该问句中找不到属性");
					}
				} else if (type_PM.equals("9")) { // howAbout
					if (!listEntity.isEmpty()) {
						for (String entity : listEntity) {
							processingMode.entityHowAbout(entity);
						}
					} else if (!listEntity_ps.isEmpty()) {
						for (String entity : listEntity) {
							processingMode.entityHowAbout(entity);
						}
					} else {
						System.out.println("9，其他情况待处理");
						answerList.add(giveSimilar.readXML(inputContent));
					}
				} else if (type_PM.equals("10")) { // compare*******待完善
					if (listEntity.size() > 1) {
						processingMode.entityCompare(listEntity);
					} else {
						System.out.println("10，其他情况待处理");
						answerList.add(giveSimilar.readXML(inputContent));
					}
				}
			}
		}
	}
}
