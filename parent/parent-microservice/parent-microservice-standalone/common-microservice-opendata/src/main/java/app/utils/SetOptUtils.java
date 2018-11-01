package app.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class SetOptUtils {
	

	public static <T> Set<T> calcuateResidualSet(Set<T> allSet, Set<T> childSet) {
		if (CollectionUtils.isEmpty(allSet)) {
			return null;
		}
		allSet.stream().filter(item -> {
			return childSet.contains(item);
		});
		return allSet;
	}
	
	
	/**
	 * 差集
	 * @param allList
	 * @param childList
	 * @return
	 */
	public static <T> List<T> calcuateResidualList(List<T> allList, List<T> childList) {
		if (CollectionUtils.isEmpty(allList)) {
			return null;
		}
		List<T> list = allList.stream().filter(item -> !childList.contains(item)).collect(Collectors.toList());
		return list;
	}

}
