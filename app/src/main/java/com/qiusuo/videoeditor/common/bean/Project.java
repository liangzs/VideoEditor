package com.qiusuo.videoeditor.common.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 序列化时把bitmap对象忽略，反序列重现加载bitmap对象
 */

/**
 * 采用sqlite存储本地数据，子项item
 */
public class Project implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private String projectId;
    private String copyId;
    private Date createTime;
    private Date updateTime;
    private long duration;
    private String coverPath;
    private String projectName;
    private String pathName;
    private int tranGroup;


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCopyId() {
        return copyId;
    }

    public void setCopyId(String copyId) {
        this.copyId = copyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public int getTranGroup() {
        return tranGroup;
    }

    public void setTranGroup(int tranGroup) {
        this.tranGroup = tranGroup;
    }
}
