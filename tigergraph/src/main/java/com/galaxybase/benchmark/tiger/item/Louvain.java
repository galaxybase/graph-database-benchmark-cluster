package com.galaxybase.benchmark.tiger.item;

import com.galaxybase.benchmark.tiger.abstr.AbstractAlgorithm;
import com.galaxybase.benchmark.tiger.database.TigerGraph;

/**
 * @Author chenyanglin
 * @Date 2020/12/14 13:57
 * @Version 1.0
 */
public class Louvain extends AbstractAlgorithm {

    @Override
    public String getTestName() {
        return "Louvain";
    }

    @Override
    public String invokeQuery(TigerGraph graph, Object[] s) {
        return graph.queryRequest("Louvain");
    }

}