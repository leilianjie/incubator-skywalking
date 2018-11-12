package org.apache.skywalking.apm.collector.storage.es.dao.ui;

import java.util.List;

import org.apache.skywalking.apm.collector.client.elasticsearch.ElasticSearchClient;
import org.apache.skywalking.apm.collector.core.util.Const;
import org.apache.skywalking.apm.collector.storage.dao.ui.IThreadPoolMetricUIDAO;
import org.apache.skywalking.apm.collector.storage.es.base.dao.EsDAO;
import org.apache.skywalking.apm.collector.storage.table.jvm.ThreadPoolMetricTable;
import org.apache.skywalking.apm.collector.storage.ui.common.Step;
import org.apache.skywalking.apm.collector.storage.utils.DurationPoint;
import org.apache.skywalking.apm.collector.storage.utils.TimePyramidTableNameBuilder;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;

public class ThreadPoolMetricUIDAO extends EsDAO implements IThreadPoolMetricUIDAO {
	
	public ThreadPoolMetricUIDAO(ElasticSearchClient client) {
		super(client);
	}

	@Override
	public Trend getThreadsTrend(int instanceId, Step step, List<DurationPoint> durationPoints) {
		  String tableName = TimePyramidTableNameBuilder.build(step, ThreadPoolMetricTable.TABLE);

	        MultiGetRequestBuilder prepareMultiGet = getClient().prepareMultiGet(durationPoints, new ElasticSearchClient.MultiGetRowHandler<DurationPoint>() {
	            @Override
	            public void accept(DurationPoint durationPoint) {
	                String id = durationPoint.getPoint() + Const.ID_SPLIT + instanceId;
	                this.add(tableName, ThreadPoolMetricTable.TABLE_TYPE, id);
	            }
	        });

	        Trend trend = new Trend();
	        MultiGetResponse multiGetResponse = prepareMultiGet.get();
	        for (MultiGetItemResponse response : multiGetResponse.getResponses()) {
	            if (response.getResponse().isExists()) {
	            	trend.getPoolsMetrics().add((String) response.getResponse().getSource().get(ThreadPoolMetricTable.POOL_NAME.getName()));
	            	trend.getCurrentMetrics().add(((Number) response.getResponse().getSource().get(ThreadPoolMetricTable.CURRENT.getName())).intValue());
	            	trend.getMaxMetrics().add(((Number) response.getResponse().getSource().get(ThreadPoolMetricTable.MAX.getName())).intValue());
	            	trend.getBusyMetrics().add(((Number) response.getResponse().getSource().get(ThreadPoolMetricTable.BUSY.getName())).intValue());
	            }
	        }
	        return trend;
	}

}
