package com.galaxybase.benchmark.hugegraph.database;

import com.alibaba.fastjson.JSONObject;
import com.baidu.hugegraph.driver.GremlinManager;
import com.baidu.hugegraph.driver.HugeClient;
import com.baidu.hugegraph.driver.HugeClientBuilder;
import com.baidu.hugegraph.structure.gremlin.ResultSet;
import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.galaxybase.benchmark.hugegraph.util.HttpConnect;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author cl32
 * 2020/11/10
 */
public class Huge implements DataBase<Huge.HugeConnect> {
    private HugeConnect hugeConnect;

    @Override
    public HugeConnect getDataBase() {
        if (hugeConnect == null) {
            initDataBase(TestConfiguration.INSTANCE);
        }
        return hugeConnect;
    }

    @Override
    public void initDataBase(TestConfiguration conf) {
        hugeConnect = new HugeConnect(String.format("http://%s:%s", conf.get("ip"), conf.get("port")),
                conf.getGraphName(),
                conf.getTimeout() / 1000,
                conf.get("vertexType"),
                conf.get("edgeType"));
    }

    @Override
    public void close() {
        hugeConnect.close();
    }

    public final class HugeConnect {
        private HugeClient hugeClient;
        private GremlinManager gremlinManager;
        private String host;
        private String edgeType;
        private String vertexType;
        private int vertexLabelId;

        public HugeConnect(String host, String graphName, int timeout, String vertexType, String edgeType) {
            this.host = String.format("%s/graphs/%s", host, graphName);
            this.hugeClient = new HugeClient(new HugeClientBuilder(host, graphName)
                    .configTimeout(timeout));
            this.gremlinManager = hugeClient.gremlin();
            this.edgeType = edgeType;
            this.vertexType = vertexType;
            this.vertexLabelId = getVertexLabelId(vertexType);
        }

        /**
         * ??????????????????
         *
         * @param startId
         * @param endId
         * @return
         */
        public int shortestPath(String startId, String endId) {
            try {
                startId = URLEncoder.encode(String.format("\"%d:%s\"", vertexLabelId, startId), "UTF-8");
                endId = URLEncoder.encode(String.format("\"%d:%s\"", vertexLabelId, endId), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String params = String.format("source=%s&target=%s&max_depth=5&direction=OUT", startId, endId);
            JSONObject jsonObject = JSONObject.parseObject(
                    HttpConnect.sendGet(host + "/traversers/shortestpath", params));
            return jsonObject.getJSONArray("path").size() - 1;
        }

        /**
         * ?????????????????????????????????id
         *
         * @param vertexLabel ?????????
         * @return ?????????id
         */
        public int getVertexLabelId(String vertexLabel) {
            JSONObject jsonObject = JSONObject.parseObject(
                    HttpConnect.sendGet(String.format("%s/schema/vertexlabels/%s", host, vertexLabel), ""));
            return jsonObject.getInteger("id");
        }

        /**
         * ??????gremlin??????
         *
         * @param gremlin gremlin??????
         * @return
         */
        public ResultSet executeGremlin(String gremlin) {
            return gremlinManager.gremlin(gremlin).execute();
        }

        public String getEdgeType() {
            return edgeType;
        }

        public String getVertexType() {
            return vertexType;
        }

        public int getVertexLabelId() {
            return vertexLabelId;
        }

        public HugeClient getHugeClient() {
            return hugeClient;
        }

        /**
         * ?????????????????????
         */
        public void close() {
            hugeClient.close();
        }
    }
}