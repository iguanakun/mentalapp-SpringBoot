package com.mentalapp.common.dao;

import com.mentalapp.common.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
  // 全件取得
  List<User> selectAll();

  // １件取得
  User selectByPrimaryKey(String userName);

  // 登録
  int insert(User user);

  // 更新
  int updateByPrimaryKey(User user);

  // 削除
  int deleteByPrimaryKey(Long id);
}
