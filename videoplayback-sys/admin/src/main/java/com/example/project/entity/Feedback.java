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
* 用户反馈
* </p>
*
* @author 
* @since 2024-04-01
*/
@Getter
@Setter
@ApiModel(value = "Feedback对象", description = "用户反馈")
public class Feedback implements Serializable {

private static final long serialVersionUID = 1L;

    // 编号
    @ApiModelProperty("编号")
    @Alias("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    // 反馈标题
    @ApiModelProperty("反馈标题")
    @Alias("反馈标题")
    private String name;


    // 反馈内容
    @ApiModelProperty("反馈内容")
    @Alias("反馈内容")
    private String content;


    // 反馈用户
    @ApiModelProperty("反馈用户")
    @Alias("反馈用户")
    private Integer userId;


    // 反馈时间
    @ApiModelProperty("反馈时间")
    @Alias("反馈时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    // 平台回复
    @ApiModelProperty("平台回复")
    @Alias("平台回复")
    private String reply;



}