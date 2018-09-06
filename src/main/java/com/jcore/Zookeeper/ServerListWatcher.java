package com.jcore.Zookeeper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcore.Tool.PropertiesHelp;

public class ServerListWatcher {

	public class ServerNode {
		private String id;

		private String name;

		private String address;

		private String port;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

	}

	static Lock _store_Lock = new ReentrantLock();

	static Map<String, Map<String, ServerNode>> store = new HashMap<String, Map<String, ServerNode>>();

	static BaseZookeeper bzk = new BaseZookeeper();

	public static ServerNode watch(String serverKey) throws Exception {

		if (!store.containsValue(serverKey)) {
			String connectstring = PropertiesHelp.getApplicationConf("spring.cloud.zookeeper.connect-string");

			bzk.connectZookeeper(connectstring);
			String root = PropertiesHelp.getApplicationConf("spring.cloud.zookeeper.discovery.root");

			fillStore(serverKey, root);

		}
		
		Random rand = new Random();
		
		int index =  rand.nextInt(store.get(serverKey).size());

		return store.get(serverKey).get(index); // 负载均衡算法 // 随机

	}

	private static void fillStore(String serverKey, String root)
			throws KeeperException, InterruptedException, IOException, JsonParseException, JsonMappingException {
		_store_Lock.lock();
		try {

			Watcher watcher = new Watcher() {

				@Override
				public void process(WatchedEvent arg0) {
					// TODO 自动生成的方法存根
					store.clear();
				}
			};

			List<String> ls = bzk.getChildren(root + "/" + serverKey, watcher);

			for (String item : ls) {

				String json = bzk.getData(root + "/" + serverKey + "/" + item);
				ObjectMapper objectMapper = new ObjectMapper();
				ServerNode sn = objectMapper.readValue(json, ServerNode.class);

				Map<String, ServerNode> snMap = new HashMap<String, ServerNode>();
				snMap.put(sn.getId(), sn);

				store.put(serverKey, snMap);

			}
		} finally {
			_store_Lock.unlock();
		}
	}

}
