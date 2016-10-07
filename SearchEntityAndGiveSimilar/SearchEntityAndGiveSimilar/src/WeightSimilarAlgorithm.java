import java.util.ArrayList;

public class WeightSimilarAlgorithm {

	// 获取某个字符串中的权重值
	public static Double getSimilarity(ArrayList<String> strList, ArrayList<String> entityList) {
		Double sum = 0.0;
		Double sum_ask = 0.0;
		Double similar = 0.0;

		for (String str : entityList) {
			if (PreTreatment.entiytWeight_map.containsKey(str)) {
				sum = sum + PreTreatment.entiytWeight_map.get(str).doubleValue();
				for (String s : strList) {
					if (s.equals(str)) {
						sum_ask = sum_ask + PreTreatment.entiytWeight_map.get(str).doubleValue();
					}
				}
			}else{
				sum++;
				for (String s : strList) {
					if (s.equals(str)) {
						sum_ask++;
					}
				}
			}
		}
		// System.out.println(string_list);
		// System.out.println(sum_ask);
		// System.out.println(sum);
		try {
			if (sum != 0) {
				similar = sum_ask / sum;
				similar += sum_ask / 10;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		// System.out.println(similar);
		return similar;
	}

	public static Double getAttrSimilarity(ArrayList<String> strList, ArrayList<String> attrList) {
		Double sum = 0.0;
		Double sum_ask = 0.0;
		Double similar = 0.0;

		for (String str : attrList) {
			sum++;
			for (String s : strList) {
				if (s.equals(str)) {
					sum_ask++;
				}
			}
		}
		try {
			if (sum != 0) {
				similar = sum_ask / sum;
				similar += sum_ask / 10;
				// similar = similar * Math.log(attr_length);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		// System.out.println(sum_ask);
		// System.out.println(sum);
		// System.out.println(similar);
		return similar;
	}
}
