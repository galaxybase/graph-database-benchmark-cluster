package com.galaxybase.benchmark.nebula.test;

import com.galaxybase.benchmark.common.result.data.AvgData;
import com.galaxybase.benchmark.nebula.test.util.TwoSampleTest;
import com.vesoft.nebula.Row;
import com.vesoft.nebula.client.graph.data.ResultSet;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cl32
 * 2020/11/5
 * 最短路径测试
 */
public class ShortestPathTest extends TwoSampleTest {
    private AvgData pathLength = new AvgData();

    public ShortestPathTest() {
        getResults().addData("PathLength", pathLength);
    }

    @Override
    public String resultHandle(ResultSet result) {
        List<Row> list = result.getRows();
        int n = 0;
        for (Row r : list) {
            n = r.getValues().get(0).getPVal().getSteps().size();
        }
        pathLength.addData(BigDecimal.valueOf(n));
        return "Path Length " + n;
    }

    @Override
    public String getNsql(Object[] s) {
        return String.format("FIND SHORTEST PATH FROM %s to %s OVER *  UPTO 5 STEPS", s[0], s[1]);
    }

    @Override
    public String getTestName() {
        return "ShortestPathTest";
    }
}
