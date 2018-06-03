package com.su.core.gambling;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.su.core.context.PlayerContext;
import com.su.core.gambling.enums.PlayerState;
import com.su.core.gambling.enums.Team;


/**
 * 游戏中的玩家对象
 */
public class GamePlayer  implements Delayed{
	
	private long id;
	/**
	 * 坐位
	 */
	private int index;
	/**
	 * 手牌
	 */
	private Card[] handCards;
	/**
	 * 队伍
	 */
	private Team team;
	/**
	 * 倍数
	 */
	private int multipleValue;
	/**
	 * 分数
	 */
	private int score;
	/**
	 * 状态
	 */
	private volatile PlayerState state;
	/**
	 * 是否托管
	 */
	private boolean isAuto;
	/**
	 * 名次
	 * */
	private Integer rank;
	/**
	 * 操作时间
	 * */
	private Long opTime;
	
	private Table table;
	
	private PlayerContext playerContext;
	
	public GamePlayer(PlayerContext playerContext) {
		this.playerContext = playerContext;
		this.id = playerContext.getPlayerId();
		// 设置玩家上下文持有游戏对象
		playerContext.setGamePlayer(this);
		handCards = new Card[Card.HAND_CARDS_NUM];
		
	}
	
	public long getId() {
		return id;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}


	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public PlayerContext getPlayerContext() {
		return playerContext;
	}
	

	public Long getOpTime() {
		return opTime;
	}

	public void setOpTime(Long opTime) {
		this.opTime = opTime;
	}

	@Override
	public int compareTo(Delayed o) {
		if(this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
            return 1;
        }else if(this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
            return -1;
        }
        return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		 return unit.convert(opTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 重置玩家状态
	 * */
	public void reset() {
		// 重置用户手牌
		for (int i = 0; i < handCards.length; i ++) {
			handCards[i] = null; 
		}
		team = null;
		multipleValue = 0;
		score = 0;
		state = null;
		isAuto = false;
		rank = null;
		opTime = null;
	}


	
}
