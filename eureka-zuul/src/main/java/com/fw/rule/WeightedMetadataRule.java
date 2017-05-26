package com.fw.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

public class WeightedMetadataRule extends ZoneAvoidanceRule {
	public static final String META_DATA_KEY_WEIGHT = "weight";

	@Override
	public Server choose(Object key) {

		List<Server> serverList = this.getPredicate().getEligibleServers(this.getLoadBalancer().getAllServers(), key);

		if (CollectionUtils.isEmpty(serverList)) {

			return null;

		}
		// 计算总值并剔除0权重节点

		int sum = 0;

		Map<Server, Integer> serverWeightMap = new HashMap<Server, Integer>();

		for (Server server : serverList) {

			String strWeight = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata()
					.get(META_DATA_KEY_WEIGHT);

			int weight = 100;

			try {
				weight = Integer.parseInt(strWeight);
			} catch (Exception e) {
				// 无需处理
			}

			if (weight <= 0) {
				continue;
			}

			serverWeightMap.put(server, weight);

			sum += weight;

		}

		// 权重随机

		int random = (int) (Math.random() * sum);

		int current = 0;

		for (Map.Entry<Server, Integer> entry : serverWeightMap.entrySet()) {

			current += entry.getValue();

			if (random < current)
				return entry.getKey();
		}

		return null;

	}
}
