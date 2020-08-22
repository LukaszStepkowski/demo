package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GoldDataDto {

//    @JsonProperty(value = "Data")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date data;

//    @JsonProperty(value = "cena")
    private Double cena;
}
