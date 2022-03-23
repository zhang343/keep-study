package com.kuang.springcloud.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
@Slf4j
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    public static final String ReplyMeExchange = "replyMeExchange";

    public static final String ReplyMeQUEUE = "replyMeQUEUE";

    public static final String MyNewsExchange = "myNewsExchange";

    public static final String MyNewsQUEUE = "myNewsQUEUE";

    public static final String FriendFeedExchange = "friendFeedExchange";

    public static final String FriendFeedQUEUE = "friendFeedQUEUE";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }



    //配置回复我的fanout_exchange
    @Bean
    public FanoutExchange replyMeFanoutExchange() {
        return new FanoutExchange(RabbitConfig.ReplyMeExchange);
    }
    //创建回复我的队列
    @Bean
    public Queue replyMeQueue() {
        return new Queue(RabbitConfig.ReplyMeQUEUE, true); //队列持久
    }
    //将回复我的队列绑定到回复我的交换机上
    @Bean
    Binding bindingExchangeReplyMe(Queue replyMeQueue,FanoutExchange replyMeFanoutExchange) {
        return BindingBuilder.bind(replyMeQueue).to(replyMeFanoutExchange);
    }



    //配置我的消息fanout_exchange
    @Bean
    public FanoutExchange MyNewsFanoutExchange() {
        return new FanoutExchange(RabbitConfig.MyNewsExchange);
    }
    //创建我的消息队列
    @Bean
    public Queue MyNewsQueue() {
        return new Queue(RabbitConfig.MyNewsQUEUE, true); //队列持久
    }
    //将我的消息队列绑定到我的消息交换机上
    @Bean
    Binding bindingExchangeMyNews(Queue MyNewsQueue,FanoutExchange MyNewsFanoutExchange) {
        return BindingBuilder.bind(MyNewsQueue).to(MyNewsFanoutExchange);
    }


    //配置好友动态fanout_exchange
    @Bean
    public FanoutExchange FriendFeedFanoutExchange() {
        return new FanoutExchange(RabbitConfig.FriendFeedExchange);
    }
    //创建好友动态队列
    @Bean
    public Queue FriendFeedQueue() {
        return new Queue(RabbitConfig.FriendFeedQUEUE, true); //队列持久
    }
    //将好友动态队列绑定到好友动态交换机上
    @Bean
    Binding bindingExchangeFriendFeed(Queue FriendFeedQueue,FanoutExchange FriendFeedFanoutExchange) {
        return BindingBuilder.bind(FriendFeedQueue).to(FriendFeedFanoutExchange);
    }
}
