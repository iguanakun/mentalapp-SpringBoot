package com.mentalapp.common.util;

import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.entity.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** タグ処理のための共通ユーティリティクラス */
@Component
@RequiredArgsConstructor
@Slf4j
public class TagUtils {

  private final TagMapper tagMapper;

  /**
   * スペース（半角・全角）区切りのタグ文字列からタグリストを作成または取得する
   *
   * @param tagNames スペース（半角・全角）区切りのタグ名
   * @param userId ユーザーID
   * @return 処理されたタグのリスト
   */
  public List<Tag> processTagNames(String tagNames, Long userId) {
    if (Objects.isNull(tagNames) || tagNames.trim().isEmpty()) {
      return List.of();
    }

    // スペース（半角・全角）で区切られたタグを分割
    String[] tagArray = tagNames.trim().split("[\\s\u3000]+");

    return Arrays.stream(tagArray)
        .filter(tagName -> !tagName.trim().isEmpty())
        .map(tagName -> findOrCreateTag(tagName, userId))
        .collect(Collectors.toList());
  }

  /**
   * タグ名からタグを検索し、存在しない場合は新規作成する
   *
   * @param tagName タグ名
   * @param userId ユーザーID
   * @return 既存または新規作成されたタグ
   */
  public Tag findOrCreateTag(String tagName, Long userId) {
    try {
      validateTagName(tagName);

      // タグが存在するか確認
      Tag tag = tagMapper.findByTagName(tagName);

      // 存在しない場合は新規作成
      if (Objects.isNull(tag)) {
        tag = new Tag();
        tag.setTagName(tagName);
        tag.setUserId(userId);
        tagMapper.insert(tag);
      }

      return tag;
    } catch (Exception e) {
      // ログ出力
      log.error("タグの検索/作成中にエラーが発生しました: {}", e.getMessage(), e);
      throw new RuntimeException("タグの処理中にエラーが発生しました", e);
    }
  }

  /**
   * タグ名のバリデーションを行う
   *
   * @param tagName バリデーション対象のタグ名
   */
  private void validateTagName(String tagName) {
    if (Objects.isNull(tagName) || tagName.trim().isEmpty()) {
      throw new IllegalArgumentException("タグ名は空にできません");
    }

    if (tagName.length() > 50) {
      throw new IllegalArgumentException("タグ名は50文字以内にしてください");
    }
  }

  /**
   * タグ名のリストを結合してスペース区切りの文字列に変換する
   *
   * @param tags タグのリスト
   * @return スペース区切りのタグ名文字列
   */
  public String convertTagsToString(List<Tag> tags) {
    if (Objects.isNull(tags) || tags.isEmpty()) {
      return "";
    }

    return tags.stream().map(Tag::getTagName).collect(Collectors.joining(" "));
  }

  /**
   * ユーザーIDに基づいてユーザーのタグを取得する
   *
   * @param userId ユーザーID
   * @return ユーザーのタグリスト
   */
  public List<Tag> getUserTags(Long userId) {
    return tagMapper.findByUserId(userId);
  }
}
