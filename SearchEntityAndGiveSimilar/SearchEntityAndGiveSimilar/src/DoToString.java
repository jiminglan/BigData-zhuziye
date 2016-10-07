import java.util.ArrayList;
import java.util.ListIterator;

public class DoToString {
	
	public static ArrayList<String> prepare(String string) throws Exception {
		ArrayList<String> ikList = IKsegment.IKAnalysis(string); // 分词
		ArrayList<String> strList = removeStopword(ikList); // 去停用词
		return strList;
	}

	private static ArrayList<String> removeStopword(ArrayList<String> ikList) {
		// 去除停用词
		String str = null;
		for (ListIterator<String> li = ikList.listIterator(); li.hasNext();) {
			str = li.next();
			// System.out.println(str);

			for (ListIterator<String> li2 = PreTreatment.stopWord_list.listIterator(); li2.hasNext();) {
				String s = li2.next();
				if (str.equals(s)) {
					// System.out.println(obj.toString());
					li.remove();
				}
			}
		}
		return ikList;
	}
}
