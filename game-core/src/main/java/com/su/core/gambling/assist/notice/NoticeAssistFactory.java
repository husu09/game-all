package com.su.core.gambling.assist.notice;

import java.util.HashMap;
import java.util.Map;

import com.su.core.gambling.BasicTable;
import com.su.core.gambling.ClassicTable;
import com.su.core.gambling.ContestTable;
import com.su.core.gambling.RankingTable;

public class NoticeAssistFactory {

	private static Map<String, BasicNoticeAssist> noticeAssistMap = new HashMap<>();

	public static void addNoticeAssist(BasicNoticeAssist noticeAssist) {
		if (noticeAssist instanceof ClassicNoticeAssist)
			noticeAssistMap.put(ClassicTable.class.getName(), noticeAssist);
		else if (noticeAssist instanceof RankingNoticeAssist)
			noticeAssistMap.put(RankingTable.class.getName(), noticeAssist);
		else if (noticeAssist instanceof ContestNoticeAssist)
			noticeAssistMap.put(ContestTable.class.getName(), noticeAssist);
	}

	public static BasicNoticeAssist getNoticeAssist(Class<? extends BasicTable> classz) {
		return noticeAssistMap.get(classz.getName());
	}
}
