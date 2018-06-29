package com.su.core.game.assist.notice;

import java.util.HashMap;
import java.util.Map;

import com.su.core.game.ClassicTable;
import com.su.core.game.ContestTable;
import com.su.core.game.RankingTable;
import com.su.core.game.Table;

public class NoticeAssistFactory {

	private static Map<String, NoticeAssist> noticeAssistMap = new HashMap<>();

	public static void addNoticeAssist(NoticeAssist noticeAssist) {
		if (noticeAssist instanceof ClassicNoticeAssist)
			noticeAssistMap.put(ClassicTable.class.getName(), noticeAssist);
		else if (noticeAssist instanceof RankingNoticeAssist)
			noticeAssistMap.put(RankingTable.class.getName(), noticeAssist);
		else if (noticeAssist instanceof ContestNoticeAssist)
			noticeAssistMap.put(ContestTable.class.getName(), noticeAssist);
	}

	public static NoticeAssist getNoticeAssist(Class<? extends Table> classz) {
		return noticeAssistMap.get(classz.getName());
	}
}
