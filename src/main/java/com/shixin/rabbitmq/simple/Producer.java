package com.shixin.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author shixin
 * @Date 2021/8/10 23:30
 */
public class Producer {

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
            //4.通过通道创建交换机等...
            String queueName = "queue1";
            //队列名称
            //是否要持久化到磁盘，非持久化是否会存盘?会存盘，但是会随着服务器的重启而丢失
            //是否独占
            //是否自动删除，随着最后一个消息消费完毕是否把队列删除
            //其他参数
            channel.queueDeclare(queueName,false,false,true,null);
            //5.准备消息内容
            String message = "hello world!";
            //6.发送消息给queue
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));


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
