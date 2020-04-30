package com.cheercent.xnetty.rpcserver.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.cheercent.xnetty.rpcserver.message.MessageFactory;
import com.cheercent.xnetty.rpcserver.message.MessageFactory.MessageRequest;
import com.cheercent.xnetty.rpcserver.message.MessageFactory.MessageResponse;
import com.cheercent.xnetty.rpcserver.server.XServer.XRequestListener;
import com.cheercent.xnetty.rpcserver.util.CommonUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class XServerHandler extends SimpleChannelInboundHandler<JSONObject> {

    private static final Logger logger = LoggerFactory.getLogger(XServerHandler.class);
    
    private XRequestListener requestListener;
    
    public XServerHandler(XRequestListener listener) {
    	requestListener = listener; 
    }
    
    
    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final JSONObject data) throws Exception {
    	logger.info("channelRead0: data = {}", data.toString());
    	JSONObject responseData = handleRequest(data);
    	if(responseData != null) {
    		ctx.writeAndFlush(responseData);
    		logger.info("channelRead0: send data = {}", responseData.toString());
    	}
        
    }

    private JSONObject handleRequest(JSONObject data) {
    	MessageRequest request = MessageFactory.toMessageRequest(data);
    	if(request != null) {
    		MessageResponse response = requestListener.onRequest(request);
    		if(response != null) {
    			return response.toJSONObject();
    		}
    	}
		return null;
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("exceptionCaught", cause);
    }
}
