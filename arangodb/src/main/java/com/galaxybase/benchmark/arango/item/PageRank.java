package com.galaxybase.benchmark.arango.item;

import com.arangodb.ArangoDatabase;
import com.arangodb.velocystream.Request;
import com.arangodb.velocystream.RequestType;
import com.arangodb.velocystream.Response;
import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.common.util.TestConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author chenyanglin
 * @Date 2021/3/3 14:40
 * @Version 1.0
 */
public class PageRank extends AbstractTest<ArangoDatabase, String> {

    @Override
    public String onStartTest(ArangoDatabase db, Object[] s) throws Exception {
        Request request = new Request("_system", RequestType.POST, "/_api/control_pregel");
        Map<String, Object> body = new HashMap<>();
        body.put("algorithm", "pagerank");
        body.put("graphName", TestConfiguration.INSTANCE.getGraphName());
        request.setBody(db.util().serialize(body));
        Response response = db.arango().execute(request);
        return response.toString();
    }

    @Override
    public String resultHandle(String result) {
        return result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[0];
    }

    @Override
    public String getTestName() {
        return "PageRank";
    }

}
