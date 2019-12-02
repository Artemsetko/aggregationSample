import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;


public class Main {
    public static void main(String[] args) throws UnknownHostException, ExecutionException, InterruptedException {
        List<CustomObject> result = getAggregationResult();

        for (CustomObject customObject : result) {
            System.out.println(customObject.getMachineOS() + ": "+ customObject.getCount());
        }
    }

    private static List<CustomObject> getAggregationResult() throws ExecutionException, InterruptedException, UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch").build();

        TransportClient transportClient = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300))
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9301));
        SearchRequestBuilder aggregationQuery =
                transportClient.prepareSearch("kibana_sample_data_logs")
                        .setQuery(QueryBuilders.matchAllQuery())
                        .addAggregation(AggregationBuilders.terms("2")
                                .field("machine.os.keyword").size(10));

        SearchResponse response = aggregationQuery.execute().get();
        Aggregation aggregation = response.getAggregations().get("2");
        StringTerms st = (StringTerms) aggregation;
        return st.getBuckets().stream()
                .map(bucket -> new CustomObject(bucket.getKeyAsString(), bucket.getDocCount()))
                .collect(toList());
    }
}
