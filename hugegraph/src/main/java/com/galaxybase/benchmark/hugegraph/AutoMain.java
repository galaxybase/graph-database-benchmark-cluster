package com.galaxybase.benchmark.hugegraph;

import com.galaxybase.benchmark.common.util.TestConfiguration;
import com.galaxybase.benchmark.common.util.TestManager;

/**
 * @author cl32
 * 2020/11/10
 */
public class AutoMain {
    public static void main(String[] args) {
        TestConfiguration.initConfiguation("D:\\IdeaProject\\graph-test-core\\hugegraph\\target\\");
        TestManager.startTest();
    }
}
