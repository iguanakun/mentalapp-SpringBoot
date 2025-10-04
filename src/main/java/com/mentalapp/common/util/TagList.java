package com.mentalapp.common.util;

import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.entity.Tag;
import com.mentalapp.common.exception.MentalSystemException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/** タグリストを管理するユーティリティクラス */
@Slf4j
public class TagList {
  private final List<Tag> tagList;
  private final TagMapper tagMapper;

  /**
   * タグリストを初期化するコンストラクタ
   *
   * @param tagNames スペース区切りのタグ名文字列
   * @param userId ユーザーID
   * @param tagMapper タグマッパー
   */
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
   * タグリストを初期化するコンストラクタ
   *
   * @param tagList タグのリスト
   * @param tagMapper タグマッパー
   */
  public TagList(List<Tag> tagList, TagMapper tagMapper) {
    // タグリストを初期化
    this.tagList = tagList;
    // tagMapperを初期化
    this.tagMapper = tagMapper;
  }

  /**
   * タグ名のバリデーションを行い、スペース区切りで分割したタグ名リストを作成
   *
   * @param tagNames バリデーション対象のタグ名
   * @return 重複を排除したタグ名の配列
   */
  private String[] createTagSplitList(String tagNames) {
    if (tagNames.length() > 50) {
      throw new IllegalArgumentException("タグ名は50文字以内にしてください");
    }

    // タグを半角スペース、全角スペース区切りで分割
    String[] splitTagList = tagNames.trim().split("[\\\\s\u3000]+");

    // 重複を排除
    return Arrays.stream(splitTagList).distinct().toArray(String[]::new);
  }

  /**
   * タグ名とユーザーIDからタグエンティティを作成する
   *
   * @param tagName タグ名
   * @param userId ユーザーID
   * @return 作成されたタグエンティティ
   */
  private Tag createTag(String tagName, Long userId) {
    Tag tag = new Tag();
    tag.setTagName(tagName);
    tag.setUserId(userId);

    return tag;
  }

  /**
   * タグリストをデータベースに登録する 既に存在するタグは登録しない
   *
   * @throws MentalSystemException データベース操作中にエラーが発生した場合
   */
  public void insertTagList() throws MentalSystemException {
    // タグリストを登録。作成済みのタグは除く
    tagList.forEach(
        tag -> {
          // タグが作成済みでなければ、登録
          if (Objects.isNull(tagMapper.findByTagNameAndUserId(tag.getTagName(), tag.getUserId()))) {
            tagMapper.insert(tag);
          }
        });
  }

  /**
   * モニタリングに関連するタグ中間テーブルの登録
   *
   * @param tagRelationMapper タグ関連マッパー
   * @param monitoringId モニタリングID
   * @throws MentalSystemException データベース操作中にエラーが発生した場合
   */
  public void insertMonitoringTagRelation(TagRelationMapper tagRelationMapper, Long monitoringId)
      throws MentalSystemException {
    // タグIDリストを抽出
    List<Long> tagIdList = extractTagIdList();
    // タグの中間テーブルへの関連付け
    tagIdList.forEach(tagId -> tagRelationMapper.insert(monitoringId, tagId));
  }

  /**
   * データベースからタグリストを取得する
   *
   * @return タグエンティティのリスト
   * @throws MentalSystemException データベース操作中にエラーが発生した場合
   */
  public List<Tag> selectTagList() throws MentalSystemException {
    // tagListフィールドの全タグリストを取得
    return tagList.stream()
        .map(tag -> tagMapper.findByTagNameAndUserId(tag.getTagName(), tag.getUserId()))
        .filter(Objects::nonNull) // nullを除外
        .collect(Collectors.toList());
  }

  /**
   * タグIDのリストを抽出する
   *
   * @return タグIDのリスト
   * @throws MentalSystemException データベース操作中にエラーが発生した場合
   */
  private List<Long> extractTagIdList() throws MentalSystemException {
    // タグIDのみ抽出。主に中間テーブルへの書き込みに使用
    return selectTagList().stream()
        .filter(Objects::nonNull)
        .map(Tag::getId)
        .collect(Collectors.toList());
  }

  /**
   * タグ名のリストを抽出する
   *
   * @return タグ名のリスト
   * @throws MentalSystemException データベース操作中にエラーが発生した場合
   */
  private List<String> extractTagNameList() throws MentalSystemException {
    // タグ名のみ抽出
    return selectTagList().stream()
        .filter(Objects::nonNull)
        .map(Tag::getTagName)
        .collect(Collectors.toList());
  }

  /**
   * タグ名のリストをスペース区切りの文字列に変換する
   *
   * @return スペース区切りのタグ名文字列
   * @throws MentalSystemException データベース操作中にエラーが発生した場合
   */
  public String tagNamesToString() throws MentalSystemException {
    List<String> tagNames = extractTagNameList();
    return String.join(" ", tagNames);
  }
}
