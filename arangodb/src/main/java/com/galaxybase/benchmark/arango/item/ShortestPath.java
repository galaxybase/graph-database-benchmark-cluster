package com.galaxybase.benchmark.arango.item;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.common.result.data.ResultData;
import com.galaxybase.benchmark.common.result.data.SumData;
import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.common.util.TestConfiguration;

import java.math.BigDecimal;

public class ShortestPath extends AbstractTest<ArangoDatabase, Long> {
    String graphName;
    // 记录测试结果
    private ResultData resultData;
    // 记录测试结果
    private ResultData resultData2;

    public ShortestPath() {
        TestConfiguration conf = TestConfiguration.INSTANCE;
        graphName = conf.getGraphName();
        resultData = new SumData();
        getResults().addData("总路径长度", resultData);
        resultData2 = new AvgData();
        getResults().addData("平均路径长度", resultData2);
    }

    @Override
    public Long onStartTest(ArangoDatabase db, Object[] s){

        String query = String.format("return count(FOR v,e\n" +
                "  IN OUTBOUND\n" +
                "  SHORTEST_PATH 'Person/%s' TO 'Person/%s'\n" +
                "  GRAPH '%s' RETURN [v._key, e._key])", s[0], s[1], graphName);
        ArangoCursor<Object> result = db.query(query, Object.class);
        return Long.valueOf(result.next() + "") - 1;
    }

    @Override
    public String resultHandle(Long result) {
        // 将结果添加到resultData，用于最后求平均值
        resultData.addData(new BigDecimal(result));
        resultData2.addData(new BigDecimal(result));
//        return "VertexCount: " + result;
        return "路径长度：" + result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return sample.get(n).split(",");
    }

    @Override
    public String getTestName() {
        return "Shortest Path";
    }
}
