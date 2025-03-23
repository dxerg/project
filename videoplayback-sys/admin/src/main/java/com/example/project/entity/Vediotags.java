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
* 视频标签关联
* </p>
*
* @author 
* @since 2024-04-01
*/
@Getter
@Setter
@ApiModel(value = "Vediotags对象", description = "视频标签关联")
public class Vediotags implements Serializable {

private static final long serialVersionUID = 1L;

    // 编号
    @ApiModelProperty("编号")
    @Alias("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    // 视频
    @ApiModelProperty("视频")
    @Alias("视频")
    private Integer vedioId;


    // 标签
    @ApiModelProperty("标签")
    @Alias("标签")
    private Integer tagsId;



}