package com.mentalapp.common.util;

import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.entity.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TagList {
  private final List<Tag> tagList;
  private final TagMapper tagMapper;

  public TagList(String tagNames, Long userId, TagMapper tagMapper) {
    // スペース区切りで分割、及び重複排除したタグ名リストを作成
    String[] tagSplitList = createTagSplitList(tagNames);

    // タグリストを作成
    this.tagList =
        Arrays.stream(tagSplitList)
            .map(tag -> createTag(tag, userId)) // タグを作成
            .collect(Collectors.toList());

    // tagMapperを初期化
    this.tagMapper = tagMapper;
  }

  /**
   * タグ名のバリデーションを行い、スペース区切りで分割したタグ名リストを作成
   *
   * @param tagNames バリデーション対象のタグ名
   */
  private String[] createTagSplitList(String tagNames) {
    if (tagNames.length() > 50) {
      throw new IllegalArgumentException("タグ名は50文字以内にしてください");
    }

    // タグを半角スペース、全角スペース区切りで分割
    String[] splitTagList = tagNames.trim().split("[\\s\u3000]+");

    // 重複を排除
    return Arrays.stream(splitTagList).distinct().toArray(String[]::new);
  }

  /**
   * タグ名からタグを検索し、存在しない場合は新規作成する
   *
   * @param tagName タグ名
   * @param userId ユーザーID
   * @return 既存または新規作成されたタグ
   */
  public Tag createTag(String tagName, Long userId) {
    Tag tag = new Tag();
    tag.setTagName(tagName);
    tag.setUserId(userId);

    return tag;
  }

  public void insertTagList() {
    try {
      // タグリストを登録。作成済みのタグは除く
      tagList.forEach(
          tag -> {
            // タグが作成済みでなければ、登録
            if (Objects.isNull(
                tagMapper.findByTagNameAndUserId(tag.getTagName(), tag.getUserId()))) {
              tagMapper.insert(tag);
            }
          });
    } catch (Exception e) {
      // ログ出力
      log.error("タグ作成中にエラーが発生しました: {}", e.getMessage(), e);
      throw new RuntimeException("タグの処理中にエラーが発生しました", e);
    }
  }

  public List<Long> extractTagIdList() {
    // タグIDのみ抽出。主に中間テーブルへの書き込みに使用
    return selectTagList().stream().map(Tag::getId).collect(Collectors.toList());
  }

  public List<Tag> selectTagList() {
    // tagListフィールドの全タグリストを取得
    return tagList.stream()
        .map(tag -> tagMapper.findByTagNameAndUserId(tag.getTagName(), tag.getUserId()))
        .collect(Collectors.toList());
  }
}
