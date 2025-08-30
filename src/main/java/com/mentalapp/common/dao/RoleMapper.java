package com.mentalapp.common.dao;

import com.mentalapp.common.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    // 全件取得
    List<Role> selectAll();

    // １件取得 (ID)
    Role selectByPrimaryKey(Long id);
    
    // 名前で取得
    Role findRoleByName(String name);

    // 登録
    int insert(Role role);

    // 更新
    int updateByPrimaryKey(Role role);

    // 削除
    int deleteByPrimaryKey(Long id);
    
    // ユーザーIDに基づいてロールを取得
    List<Role> findRolesByUserId(Long userId);
}