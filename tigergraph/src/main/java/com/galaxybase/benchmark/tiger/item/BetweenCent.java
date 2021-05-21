package com.galaxybase.benchmark.tiger.item;

import com.galaxybase.benchmark.tiger.abstr.AbstractAlgorithm;
import com.galaxybase.benchmark.tiger.database.TigerGraph;

/**
 * @Author chenyanglin
 * @Date 2020/12/14 13:54
 * @Version 1.0
 */
public class BetweenCent extends AbstractAlgorithm {

    @Override
    public String getTestName() {
        return "Between_Cent";
    }

    @Override
    public String invokeQuery(TigerGraph graph, Object[] s) {
        return graph.queryRequest("Between_Cent");
    }

}