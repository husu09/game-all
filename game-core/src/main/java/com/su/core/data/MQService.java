package com.su.core.data;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.su.common.mq.MQMessage;
import com.su.common.mq.DataOperator;

/**
 * mq 服务对象
 * */
@Service
public class MQService {

	@Autowired
	private MQClient mqClient;

	public <T> void sendSave(T t) {
		MQMessage mqMessage = new MQMessage();
		mqMessage.setClassName(t.getClass().getCanonicalName());
		mqMessage.setMqOperator(DataOperator.SAVE);
		mqMessage.setData(JSON.toJSONString(t));
		mqClient.send(JSON.toJSONString(mqMessage));
	}

	public <T> void sendSave(Collection<T> ts) {
		for (T t : ts) {
			sendSave(t);
		}

	}

	public <T> void sendSave(T[] ts) {
		for (T t : ts) {
			sendSave(t);
		}
	}

	public <T> void sendUpdate(T t) {
		MQMessage mqMessage = new MQMessage();
		mqMessage.setClassName(t.getClass().getCanonicalName());
		mqMessage.setMqOperator(DataOperator.UPDATE);
		mqMessage.setData(JSON.toJSONString(t));
		mqClient.send(JSON.toJSONString(mqMessage));
	}

	public <T> void sendUpdate(Collection<T> ts) {
		for (T t : ts) {
			sendUpdate(t);
		}
	}

	public <T> void sendUpdate(T[] ts) {
		for (T t : ts) {
			sendUpdate(t);
		}
	}

	public <T> void delete(T t) {
		MQMessage mqMessage = new MQMessage();
		mqMessage.setClassName(t.getClass().getCanonicalName());
		mqMessage.setMqOperator(DataOperator.DELETE);
		mqMessage.setData(JSON.toJSONString(t));
		mqClient.send(JSON.toJSONString(mqMessage));
	}

	public <T> void delete(Collection<T> ts) {
		for (T t : ts) {
			delete(t);
		}

	}

	public <T> void delete(T[] ts) {
		for (T t : ts) {
			delete(t);
		}
	}
	
	public <T> void common(DataOperator dataOperator, T t) {
		MQMessage mqMessage = new MQMessage();
		mqMessage.setClassName(t.getClass().getCanonicalName());
		mqMessage.setMqOperator(dataOperator);
		mqMessage.setData(JSON.toJSONString(t));
		mqClient.send(JSON.toJSONString(mqMessage));
	}

}