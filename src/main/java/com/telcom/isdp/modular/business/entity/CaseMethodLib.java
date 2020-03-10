package com.telcom.isdp.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("case_method_lib")
public class CaseMethodLib implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id" ,type = IdType.ID_WORKER)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("read_count")
    private Long readCount;

    @TableField("file_type")
    private String fileType;

    @TableField("page_count")
    private Integer pageCount;

    @TableField("upload_man")
    private String uploadMan;

    @TableField("upload_time")
    private LocalDateTime uploadTime;

    @TableField("type")
    private String type;

    @TableField("file_path")
    private String filePath;

    @TableField("is_deleted")
    private Integer isDeleted;

    @TableField("summary")
    private String summary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getReadCount() {
        return readCount;
    }

    public void setReadCount(Long readCount) {
        this.readCount = readCount;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getUploadMan() {
        return uploadMan;
    }

    public void setUploadMan(String uploadMan) {
        this.uploadMan = uploadMan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "CaseMethodLib [id=" + this.id + "," +
            "name=" + this.name + "," +
            "readCount=" + this.readCount + "," +
            "fileType=" + this.fileType + "," +
            "uploadTime=" + this.uploadTime + "," +
            "pageCount=" + this.pageCount + "," +
            "uploadMan=" + this.uploadMan + "," +
            "type=" + this.type + "," +
            "filePath=" + this.filePath + "," +
            "isDeleted=" + this.isDeleted + "," +
            "summary=" + this.summary + "," +
            "]";
    }
}
