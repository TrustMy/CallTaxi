package com.trust.shengyu.rentalcarclient.tools.beans.oldbeans;

/**
 * Created by Trust on 2017/9/6.
 */

public class UpdataApp {

    /**
     * status : 1
     * info : 根据appType查询latest(最新)已发布的 版本信息成功
     * code : null
     * type : null
     * content : {uid:2,versionNo:2,description:有新版本啦，请尽快升级吧,url:http://139.196.229.233/changan/update/CAGps-4.2.apk,appType:1,appPlatform:1,latest:1,uploadTime:Aug 29, 2017 3:41:31 PM,userAccount:yes,publishTime:Aug 29, 2017 5:32:58 PM,status:1,version:2}
     */

    private int status;
    private String info;
    private Object code;
    private Object type;
    private UpdataAppData content;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public UpdataAppData getContent() {
        return content;
    }

    public void setContent(UpdataAppData content) {
        this.content = content;
    }
}
