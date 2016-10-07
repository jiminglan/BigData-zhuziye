
public class SearchEntityAndGiveSimilar {

	public static void main(String[] args) throws Exception {

		new PreTreatment().preTreatment(); // 读入实体名表、属性表、类别表、实体名权重表、实体库、关系库

		String inputContent = "iFree卡黄金版是什么？";
		// 实体库检索
		SearchEntityXML searchEntityXML = new SearchEntityXML();
		searchEntityXML.search(inputContent);
		System.out.println(SearchEntityXML.answerList);

		// 查找问题库的相似问题
		// GiveSimilar giveSimilar = new GiveSimilar();
		// giveSimilar.readXML(inputContent);

		// **********从文本读入查询问题**********
		// ReadTxt.readtxt();
	}
}
