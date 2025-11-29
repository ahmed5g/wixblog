package com.tech.wixblog.dto.payload;

import com.tech.wixblog.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsResponse  {

    private Long totalUsers;
    private Long activeUsers;
    private Long inactiveUsers;
    private Map<Role, Long> usersByRole;
}