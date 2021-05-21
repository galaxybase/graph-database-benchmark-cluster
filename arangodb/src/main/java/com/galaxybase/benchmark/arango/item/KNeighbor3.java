package com.galaxybase.benchmark.arango.item;

import com.galaxybase.benchmark.arango.abstr.AbstractBFSMaster;

/**
 * @Author chenyanglin
 * @Date 2021/1/18 19:08
 * @Version 1.0
 */
public class KNeighbor3 extends AbstractBFSMaster {

    @Override
    public int depth() {
        return 3;
    }

    @Override
    public boolean isHop() {
        return false;
    }

}
