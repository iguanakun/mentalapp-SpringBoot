package com.mentalapp.cbt_basic.util;

import com.mentalapp.cbt_basic.dao.CbtBasicsTagRelationMapper;
import com.mentalapp.common.util.TagRelationOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** CbtBasics用のタグ関連操作実装クラス */
@Component
@RequiredArgsConstructor
public class CbtBasicsTagRelationOperator implements TagRelationOperator {

  private final CbtBasicsTagRelationMapper cbtBasicsTagRelationMapper;

  @Override
  public int insertTagRelation(Long cbtBasicId, Long tagId) {
    return cbtBasicsTagRelationMapper.insert(cbtBasicId, tagId);
  }

  @Override
  public int deleteTagRelationsByEntityId(Long cbtBasicId) {
    return cbtBasicsTagRelationMapper.deleteByCbtBasicId(cbtBasicId);
  }
}
