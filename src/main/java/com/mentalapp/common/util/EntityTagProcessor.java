package com.mentalapp.common.util;

import com.mentalapp.common.entity.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** エンティティとタグの関連付けを処理するクラス */
@Component
@RequiredArgsConstructor
public class EntityTagProcessor {

  private final TagUtils tagUtils;

  /**
   * エンティティにタグを関連付ける
   *
   * @param entityId エンティティID
   * @param tagNames スペース区切りのタグ名
   * @param userId ユーザーID
   * @param operator タグ関連操作インターフェース
   * @return 処理されたタグのリスト
   */
  @Transactional
  public List<Tag> processEntityTags(
      Long entityId, String tagNames, Long userId, TagRelationOperator operator) {
    // 既存の関連を削除
    operator.deleteTagRelationsByEntityId(entityId);

    // タグを処理
    List<Tag> tags = tagUtils.processTagNames(tagNames, userId);

    // 新しい関連を作成
    tags.forEach(tag -> operator.insertTagRelation(entityId, tag.getId()));

    return tags;
  }
}
