package com.myx.library.rxcache.callback;




import com.myx.library.rxcache.utils.Utils;

import java.lang.reflect.Type;

/**
 * Created by holenzhou on 2018/5/11.
 */

public class CacheTypeToken<T> implements IType<T> {

    @Override
    public Type getType() {
        //获取需要解析的泛型T类型
        Type fullType = Utils.findNeedClass(getClass());
        return fullType;
    }

}
