package com.galaxybase.benchmark.janus.function;


import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;

public abstract class KNeighbor extends AbstractTest<GraphTraversalSource, String> {
    protected abstract int depth();

    @Override
    public String onStartTest(GraphTraversalSource g, Object[] s) throws Exception {
        int size = g.V().has("id", s[0]).repeat(__.out().dedup()).times(depth()).emit().toSet().size();
        return String.valueOf(size);
    }

    @Override
    public String resultHandle(String result) {
        return result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[]{Integer.parseInt(sample.get(n))};
    }

    @Override
    public String getTestName() {
        return "K-neighbor" + depth();
    }
}
