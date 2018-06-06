package com.su.core.gambling;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.su.common.util.TimeUtil;
import com.su.core.context.PlayerContext;
import com.su.core.gambling.enums.PlayerState;
import com.su.core.gambling.enums.TableState;
import com.su.core.gambling.enums.Team;


/**
 * 游戏中的玩家对象
 */
public class GamePlayer  implements Delayed{
	
	private long id;
	/**
	 * 坐位
	 */
	private Integer index;
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
	
	/**
	 * 叫牌时间
	 */
	private static final int CALL_WAIT_TIME = TimeUtil.ONE_SECOND * 15;
	/**
	 * 出牌时间
	 */
	private static final int OPERATE_WAIT_TIME = TimeUtil.ONE_SECOND * 15;
	
	public GamePlayer(PlayerContext playerContext) {
		this.playerContext = playerContext;
		this.id = playerContext.getPlayerId();
		this.handCards = new Card[Card.HAND_CARDS_NUM];
		// 设置玩家上下文持有游戏对象
		playerContext.setGamePlayer(this);
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
	
	
	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GamePlayer other = (GamePlayer) obj;
		if (id != other.id)
			return false;
		return true;
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
	 * 清空玩家状态
	 * */
	public void clean() {
		reset();
		this.index = null;
		this.table = null;
	}
	
	/**
	 * 重置玩家状态
	 * */
	public void reset() {
		// 重置用户手牌
		for (int i = 0; i < this.handCards.length; i ++) {
			this.handCards[i] = null; 
		}
		this.team = null;
		this.multipleValue = 0;
		this.score = 0;
		this.state = null;
		this.isAuto = false;
		this.rank = null;
		this.opTime = null;
	}
	
	
	/**
	 * 设置状态
	 * */
	public void setState(PlayerState state, boolean isDelay) {
		this.state = state;
		if (isDelay && state == PlayerState.OPERATE) {
			if (this.table.getState() == TableState.CALL)
				this.opTime = TimeUtil.getCurrTime() + CALL_WAIT_TIME;
			else if (this.table.getState() == TableState.DRAW) 
				this.opTime = TimeUtil.getCurrTime() + OPERATE_WAIT_TIME;
			else 
				return;
			this.table.getSite().getWaitGamePlayerQueue().put(this);
		}
	}


	
}
