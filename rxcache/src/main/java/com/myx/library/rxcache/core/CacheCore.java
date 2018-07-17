/*
 * Copyright (C) 2017 zhouyou(478319399@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myx.library.rxcache.core;


import android.util.Log;


import com.myx.library.rxcache.model.RealEntity;
import com.myx.library.rxcache.utils.Utils;

import java.lang.reflect.Type;

import okio.ByteString;


/**
 * <p>描述：缓存核心管理类</p>
 * <p>
 * 1.采用LruDiskCache<br>
 * 2.对Key进行MD5加密<br>
 * <p>
 * 以后可以扩展 增加内存缓存，但是内存缓存的时间不好控制，暂未实现，后续可以添加》<br>
 * <p>
 * 作者： zhouyou<br>
 * 日期： 2016/12/24 10:35<br>
 * 版本： v2.0<br>
 * <p>
 * 修改者： zhouyou
 * 日期： 2017/01/06 10:35<br>
 * 1.这里笔者给读者留个提醒，ByteString其实已经很强大了，不需要我们自己再去处理加密了，只要善于发现br>
 * 2.这里为设么把MD5改成ByteString呢？其实改不改无所谓，只是想把ByteString这个好东西介绍给大家。(ok)br>
 * 3.在ByteString中有很多好用的方法包括MD5.sha1 base64  encodeUtf8 等等功能。br>
 */
public class CacheCore {
    private static final String TAG = "CacheCore";

    private LruDiskCache disk;

    public CacheCore(LruDiskCache disk) {
        this.disk = Utils.checkNotNull(disk, "disk==null");
    }


    /**
     * 读取
     */
    public synchronized <T> RealEntity<T> load(Type type, String key, long time) {
        String cacheKey = ByteString.of(key.getBytes()).md5().hex();
        Log.d(TAG, "loadCache  key=" + cacheKey);
        if (disk != null) {
            RealEntity<T> result = disk.load(type, cacheKey, time);
            if (result != null) {
                if (result.getCacheTime() == -1 || result.getUpdateDate() + result.getCacheTime() > System.currentTimeMillis()) {
                    // 未过期
                    return result;
                }

                // 已过期
                disk.remove(cacheKey);
            }
        }

        return null;
    }

    /**
     * 保存
     */
    public synchronized <T> boolean save(String key, T value) {
        String cacheKey = ByteString.of(key.getBytes()).md5().hex();
        Log.d(TAG, "saveCache  key=" + cacheKey);
        return disk.save(cacheKey, value);
    }

    /**
     * 是否包含
     *
     * @param key
     * @return
     */
    public synchronized boolean containsKey(String key) {
        String cacheKey = ByteString.of(key.getBytes()).md5().hex();
        Log.d(TAG, "containsCache  key=" + cacheKey);
        if (disk != null) {
            if (disk.containsKey(cacheKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public synchronized boolean remove(String key) {
        String cacheKey = ByteString.of(key.getBytes()).md5().hex();
        Log.d(TAG, "removeCache  key=" + cacheKey);
        if (disk != null) {
            return disk.remove(cacheKey);
        }
        return true;
    }

    /**
     * 清空缓存
     */
    public synchronized boolean clear() {
        if (disk != null) {
            return disk.clear();
        }
        return false;
    }

}
