package com.tdclighthouse.prototype.utils;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.comparators.ComparableComparator;
import org.junit.Assert;
import org.junit.Test;

import com.tdclighthouse.prototype.utils.ReplacementUtil.ReplacemnetItem;

public class ReplacementUtilTest {
	
	@Test
	public void replacemnetItemOrderTest() {
		@SuppressWarnings("unchecked")
		SortedSet<ReplacemnetItem> set = new TreeSet<ReplacemnetItem>(new ComparableComparator());
		set.add(new ReplacemnetItem(10, 20, "replacement"));
		set.add(new ReplacemnetItem(30, 40, "replacement"));
		Iterator<ReplacemnetItem> iterator = set.iterator();
		Assert.assertEquals(30, iterator.next().getStart());
		Assert.assertEquals(10, iterator.next().getStart());
		
	}
	
	@Test
	public void replacementTest() {
		String originalString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sed erat quam, quis porta enim adipiscing. Mauris urna ligula, scelerisque";
		ReplacementUtil replacementUtil = new ReplacementUtil(originalString);
		Matcher matcher = Pattern.compile("adipiscing").matcher(originalString);
		while(matcher.find()) {
			replacementUtil.addReplacemnetItem(new ReplacemnetItem(matcher.start(), matcher.end(), "test"));
		}
		
		Assert.assertEquals("Lorem ipsum dolor sit amet, consectetur test elit. Proin sed erat quam, quis porta enim test. Mauris urna ligula, scelerisque", replacementUtil.replace());
		
	}

}
