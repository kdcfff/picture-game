package com.itkd.picture.model.enums;

import com.itkd.picture.exception.BusinessException;
import com.itkd.picture.exception.ErrorCode;
import com.itkd.picture.exception.ThrowUtils;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public enum PictureReviewStatusEnum {  
    REVIEWING("待审核", 0),  
    PASS("通过", 1),  
    REJECT("拒绝", 2);
  
    private final String text;

    private final int value;



    PictureReviewStatusEnum(String text, int value) {  
        this.text = text;  
        this.value = value;  
    }  
  
    /**  
     * 根据 value 获取枚举  
     */  
    public static PictureReviewStatusEnum getEnumByValue(Integer value) {
       if (value==null){
           return null;
       }
       for (PictureReviewStatusEnum anEnum : PictureReviewStatusEnum.values()) {
           if (anEnum.value == value) {
               return anEnum;
           }
       }
       return null;
    }  
}
