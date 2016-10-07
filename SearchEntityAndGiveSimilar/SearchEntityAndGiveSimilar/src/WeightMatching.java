
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WeightMatching {

	public Map<Double, String> getMap(ArrayList<String> strList) throws Exception {
		
		
		// IdentityHashMap，可以保持相同的key值
		Map<Double, String> map = new IdentityHashMap<Double, String>();
		Double similar;
		
		for (String entity : PreTreatment.entity_list) {
			ArrayList<String> entityList = IKsegment.IKAnalysis(entity); // 分词标准实体名
			similar = WeightSimilarAlgorithm.getSimilarity(strList, entityList); // 权重相似度
			map.put(similar, entity);
			// System.out.println(entity);
			// System.out.println(similar);
		}
		return map;
	}

	public HashMap<Integer, TreeMap<Double, String>> wMatching(ArrayList<String> strList) throws Exception {

		Map<Double, String> resMap = getMap(strList);
		// System.out.println(resMap);
		HashMap<Integer, TreeMap<Double, String>> resultMap = new HashMap<Integer, TreeMap<Double, String>>();

		Iterator<Double> keys = resMap.keySet().iterator();
		List<Double> listd = new ArrayList<>();
		while (keys.hasNext()) {
			listd.add(keys.next());
		}
		Collections.sort(listd); // 由低到高排序

		int j = 0;
		for (int i = listd.size() - 1; i >= 0; i--) {
			Double similar = listd.get(i);
			String matcher = resMap.get(listd.get(i));
			TreeMap<Double, String> similarAndMatcher = new TreeMap<Double, String>();
			similarAndMatcher.put(similar, matcher);
			resultMap.put(j, similarAndMatcher);
			j++;
		}
		return resultMap;

	}
}
