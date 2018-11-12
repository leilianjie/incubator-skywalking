package org.apache.skywalking.apm.collector.storage.es.dao.ui;

import java.util.List;

import org.apache.skywalking.apm.collector.client.elasticsearch.ElasticSearchClient;
import org.apache.skywalking.apm.collector.core.util.Const;
import org.apache.skywalking.apm.collector.storage.dao.ui.IConnPoolMetricUIDAO;
import org.apache.skywalking.apm.collector.storage.es.base.dao.EsDAO;
import org.apache.skywalking.apm.collector.storage.table.jvm.ConnPoolMetricTable;
import org.apache.skywalking.apm.collector.storage.ui.common.Step;
import org.apache.skywalking.apm.collector.storage.utils.DurationPoint;
import org.apache.skywalking.apm.collector.storage.utils.TimePyramidTableNameBuilder;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;

public class ConnPoolMetricUIDAO extends EsDAO implements IConnPoolMetricUIDAO {

	public ConnPoolMetricUIDAO(ElasticSearchClient client) {
		super(client);
	}

	@Override
	public Trend getConnsTrend(int instanceId, Step step, List<DurationPoint> durationPoints) {
		  String tableName = TimePyramidTableNameBuilder.build(step, ConnPoolMetricTable.TABLE);

	        MultiGetRequestBuilder prepareMultiGet = getClient().prepareMultiGet(durationPoints, new ElasticSearchClient.MultiGetRowHandler<DurationPoint>() {
	            @Override
	            public void accept(DurationPoint durationPoint) {
	                String id = durationPoint.getPoint() + Const.ID_SPLIT + instanceId;
	                this.add(tableName, ConnPoolMetricTable.TABLE_TYPE, id);
	            }
	        });

	        Trend trend = new Trend();
	        MultiGetResponse multiGetResponse = prepareMultiGet.get();
	        for (MultiGetItemResponse response : multiGetResponse.getResponses()) {
	            if (response.getResponse().isExists()) {
	            	trend.getPools().add((String) response.getResponse().getSource().get(ConnPoolMetricTable.POOL_NAME.getName()));
	            	trend.getActiveMetrics().add(((Number) response.getResponse().getSource().get(ConnPoolMetricTable.ACTIVE.getName())).intValue());
	            	trend.getMaxMetrics().add(((Number) response.getResponse().getSource().get(ConnPoolMetricTable.MAX.getName())).intValue());
	            }
	        }
	        return trend;
	}

}
