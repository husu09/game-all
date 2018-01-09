package com.su.data.mq;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.su.common.mq.MQMessage;
import com.su.common.mq.DataOperator;
import com.su.common.util.SpringUtil;
import com.su.data.dao.BaseDao;

/**
 * mq消费者
 */
public class MQCustomer extends DefaultConsumer {

	private BaseDao dao = SpringUtil.getContext().getBean(BaseDao.class);

	public MQCustomer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
			throws IOException {
		String message = new String(body, "UTF-8");
		MQMessage mqMessage = JSON.parseObject(message, MQMessage.class);
		try {
			if (mqMessage.getMqOperator() == DataOperator.SAVE) {
				dao.save(JSON.parseObject(mqMessage.getData(), Class.forName(mqMessage.getClassName())));
			} else if (mqMessage.getMqOperator() == DataOperator.UPDATE) {
				dao.update(JSON.parseObject(mqMessage.getData(), Class.forName(mqMessage.getClassName())));
			} else if (mqMessage.getMqOperator() == DataOperator.DELETE) {
				dao.delete(JSON.parseObject(mqMessage.getData(), Class.forName(mqMessage.getClassName())));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("mq 消息消息失败 " + message);
		}
	}

}
