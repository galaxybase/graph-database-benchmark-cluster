package com.galaxybase.benchmark.tiger;

import com.galaxybase.benchmark.common.util.TestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author chenyanglin
 * @Date 2020/11/11 14:06
 * @Version 1.0
 */
public class AutoMain {

    private static final List<Long> times = new ArrayList<>(2000001);
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
        TestManager.startTest();
        System.exit(-1);
    }

}
