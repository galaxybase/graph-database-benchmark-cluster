package com.galaxybase.benchmark.nebula.test.util;

import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.Session;

/**
 * @author cl32
 * 2020/11/5
 */
public abstract class NebulaTest extends AbstractTest<Session, ResultSet> {
    protected String vertexType;
    protected String edgeType;

    public NebulaTest() {
        TestConfiguration conf = TestConfiguration.INSTANCE;
        vertexType = conf.get("vertexType");
        edgeType = conf.get("edgeType");
    }

    @Override
    public final ResultSet onStartTest(Session session, Object[] s) throws Exception {
        return session.execute(getNsql(s));
    }

    /**
     * 子类传入nsql用
     *
     * @return
     */
    public abstract String getNsql(Object[] s);
}
