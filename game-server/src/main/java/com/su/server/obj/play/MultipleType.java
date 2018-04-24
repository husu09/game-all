package com.su.server.obj.play;

public enum MultipleType {
	/**
	 * 初始倍数 0
	 */
	CHU_SHI(15),
	/**
	 * 叫牌 1
	 */
	JIAO_PAI(2),
	/**
	 * 暗叫 2
	 */
	AN_JIAO(4),
	/**
	 * 明叫 3
	 */
	MING_JIAO(6),
	/**
	 * 全胜 4
	 */
	QUAN_SHENG(2),
	/**
	 * 满分 5
	 */
	MAN_FEN(4),
	/**
	 * 510K 6
	 */
	F_510K(2),
	/**
	 * 六炸 7
	 */
	LIU_ZHA(2),
	/**
	 * 七炸 8
	 */
	QI_ZHA(3),
	/**
	 * 王炸 9
	 */
	WANG_ZHA(2),
	/**
	 * 同花510k 10
	 */
	TONG_HUA_510K(3),
	/**
	 * 天炸 11
	 */
	TIAN_ZHA(4);

	private int value;

	private MultipleType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
