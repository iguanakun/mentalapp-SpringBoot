class CbtBasicsController < ApplicationController
  before_action :authenticate_user!
  before_action :move_to_index, only: [:show, :edit, :update, :destroy]
  before_action :set_cbt_basic, only: [:show, :edit, :update]

  def new
    @cbt_basic_form = CbtBasicForm.new
  end

  def create
    @cbt_basic_form = CbtBasicForm.new(cbt_basic_form_params)
    
    if @cbt_basic_form.valid?
      @cbt_basic_form.save
      redirect_to memos_path
    else
      render :new, status: :unprocessable_entity
    end
  end

  def show
  end

  def edit
    # @cbt_basicから情報をハッシュとして取り出し、@cbt_basic_formとしてインスタンス生成する
    cbt_basic_attributes = @cbt_basic.attributes
    @cbt_basic_form = CbtBasicForm.new(cbt_basic_attributes)
    @cbt_basic_form.negative_feel_name = @cbt_basic&.negative_feels
    @cbt_basic_form.positive_feel_name = @cbt_basic&.positive_feels
  end

  def update
    @cbt_basic_form = CbtBasicForm.new(cbt_basic_form_params)

    if @cbt_basic_form.valid?
      @cbt_basic_form.update(cbt_basic_form_params, @cbt_basic)
      redirect_to memos_path
    else
      render :edit, status: :unprocessable_entity
    end
  end

  def destroy
    cbt_basic = CbtBasic.find(params[:id])
    cbt_basic.destroy
    redirect_to memos_path
  end

  def lists
    # 上位３つ表示機能のため、メソッドを実行
    @negative_feels = CbtBasicNegativeFeel.negative_max_count(current_user)

    ### タグ検索 ###
    # params[:q]がnilではない且つ、params[:q][:name]がnilではないとき（商品名の欄が入力されているとき）
    # if params[:q] && params[:q][:name]と同じような意味合い
    if params[:q]&.dig(:tags_tag_name)
      # squishメソッドで余分なスペースを削除する
      squished_keywords = params[:q][:tags_tag_name].squish
      ## 半角スペースを区切り文字として配列を生成し、paramsに入れる
      params[:q][:tags_tag_name_cont_any] = squished_keywords.split(" ")
    end
    data = current_user.cbt_basics.order("created_at DESC")
    @q = data.ransack(params[:q])
    @cbt_basics = @q.result(distinct: true)
  end

  private

  def cbt_basic_form_params
    params.require(:cbt_basic_form).permit(:fact, :mind, :body, :behavior, :tag_name, negative_feel_name:[], 
                                            positive_feel_name:[]).merge(user_id: current_user.id)
  end

  def move_to_index
    cbt_basic = CbtBasic.find(params[:id])
    if current_user.id != cbt_basic.user.id
      redirect_to root_path
    end
  end

  def set_cbt_basic
    @cbt_basic = CbtBasic.find(params[:id])
  end
end
