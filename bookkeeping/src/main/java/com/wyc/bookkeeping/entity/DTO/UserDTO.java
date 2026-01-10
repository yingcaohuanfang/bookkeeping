package com.wyc.bookkeeping.entity.DTO;

import com.wyc.bookkeeping.entity.User;
import lombok.Data;

/**
 * @author 王亚川
 */
@Data
public class UserDTO extends User {
    private String nickname;
    private String Invoices;
}
