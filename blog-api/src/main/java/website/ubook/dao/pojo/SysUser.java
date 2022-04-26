package website.ubook.dao.pojo;

import lombok.Data;

@Data
public class SysUser {

    //默认id类型，MybatisPlus使用雪花算法，默认不是自增的
    //以后用户多了，要进行百分表操作，id就需要分布式的了
    private Long id;

    private String account;

    private Integer admin;

    private String avatar;

    private Long createDate;

    private Integer deleted;

    private String email;

    private Long lastLogin;

    private String mobilePhoneNumber;

    private String nickname;

    private String password;

    private String salt;

    private String status;
}
