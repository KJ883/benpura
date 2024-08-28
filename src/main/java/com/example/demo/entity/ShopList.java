package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopList {
	@Id
    private Integer id;

    private String shopName;
    private String shopAddress;
    private Integer shopTel;
    private String shopHour;
    private String holiday;
    private byte[] shopPicture;
    
//    private String shopHourStart;
//    private String shopHourEnd;
    
}
