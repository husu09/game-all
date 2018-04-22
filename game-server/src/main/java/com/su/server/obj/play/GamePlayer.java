package com.su.server.obj.play;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.su.proto.PlayProto.CardPro;
import com.su.proto.PlayProto.GamePlayerPro;
import com.su.proto.PlayProto.MultiplePro;
import com.su.server.context.PlayerContext;

/**
 * 游戏中的玩家对象
 */
public class GamePlayer implements Delayed {
	private long id;
	/**
	 * 坐位
	 */
	private int index;
	/**
	 * 手牌
	 */
	private Card[] handCards = new Card[27];
	/**
	 * 队伍
	 */
	private Team team = Team.NONE;
	/**
	 * 我的倍数
	 */
	private int multipleValue;
	/**
	 * 分数
	 */
	private int myScore;
	/**
	 * 状态
	 */
	private PlayerState state = PlayerState.READY;
	/**
	 * 截止时间
	 */
	private int deadLine;
	/**
	 * 是否托管
	 */
	private int isAuto;
	/**
	 * 玩家上下文
	 */
	private PlayerContext playerContext;
	
	private GamePlayerPro.Builder gamePlayerPro = GamePlayerPro.newBuilder();

	private Table table;

	public GamePlayer(PlayerContext playerContext) {
		this.playerContext = playerContext;
		this.id = playerContext.getPlayer().getId();
	}


	/**
	 * 叫牌
	 */
	public void call() {

	}

	/**
	 * 出牌
	 */
	public void draw() {

	}

	/**
	 * 过牌
	 */
	public void check() {
		state = PlayerState.WATCH;
		table.doCheck(this);
	}

	@Override
	public int compareTo(Delayed o) {
		if (this.getDelay(TimeUnit.SECONDS) > o.getDelay(TimeUnit.SECONDS))
			return 1;
		else if (this.getDelay(TimeUnit.SECONDS) < o.getDelay(TimeUnit.SECONDS))
			return -1;
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(deadLine, TimeUnit.SECONDS);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Card[] getHandCards() {
		return handCards;
	}

	public void setHandCards(Card[] handCards) {
		this.handCards = handCards;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getMultipleValue() {
		return multipleValue;
	}

	public void setMultipleValue(int multipleValue) {
		this.multipleValue = multipleValue;
	}

	public int getMyScore() {
		return myScore;
	}

	public void setMyScore(int myScore) {
		this.myScore = myScore;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public int getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(int deadLine) {
		this.deadLine = deadLine;
	}

	public int getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public long getId() {
		return id;
	}

	public PlayerContext getPlayerContext() {
		return playerContext;
	}

	public GamePlayerPro.Builder getGamePlayerPro() {
		return gamePlayerPro;
	}
	
}
