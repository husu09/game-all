package com.su.server.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.common.obj.Grid;
import com.su.common.obj.Item;
import com.su.common.po.PlayerDetail;
import com.su.common.util.TimeUtil;
import com.su.core.context.PlayerContext;
import com.su.core.data.DataService;
import com.su.core.event.GameEventAdapter;
import com.su.excel.config.BagCo;
import com.su.excel.mapper.BagMapper;
import com.su.msg.BagMsg.DeleteItem_;
import com.su.msg.BagMsg.UpdateItem_;
import com.su.msg.BagMsg._Grid;
import com.su.msg.LoginMsg.Login_.Builder;

@Service
public class BagService extends GameEventAdapter {

	private Logger logger = LoggerFactory.getLogger(BagService.class);

	@Autowired
	private BagMapper bagConf;
	@Autowired
	private LogServer logServer;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private DataService dataService;

	/**
	 * 添加物品
	 */
	public boolean addItem(PlayerContext playerContext, Item item, int reason) {
		PlayerDetail playerDetail  = playerService.getPlayerDetail(playerContext.getPlayerId());
		// 排序规则：类型小的 < 品质小 < id小
		BagCo bagCo = bagConf.get(item.getSysId());
		if (bagCo == null) {
			logger.error("找不到对应的配置 {}", item.getSysId());
			return false;
		}
		List<Grid> bagGrid = playerDetail.getGridList();
		for (int i = 0; i < bagGrid.size(); i++) {
			// 全部已添加
			if (item.getCount() == 0)
				break;

			Grid grid = bagGrid.get(i);

			// id 相同叠加物品
			if (grid.getSysId() == item.getSysId()) {
				if (grid.getCount() >= bagCo.getLimit()) {
					continue;
				}
				int addCount = item.getCount();
				if (item.getCount() + grid.getCount() > bagCo.getLimit()) {
					addCount  = bagCo.getLimit() - grid.getCount();
					item.setCount(item.getCount() - addCount);
				} else {
					item.setCount(0);
				}
				grid.setCount(grid.getCount() + addCount);
				// 通知
				UpdateItem_.Builder builder = UpdateItem_.newBuilder();
				builder.setGrid(serializeGrid(i, grid));
				playerContext.write(builder);
				continue;
			}
			if (grid.getType() == item.getType()) {
				BagCo currBagCo = bagConf.get(grid.getSysId());
				if (currBagCo.getQuality() > bagCo.getQuality()) {
					createGrid(playerContext, bagGrid, i, item, bagCo);
				} else if (currBagCo.getQuality() == bagCo.getQuality() && currBagCo.getId() > bagCo.getId()) {
					createGrid(playerContext, bagGrid, i, item, bagCo);
				}
			} else if (grid.getType() > item.getType()) {
				createGrid(playerContext, bagGrid, i, item, bagCo);
			}
		}
		// 找不到可以叠加或插入的位置，直接添加到末尾
		if (item.getCount() > 0) {
			createGrid(playerContext, bagGrid, bagGrid.size(), item, bagCo);
		}
		playerDetail.updateBagData();
		dataService.update(playerDetail);
		// 流水
		logServer.addResourceLog(reason, item.getCount(), -1);
		return true;
	}

	/**
	 * 扣除物品
	 */
	public boolean eddItem(PlayerContext playerContext, Item item, int reason) {
		PlayerDetail playerDetail  = playerService.getPlayerDetail(playerContext.getPlayerId());
		int haveCount = 0;
		List<Grid> bagGrid = playerDetail.getGridList();
		for (int i = 0; i < bagGrid.size(); i++) {
			Grid grid = bagGrid.get(i);
			if (grid.getType() > item.getType())
				break;

			if (item.getSysId() == grid.getSysId())
				haveCount += grid.getCount();
		}
		if (haveCount < item.getCount()) {
			return false;
		}
		UpdateItem_.Builder updateItem_ = null;
		DeleteItem_.Builder deleteItem_ = null;
		// 从后往前，数量少的先扣
		for (int i = bagGrid.size(); i > -1; i--) {
			Grid grid = bagGrid.get(i);
			if (item.getSysId() == grid.getSysId()) {
				if (grid.getCount() > item.getCount()) {
					grid.setCount(grid.getCount() - item.getCount());
					// 通知
					if (updateItem_ == null)
						updateItem_ = UpdateItem_.newBuilder();
					updateItem_.setGrid(serializeGrid(i, grid));
					playerContext.write(updateItem_);
					break;
				} else {
					item.setCount(item.getCount() - grid.getCount());
					if (item.getCount() == 0) {
						// 通知
						if (deleteItem_ == null)
							deleteItem_ = DeleteItem_.newBuilder();
						deleteItem_.setIndex(i);
						playerContext.write(deleteItem_);
						break;
					}
				}
			}
		}
		playerDetail.updateBagData();
		dataService.update(playerDetail);
		// 流水
		logServer.addResourceLog(reason, item.getCount(), haveCount - item.getCount());
		return true;
	}

	/**
	 * 创建新格子
	 */
	private void createGrid(PlayerContext playerContext, List<Grid> bagGrid, int index, Item item, BagCo bagCo) {
		// 全部已添加
		if (item.getCount() == 0)
			return;
		Grid grid = new Grid();
		grid.setType(item.getType());
		grid.setSysId(item.getSysId());
		int addCount = item.getCount();
		if (item.getCount() > bagCo.getLimit()) {
			addCount = bagCo.getLimit();
			item.setCount(item.getCount() - bagCo.getLimit());
		} else {
			item.setCount(0);
		}
		grid.setCount(addCount);
		if (bagCo.getExpirationTime() != 0) {
			grid.setEndTime(TimeUtil.getCurrTime() + bagCo.getExpirationTime());
		}
		bagGrid.add(index, grid);
		// 通知
		UpdateItem_.Builder builder = UpdateItem_.newBuilder();
		builder.setGrid(serializeGrid(index, grid));
		playerContext.write(builder);

		createGrid(playerContext, bagGrid, index + 1, item, bagCo);
	}

	public _Grid serializeGrid(int index, Grid grid) {
		_Grid.Builder builder = _Grid.newBuilder();
		builder.setIndex(index);
		builder.setType(grid.getType());
		builder.setSysId(grid.getSysId());
		builder.setCount(grid.getCount());
		builder.setEndTime(grid.getEndTime());
		return builder.build();
	}

	@Override
	public void login(PlayerContext playerContext, Builder builder) {
		PlayerDetail playerDetail = playerService.getPlayerDetail(playerContext.getPlayerId());
		List<Grid> gridList = playerDetail.getGridList();
		for (int i = 0; i < gridList.size(); i ++ ) {
			builder.addGrid(serializeGrid(i, gridList.get(i)));
		}
	}
	
	
}
