package de.tel_ran.SHTENTSEL_JAVA_BOT.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class CurrencyDto {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("alphaCode")
    @Expose
    private String alphaCode;
    @SerializedName("numericCode")
    @Expose
    private String numericCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rate")
    @Expose
    private Double rate;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("inverseRate")
    @Expose
    private Double inverseRate;
}
