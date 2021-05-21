package com.galaxybase.benchmark.nebula.database;

import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;

import java.util.Arrays;
import java.util.List;

import static com.galaxybase.benchmark.common.util.LoggerUtil.LOG;

/**
 * @author cl32
 * 2020/11/5
 */
public class Nebula implements DataBase<Session> {

    private Session session;
    private NebulaPool pool;

    @Override
    public Session getDataBase() {
        if (session == null) {
            initDataBase(TestConfiguration.INSTANCE);
        }
        return session;
    }

    @Override
    public void initDataBase(TestConfiguration conf) {
        NebulaPoolConfig config = new NebulaPoolConfig();
        config.setMaxConnSize(10);
        config.setTimeout(conf.getTimeout());
        List<HostAddress> addresses = Arrays.asList(new HostAddress("" + conf.get("ip"), conf.get("port")));
        pool = new NebulaPool();
        try {
            pool.init(addresses, config);
            session = pool.getSession(conf.get("user"), conf.get("password"), false);
            session.execute("Use " + conf.getGraphName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            pool.close();
        } catch (Exception e) {
            LOG.error("The Nebula database connection failed to close!", e);
        }
    }

}
