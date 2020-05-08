package com.cheercent.xnetty.rpcserver.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.cheercent.xnetty.rpcserver.message.MessageFactory;
import com.cheercent.xnetty.rpcserver.message.MessageFactory.MessageResponse;
import com.cheercent.xnetty.rpcserver.util.CommonUtils;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class XClientManager {

    private static final Logger logger = LoggerFactory.getLogger(XClientManager.class);
    
    private static XClientManager clientManager;
    private final ChannelGroup clientChannelGroup;

    //for test
    private final ScheduledExecutorService scheduledService;
    
    private XClientManager() {
    	clientChannelGroup = new DefaultChannelGroup("clientChannelGroup", ImmediateEventExecutor.INSTANCE);
    	
    	scheduledService = Executors.newScheduledThreadPool(4);
    }
    
    public static XClientManager instance(){
        if(clientManager == null){
            synchronized (XClientManager.class){
                if(clientManager == null){
                	clientManager = new XClientManager();
                }
            }
        }
        return clientManager;
    }
    
	public void onChannelConnect(Channel channel){
		clientChannelGroup.add(channel);
	}
	
	public void onChannelClose(Channel channel){
		clientChannelGroup.remove(channel);
	}
	
	public void broadcastMessage(String message){
		if(message != null) {
			clientChannelGroup.writeAndFlush(message);
		}
	}
	
	public void startTest(){
		scheduledService.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				String requestid = "all-"+String.valueOf(System.currentTimeMillis());
				logger.info("scheduleAtFixedRate send message: requestid="+requestid);
				MessageResponse response = MessageFactory.newMessageResponse(requestid);
				JSONObject resultData = new JSONObject();
				resultData.put("title", CommonUtils.randomString(10, false));
				response.setData(resultData.toString());
				String responseString = response.toString();
				logger.info("responseString="+responseString);
				broadcastMessage(responseString);
			}
			
		}, 10, 15, TimeUnit.SECONDS);
	}
}
