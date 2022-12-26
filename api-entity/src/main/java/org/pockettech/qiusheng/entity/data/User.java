package org.pockettech.qiusheng.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Integer uid;
    private String user_name;
    private String password;
    private String salt;

    public User(Integer uid, String user_name) {
        this.uid = uid;
        this.user_name = user_name;
    }
}
