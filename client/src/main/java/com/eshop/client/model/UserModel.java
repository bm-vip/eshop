package com.eshop.client.model;

import com.eshop.client.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class UserModel extends BaseModel<Long> {
    @NotNull
    @NotBlank
    private String userName;
    @Email
    private String email;
    @NotNull
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    private Boolean active;
    private String uid;
    private String referralCode;
    private UserModel parent;
    private String treePath;
    private int childCount;
    private String walletAddress;
    private String profileImageUrl;
    private CountryModel country;
    private Set<RoleModel> roles;
    private BigDecimal deposit;
    private BigDecimal withdrawal;
    private BigDecimal bonus;
    private BigDecimal reward;

    public UserModel setUserId(Long id) {
        setId(id);
        return this;
    }
    public List<Long> getParents(){
        if(treePath == null)
            return null;
        String parents = treePath.replace("," + getId(),"");
        return StringUtils.reverseArrayFromString(parents);
    }
}
