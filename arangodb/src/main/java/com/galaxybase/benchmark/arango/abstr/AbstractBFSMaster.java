package com.galaxybase.benchmark.arango.abstr;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.common.result.data.ResultData;
import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import com.galaxybase.benchmark.common.util.TestConfiguration;

import java.math.BigDecimal;

/**
 * @Author chenyanglin
 * @Date 2021/1/18 11:14
 * @Version 1.0
 */
public abstract class AbstractBFSMaster extends AbstractTest<ArangoDatabase, Long> {

    // 记录测试结果
    private ResultData resultData;

    public AbstractBFSMaster() {
        resultData = new AvgData();
        getResults().addData((isHop() ? "KHop-" : "KNeighbor-") + depth(), resultData);
    }

    @Override
    public Long onStartTest(ArangoDatabase db, Object[] s) throws Exception {
        String query = String.format("return count(FOR v IN 1..%s OUTBOUND 'Person/%s' GRAPH '%s' RETURN distinct v._id)", depth(), s[0], TestConfiguration.instance().getGraphName());
        ArangoCursor<Object> result = db.query(query, Object.class);
        return Long.valueOf(result.next() + "");
    }

    @Override
    public String resultHandle(Long result) {
        // 将结果添加到resultData，用于最后求平均值
        resultData.addData(new BigDecimal(result));
        return "VertexCount: " + result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[] {sample.get(n)};
    }

    @Override
    public String getTestName() {
        return (isHop() ? "KHop-" : "KNeighbor-") + depth();
    }

    public abstract int depth();

    public abstract boolean isHop();

}
