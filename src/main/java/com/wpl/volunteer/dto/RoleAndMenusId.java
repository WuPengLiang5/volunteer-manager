package com.wpl.volunteer.dto;

import com.wpl.volunteer.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class RoleAndMenusId extends Role {
    private List<Integer> menuIds;
}
