package com.kar20240703.be.temp.web.util;

import cn.hutool.core.map.MapUtil;

public class MyMapUtil {

    public static int getInitialCapacity(int size) {

        return (int)(size / MapUtil.DEFAULT_LOAD_FACTOR) + 1;

    }

}
