package com.mentalapp.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mentalapp.common.TestUtils;
import com.mentalapp.common.dao.TagMapper;
import com.mentalapp.common.entity.Tag;
import com.mentalapp.common.exception.MentalSystemException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** タグリスト管理ユーティリティクラスのテスト */
@ExtendWith(MockitoExtension.class)
public class TagListTest {

  @Mock private TagMapper tagMapper;

  @Mock private TagRelationMapper tagRelationMapper;

  private Long userId;
  private List<Tag> testTags;

  @BeforeEach
  public void setup() {
    userId = 1L;
    testTags = TestUtils.createTags();
  }

  /**
   * コンストラクタ（String版）のテスト - 通常のタグ名文字列
   *
   * <p>スペース区切りのタグ名文字列から正しくTagListが作成されることを検証
   */
  @Test
  public void testConstructorWithString_Normal() {
    // 実行
    TagList tagList = new TagList("タグ1 タグ2 タグ3", userId, tagMapper);

    // 検証
    assertNotNull(tagList);
  }

  /**
   * コンストラクタ（String版）のテスト - 全角・半角スペース混在
   *
   * <p>全角・半角スペースが混在するタグ名文字列を正しく分割できることを検証
   */
  @Test
  public void testConstructorWithString_MixedSpaces() {
    // 実行
    TagList tagList = new TagList("タグ1　タグ2 タグ3　　タグ4", userId, tagMapper);

    // 検証
    assertNotNull(tagList);
  }

  /**
   * コンストラクタ（String版）のテスト - 重複タグ名の排除
   *
   * <p>重複するタグ名が正しく排除されることを検証
   */
  @Test
  public void testConstructorWithString_DuplicateTags() {
    // 実行: 重複するタグ名を含む文字列
    TagList tagList = new TagList("タグ1 タグ2 タグ1 タグ3 タグ2", userId, tagMapper);

    // 検証: TagListが作成されることを確認
    assertNotNull(tagList);
  }

  /**
   * コンストラクタ（String版）のテスト - 50文字超過時の例外
   *
   * <p>タグ名文字列が50文字を超える場合にIllegalArgumentExceptionが発生することを検証
   */
  @Test
  public void testConstructorWithString_ExceedsMaxLength() {
    // 51文字のタグ名文字列を作成
    String longTagNames = "あ".repeat(51);

    // 実行と検証
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> new TagList(longTagNames, userId, tagMapper));

    assertEquals("タグ名は50文字以内にしてください", exception.getMessage());
  }

  /**
   * コンストラクタ（String版）のテスト - 50文字ちょうど（境界値）
   *
   * <p>タグ名文字列が50文字ちょうどの場合は正常に処理されることを検証
   */
  @Test
  public void testConstructorWithString_ExactlyMaxLength() {
    // 50文字のタグ名文字列を作成
    String tagNames = "あ".repeat(50);

    // 実行
    TagList tagList = new TagList(tagNames, userId, tagMapper);

    // 検証
    assertNotNull(tagList);
  }

  /**
   * コンストラクタ（List版）のテスト
   *
   * <p>タグリストからTagListが正しく作成されることを検証
   */
  @Test
  public void testConstructorWithList() {
    // 実行
    TagList tagList = new TagList(testTags, tagMapper);

    // 検証
    assertNotNull(tagList);
  }

  /**
   * タグリスト登録のテスト - 新規タグのみ
   *
   * <p>全て新規タグの場合、すべてのタグが登録されることを検証
   */
  @Test
  public void testInsertTagList_NewTags() throws MentalSystemException {
    // モックの設定: 全てのタグが存在しない
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong())).thenAnswer(invocation -> null);

    // 実行
    TagList tagList = new TagList("タグ1 タグ2 タグ3", userId, tagMapper);
    tagList.insertTagList();

    // 検証: insert()が3回呼ばれることを確認
    verify(tagMapper, times(3)).insert(org.mockito.ArgumentMatchers.any(Tag.class));
  }

  /**
   * タグリスト登録のテスト - 既存タグのみ
   *
   * <p>全て既存タグの場合、insert()が呼ばれないことを検証
   */
  @Test
  public void testInsertTagList_ExistingTags() throws MentalSystemException {
    // モックの設定: 全てのタグが既に存在
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong()))
        .thenAnswer(invocation -> new Tag());

    // 実行
    TagList tagList = new TagList("タグ1 タグ2 タグ3", userId, tagMapper);
    tagList.insertTagList();

    // 検証: insert()が呼ばれないことを確認
    verify(tagMapper, never()).insert(org.mockito.ArgumentMatchers.any(Tag.class));
  }

  /**
   * タグリスト登録のテスト - 新規と既存の混在
   *
   * <p>新規タグと既存タグが混在する場合、新規タグのみが登録されることを検証
   */
  @Test
  public void testInsertTagList_MixedTags() throws MentalSystemException {
    // モックの設定
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong()))
        .thenAnswer(
            invocation -> {
              String tagName = invocation.getArgument(0);
              if ("タグ1".equals(tagName)) return new Tag(); // 既存
              if ("タグ2".equals(tagName)) return null; // 新規
              if ("タグ3".equals(tagName)) return new Tag(); // 既存
              return null;
            });

    // 実行
    TagList tagList = new TagList("タグ1 タグ2 タグ3", userId, tagMapper);
    tagList.insertTagList();

    // 検証: insert()が1回だけ呼ばれることを確認（タグ2のみ）
    verify(tagMapper, times(1)).insert(org.mockito.ArgumentMatchers.any(Tag.class));
  }

  /**
   * タグ中間テーブル登録のテスト
   *
   * <p>タグIDリストが正しく中間テーブルに登録されることを検証
   */
  @Test
  public void testInsertMonitoringTagRelation() throws MentalSystemException {
    Long monitoringId = 10L;

    // モックの設定: タグリスト取得時にテストデータを返す
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong()))
        .thenAnswer(
            invocation -> {
              String tagName = invocation.getArgument(0);
              Long uid = invocation.getArgument(1);
              if ("タグ1".equals(tagName)) return createTagWithId(1L, "タグ1");
              if ("タグ2".equals(tagName)) return createTagWithId(2L, "タグ2");
              if ("タグ3".equals(tagName)) return createTagWithId(3L, "タグ3");
              return null;
            });

    // 実行
    TagList tagList = new TagList("タグ1 タグ2 タグ3", userId, tagMapper);
    tagList.insertMonitoringTagRelation(tagRelationMapper, monitoringId);

    // 検証: ArgumentCaptorで引数をキャプチャ
    ArgumentCaptor<Long> monitoringIdCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> tagIdCaptor = ArgumentCaptor.forClass(Long.class);

    verify(tagRelationMapper, times(3)).insert(monitoringIdCaptor.capture(), tagIdCaptor.capture());

    // 全ての呼び出しでmonitoringIdが正しいことを確認
    List<Long> capturedMonitoringIds = monitoringIdCaptor.getAllValues();
    assertTrue(capturedMonitoringIds.stream().allMatch(id -> id.equals(monitoringId)));

    // タグIDが1, 2, 3であることを確認
    List<Long> capturedTagIds = tagIdCaptor.getAllValues();
    assertEquals(3, capturedTagIds.size());
    assertTrue(capturedTagIds.contains(1L));
    assertTrue(capturedTagIds.contains(2L));
    assertTrue(capturedTagIds.contains(3L));
  }

  /**
   * タグリスト取得のテスト
   *
   * <p>データベースからタグリストが正しく取得されることを検証
   */
  @Test
  public void testSelectTagList() throws MentalSystemException {
    // モックの設定
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong()))
        .thenAnswer(
            invocation -> {
              String tagName = invocation.getArgument(0);
              Long uid = invocation.getArgument(1);
              if ("タグ1".equals(tagName)) return createTagWithId(1L, "タグ1");
              if ("タグ2".equals(tagName)) return createTagWithId(2L, "タグ2");
              if ("タグ3".equals(tagName)) return createTagWithId(3L, "タグ3");
              return null;
            });

    // 実行
    TagList tagList = new TagList("タグ1 タグ2 タグ3", userId, tagMapper);
    List<Tag> result = tagList.selectTagList();

    // 検証
    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals(1L, result.get(0).getId());
    assertEquals(2L, result.get(1).getId());
    assertEquals(3L, result.get(2).getId());
  }

  /**
   * タグリスト取得のテスト - null除外
   *
   * <p>取得結果にnullが含まれる場合、それが除外されることを検証
   */
  @Test
  public void testSelectTagList_WithNull() throws MentalSystemException {
    // モックの設定: タグ2がnull（存在しない）
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong()))
        .thenAnswer(
            invocation -> {
              String tagName = invocation.getArgument(0);
              Long uid = invocation.getArgument(1);
              if ("タグ1".equals(tagName)) return createTagWithId(1L, "タグ1");
              if ("タグ2".equals(tagName)) return null;
              if ("タグ3".equals(tagName)) return createTagWithId(3L, "タグ3");
              return null;
            });

    // 実行
    TagList tagList = new TagList("タグ1 タグ2 タグ3", userId, tagMapper);
    List<Tag> result = tagList.selectTagList();

    // 検証: nullが除外され、2件のみ返されることを確認
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(1L, result.get(0).getId());
    assertEquals(3L, result.get(1).getId());
  }

  /**
   * タグ名文字列変換のテスト
   *
   * <p>タグリストがスペース区切りの文字列に正しく変換されることを検証
   */
  @Test
  public void testTagNamesToString() throws MentalSystemException {
    // モックの設定
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong()))
        .thenAnswer(
            invocation -> {
              String tagName = invocation.getArgument(0);
              Long uid = invocation.getArgument(1);
              if ("タグ1".equals(tagName)) return createTagWithId(1L, "タグ1");
              if ("タグ2".equals(tagName)) return createTagWithId(2L, "タグ2");
              if ("タグ3".equals(tagName)) return createTagWithId(3L, "タグ3");
              return null;
            });

    // 実行
    TagList tagList = new TagList("タグ1 タグ2 タグ3", userId, tagMapper);
    String result = tagList.tagNamesToString();

    // 検証
    assertEquals("タグ1 タグ2 タグ3", result);
  }

  /**
   * タグ名文字列変換のテスト - 空のタグリスト
   *
   * <p>空のタグリストの場合、空文字列が返されることを検証
   */
  @Test
  public void testTagNamesToString_EmptyList() throws MentalSystemException {
    // モックの設定: 全てのタグが存在しない
    when(tagMapper.findByTagNameAndUserId(anyString(), anyLong())).thenReturn(null);

    // 実行
    TagList tagList = new TagList("タグ1 タグ2 タグ3", userId, tagMapper);
    String result = tagList.tagNamesToString();

    // 検証
    assertEquals("", result);
  }

  /**
   * ヘルパーメソッド: IDとタグ名を持つTagエンティティを作成
   *
   * @param id タグID
   * @param tagName タグ名
   * @return Tagエンティティ
   */
  private Tag createTagWithId(Long id, String tagName) {
    Tag tag = new Tag();
    tag.setId(id);
    tag.setTagName(tagName);
    tag.setUserId(userId);
    return tag;
  }
}
