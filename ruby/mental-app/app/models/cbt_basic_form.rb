class CbtBasicForm
  include ActiveModel::Model
  attr_accessor(
    :fact, :mind, :body, :behavior,
    :user_id,
    :id, :created_at, :updated_at,
    :negative_feel_name,
    :positive_feel_name,
    :tag_name
  )

  validates :user_id, presence: true
  validate :required_either_columns

  def save
    cbt_basic = CbtBasic.create(fact: fact, mind: mind, body: body, behavior: behavior, user_id: user_id)
    if tag_name.present?
      input_tags = tag_name.squish.split
      input_tags.each do |item|
        tag = Tag.where(tag_name: item).first_or_initialize
        if tag.id.nil?
          tag.user_id = user_id;
        end
        tag.save
        CbtBasicTagRelation.create(cbt_basic_id: cbt_basic.id, tag_id: tag.id)
      end
    end

    if negative_feel_name.present?
      negative_feel_name.each do |id|
        CbtBasicNegativeFeel.create(cbt_basic_id: cbt_basic.id, negative_feel_id: id)
      end
    end

    if positive_feel_name.present?
      positive_feel_name.each do |id|
        CbtBasicPositiveFeel.create(cbt_basic_id: cbt_basic.id, positive_feel_id: id)
      end
    end
  end

  def update(params, cbt_basic)
    #一度中間テーブルの紐付けを消す
    cbt_basic.cbt_basic_tag_relations.destroy_all
    cbt_basic.cbt_basic_negative_feels.destroy_all
    cbt_basic.cbt_basic_positive_feels.destroy_all

    #paramsの中の各情報を削除。同時に、返り値として各情報を変数に代入
    input_tags = params.delete(:tag_name).squish.split if params.delete(:tag_name).present?
    input_negative_feels = params.delete(:negative_feel_name)
    input_positive_feels = params.delete(:positive_feel_name)

    # 中間テーブル関連情報以外を更新
    cbt_basic.update(params)

    if input_tags.present?
      input_tags.each do |item|
        #もしタグの情報がすでに保存されていればインスタンスを取得、無ければインスタンスを新規作成
        tag = Tag.where(tag_name: item).first_or_initialize 
        tag.user_id = user_id if tag.id.nil?

        #タグを保存
        tag.save
        CbtBasicTagRelation.create(cbt_basic_id: cbt_basic.id, tag_id: tag.id)
      end
    end

    if input_negative_feels.present?
      input_negative_feels.each do |id|
        CbtBasicNegativeFeel.create(cbt_basic_id: cbt_basic.id, negative_feel_id: id)
      end
    end

    if input_positive_feels.present?
      input_positive_feels.each do |id|
        CbtBasicPositiveFeel.create(cbt_basic_id: cbt_basic.id, positive_feel_id: id)
      end
    end
  end

  private

  def required_either_columns
    return if ( negative_feel_name.present? || positive_feel_name.present? || fact.present? || mind.present? ||
                body.present? || behavior.present? )
    errors.add(:base, 'いずれかひとつの項目を入力してください。')
  end
end