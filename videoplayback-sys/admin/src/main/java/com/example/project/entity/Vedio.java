package com.example.project.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import cn.hutool.core.annotation.Alias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.project.common.LDTConfig;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
* <p>
* 视频
* </p>
*
* @author 
* @since 2024-04-01
*/
@Getter
@Setter
@ApiModel(value = "Vedio对象", description = "视频")
public class Vedio implements Serializable {

private static final long serialVersionUID = 1L;

    // 编号
    @ApiModelProperty("编号")
    @Alias("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    // 视频分类
    @ApiModelProperty("视频分类")
    @Alias("视频分类")
    private Integer categoryId;


    // 视频名称
    @ApiModelProperty("视频名称")
    @Alias("视频名称")
    private String name;


    // 视频封面
    @ApiModelProperty("视频封面")
    @Alias("视频封面")
    private String img;


    // 视频简介
    @ApiModelProperty("视频简介")
    @Alias("视频简介")
    private String content;


    // 视频文件
    @ApiModelProperty("视频文件")
    @Alias("视频文件")
    private String vedio;


    // 上传用户
    @ApiModelProperty("上传用户")
    @Alias("上传用户")
    private Integer userId;


    // 上传时间
    @ApiModelProperty("上传时间")
    @Alias("上传时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    // 状态,审核中|审核通过|审核失败
    @ApiModelProperty("状态,审核中|审核通过|审核失败")
    @Alias("状态,审核中|审核通过|审核失败")
    private String stateRadio;


    // 失败原因
    @ApiModelProperty("失败原因")
    @Alias("失败原因")
    private String reason;


    // 访问量
    @ApiModelProperty("访问量")
    @Alias("访问量")
    private Integer views;



}