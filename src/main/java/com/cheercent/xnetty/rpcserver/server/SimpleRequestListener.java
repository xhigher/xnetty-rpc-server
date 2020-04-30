package com.cheercent.xnetty.rpcserver.server;

import com.alibaba.fastjson.JSONObject;
import com.cheercent.xnetty.rpcserver.message.MessageFactory;
import com.cheercent.xnetty.rpcserver.message.MessageFactory.MessageRequest;
import com.cheercent.xnetty.rpcserver.message.MessageFactory.MessageResponse;
import com.cheercent.xnetty.rpcserver.server.XServer.XRequestListener;
import com.cheercent.xnetty.rpcserver.util.CommonUtils;

/*
 * @copyright (c) xhigher 2015 
 * @author xhigher    2015-3-26 
 */
public class SimpleRequestListener implements XRequestListener {


	public MessageResponse onRequest(MessageRequest request) {
    	if(request != null) {
    		String requestid = request.getRequestid();
    		if(!MessageFactory.isHeartBeatMessage(requestid)) {
    			MessageResponse response = MessageFactory.newMessageResponse(requestid);
    			JSONObject resultData = new JSONObject();
				resultData.put("name", CommonUtils.randomString(CommonUtils.randomInt(6, 16), false));
				resultData.put("age", CommonUtils.randomInt(8, 60));
				response.setData(resultData.toString());
        		return response;
        	}else {
        		return MessageFactory.newMessageResponse(requestid);
        	}
    	}
    	return null;
	}
	
}
