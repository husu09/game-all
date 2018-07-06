package com.su.core.game.assist.notice;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.su.core.game.Card;
import com.su.core.game.GamePlayer;
import com.su.core.game.Table;
import com.su.msg.TableMsg._Card;
import com.su.msg.TableMsg._GamePlayer;
import com.su.msg.TableMsg._Table;

@Component
public class NoticeAssist {
	
	@PostConstruct
	public void initialize(){ 
		NoticeAssistFactory.addNoticeAssist(this);
	}
	
	/**
	 * 通知所有状态
	 */
	public void notice(Table basicTable) {
		for (GamePlayer otherGamePlayer : basicTable.getPlayers()) {
			otherGamePlayer.getPlayerContext().write(serializeTable(otherGamePlayer.getTable(),
					otherGamePlayer.getTable().getTableBuilder(), otherGamePlayer));
		}
	}

	/**
	 * 序列化牌桌
	 */
	public _Table serializeTable(Table table, _Table.Builder builder, GamePlayer currGamePlayer) {
		builder.clear();
		for (GamePlayer otherGamePlayer : table.getPlayers())
			builder.addPlayer(serializeGamePlayer(otherGamePlayer, table.getGamePlayerBuilder(),
					currGamePlayer.equals(otherGamePlayer)));
		for (int multiple : table.getMultiples())
			builder.addMultiple(multiple);
		builder.setState(table.getState().getValue());
		for (Card card : table.getLastCards()) {
			if (card != null)
				builder.addLastCard(serializeCard(card, table.getCardBuilder()));
		}
		builder.setLastCardType(table.getLastCardType().getValue());
		builder.setLastOp(table.getLastOp());
		builder.setRoundScore(table.getRoundScore());
		builder.setCallCard(serializeCard(table.getCallCard(), table.getCardBuilder()));
		builder.setCallType(table.getCallType().getValue());
		builder.setCallOp(table.getCallOp());
		builder.setDealer(table.getDealer());
		builder.setWaitTime(table.getWaitTime());
		builder.setType(table.getSite().getSiteType().getValue());
		return builder.build();
	}

	/**
	 * 序列化游戏玩家
	 */
	public _GamePlayer serializeGamePlayer(GamePlayer gamePlayer, _GamePlayer.Builder builder, boolean isContainHands) {
		builder.clear();
		builder.setId(gamePlayer.getId());
		if (isContainHands)
			for (Card card : gamePlayer.getHandCards()) {
				if (card != null)
					builder.addHandCard(serializeCard(card, gamePlayer.getTable().getCardBuilder()));
			}
		else
			builder.setCardNum(gamePlayer.getHandCardsCount());
		builder.setMultiple(gamePlayer.getMultiple());
		builder.setScore(gamePlayer.getScore());
		builder.setIsAuto(gamePlayer.getIsAuto());
		builder.setState(gamePlayer.getState().getValue());
		builder.setOpTime(gamePlayer.getOpTime());
		builder.setContestScore(gamePlayer.getContestScore());
		builder.setIsQuit(gamePlayer.getIsQuit());
		return builder.build();
	}

	/**
	 * 序列化卡牌
	 */
	public _Card serializeCard(Card card, _Card.Builder builder) {
		builder.clear();
		builder.setValue(card.getValue());
		builder.setSuit(card.getSuit().getValue());
		return builder.build();
	}

}
