package com.example.project.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
* 用户关注
* </p>
*
* @author 
* @since 2024-04-01
*/
@Getter
@Setter
@ApiModel(value = "Follow对象", description = "用户关注")
public class Follow implements Serializable {

private static final long serialVersionUID = 1L;

    // 编号
    @ApiModelProperty("编号")
    @Alias("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    // 关注用户
    @ApiModelProperty("关注用户")
    @Alias("关注用户")
    private Integer userId;


    // 被关注用户
    @ApiModelProperty("被关注用户")
    @Alias("被关注用户")
    private Integer followId;



}