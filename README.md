# xnetty-rpc-client
基于netty构建的rpc分布式服务框架客户端

简介
---
  基于netty构建的rpc分布式服务框架客户端，通信协议采用json，心跳检测及重连机制；

服务端实现

```java
	final XServer server = new XServer(properties);

	Runtime.getRuntime().addShutdownHook(new Thread(){
		@Override
		public void run() {
			server.stop();
		}
	});

	server.start();
 ```
 
客户端实现

  参看 xnetty-rpc-client






