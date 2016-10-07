
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CosineMatching {

	public Map<Double, String> getMap(String str,ArrayList<String> attrList) throws Exception {

		// IdentityHashMap
		Map<Double, String> map = new IdentityHashMap<Double, String>();
		Double similar;

		// 余弦相似度模糊匹配属性
		for (String attr : attrList) {
			//System.out.println(attr);
			similar = CosineSimilarAlgorithm.getSimilarity(str, attr); // 余弦相似度
			//System.out.println(similar);
			map.put(similar, attr);
		}

		// 余弦相似度模糊匹配实体
		//ArrayList<Float> num_str = ProcessingMode.getNum(str);// 问句中包含的数字
		// for (String entity : PreTreatment.entity_list) {
		// similar = CosineSimilarAlgorithm.getSimilarity(str, entity); // 余弦相似度
		// ArrayList<Float> num_entity = ProcessingMode.getNum(entity);
		// if (num_entity.size() != 0) {
		// float value = 1 / (float) (num_entity.size());
		// for (float num_s : num_str) {
		// for (float num_e : num_entity) {
		// if (Math.abs(num_s - num_e) <= 1e-6) {
		// similar += value;
		// }
		// }
		// }
		// }
		// map.put(similar, entity);
		// // System.out.println(entity);
		// // System.out.println(simi_num);
		// }

		return map;
	}

	public HashMap<Integer, TreeMap<Double, String>> iMatching(String str,ArrayList<String> attrList) throws Exception {

		Map<Double, String> resMap = getMap(str,attrList);
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
