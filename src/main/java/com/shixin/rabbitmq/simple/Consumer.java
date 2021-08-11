package com.shixin.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author shixin
 * @Date 2021/8/10 23:30
 */
public class Consumer {
    public static void main(String[] args) {
        //rabbit遵循的是amqp，并不是tcp/ip
        // ip port

        //1.创建一个连接工程
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("www.shixin.xyz");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/"); //代表把消息发送在根目录下
        //2.创建Connect
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection("生产者");
            //3.创建通过连接获取通道
            channel = connection.createChannel();

            channel.basicConsume("queue1", true, new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery message) throws IOException {
                    System.out.println(consumerTag);
                    System.out.println(new String(message.getBody(),"UTF-8"));
                    System.out.println("收到queue1的一条信息");
                }
            }, new CancelCallback() {
                @Override
                public void handle(String consumerTag) throws IOException {
                    System.out.println("消息接收失败");
                }
            });
            System.out.println("开始接收消息");
            System.in.read();

        } catch (Exception e) {
            //7.关闭通道
            if (channel !=null && channel.isOpen()){
                try {
                    channel.close();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            //8.关闭连接
            if (connection !=null && connection.isOpen()){
                try {
                    connection.close();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }
}
