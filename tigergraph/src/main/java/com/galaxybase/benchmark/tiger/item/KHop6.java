package com.galaxybase.benchmark.tiger.item;

import com.galaxybase.benchmark.tiger.abstr.AbstractBFSMaster;

/**
 * @Author chenyanglin
 * @Date 2020/12/11 20:46
 * @Version 1.0
 */
public class KHop6 extends AbstractBFSMaster {

    @Override
    public int getDepth() {
        return 6;
    }

    @Override
    public boolean isHop() {
        return true;
    }

}
