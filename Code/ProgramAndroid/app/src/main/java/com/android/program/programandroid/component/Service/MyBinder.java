package com.android.program.programandroid.component.Service;

import android.os.Binder;

/**
 * Created by wangjie on 2017/9/8.
 * please attention weixin get more info
 * weixin number: ProgramAndroid
 * 微信公众号： 程序员Android
 */
/**
* 该类提供 绑定组件与绑定服务提供接口
* */
public class MyBinder extends Binder {
   private int count = 0;

    public int getBindCount() {
        return ++count;
    }
    public int getUnBindCount() {
        return count> 0 ? count-- : 0;
    }
}
