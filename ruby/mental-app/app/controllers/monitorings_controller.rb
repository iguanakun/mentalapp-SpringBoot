class MonitoringsController < ApplicationController
  before_action :authenticate_user!, except: [:index]
  before_action :move_to_index, only: [:show, :edit, :update, :destroy, :edit_step2]
  before_action :set_monitoring, only: [:show, :edit, :update, :edit_step2]
  before_action :set_step2, only: [:step2, :edit_step2]

  def index
  end

  def new
    @monitoring_form = MonitoringForm.new
  end

  def create
    @monitoring_form = MonitoringForm.new(monitoring_form_params)
    @monitoring_form.negative_feel_name = session[:negative_feel_name]
    @monitoring_form.positive_feel_name = session[:positive_feel_name]
    @monitoring_form.fact = session[:fact]
    @monitoring_form.mind = session[:mind]

    session.delete(:negative_feel_name)
    session.delete(:positive_feel_name)
    session.delete(:fact)
    session.delete(:mind)
    
    if @monitoring_form.valid?
      @monitoring_form.save
      redirect_to memos_path
    else
      render :new, status: :unprocessable_entity
    end
  end

  def show
  end

  def edit
    # @monitoringから情報をハッシュとして取り出し、@monitoring_formとしてインスタンス生成する
    monitoring_attributes = @monitoring.attributes
    @monitoring_form = MonitoringForm.new(monitoring_attributes)
    @monitoring_form.negative_feel_name = @monitoring&.negative_feels
    @monitoring_form.positive_feel_name = @monitoring&.positive_feels
  end

  def update
    @monitoring_form = MonitoringForm.new(monitoring_form_params)
    session_params ={ 
      negative_feel_name: session[:negative_feel_name],
      positive_feel_name: session[:positive_feel_name],
      fact: session[:fact],
      mind: session[:mind]
    }
    @monitoring_form.negative_feel_name = session[:negative_feel_name]
    @monitoring_form.positive_feel_name = session[:positive_feel_name]
    @monitoring_form.fact = session[:fact]
    @monitoring_form.mind = session[:mind]

    session.delete(:negative_feel_name)
    session.delete(:positive_feel_name)
    session.delete(:fact)
    session.delete(:mind)

    if @monitoring_form.valid?
      @monitoring_form.update(monitoring_form_params, @monitoring, session_params)
      redirect_to memos_path
    else
      render :edit, status: :unprocessable_entity
    end
  end

  def destroy
    monitoring = Monitoring.find(params[:id])
    monitoring.destroy
    redirect_to memos_path
  end

  def lists
    # 上位３つ表示機能のため、メソッドを実行
    @negative_feels = MonitoringNegativeFeel.negative_max_count(current_user)
    @distortions = MonitoringDistortionRelation.distortion_max_count(current_user)

    ### タグ検索 ###
    # params[:q]がnilではない且つ、params[:q][:name]がnilではないとき（商品名の欄が入力されているとき）
    # if params[:q] && params[:q][:name]と同じような意味合い
    if params[:q]&.dig(:tags_tag_name)
      # squishメソッドで余分なスペースを削除する
      squished_keywords = params[:q][:tags_tag_name].squish
      ## 半角スペースを区切り文字として配列を生成し、paramsに入れる
      params[:q][:tags_tag_name_cont_any] = squished_keywords.split(" ")
    end
    data = current_user.monitorings.order("created_at DESC")
    @q = data.ransack(params[:q])
    @monitorings = @q.result(distinct: true)
  end
  
  def step2
    @monitoring_form = MonitoringForm.new
  end

  def edit_step2
    monitoring_attributes = @monitoring.attributes
    @monitoring_form = MonitoringForm.new(monitoring_attributes)
    @monitoring_form.distortion_name = @monitoring&.distortion_lists

    if @monitoring.tags.present?
      tags = []
      @monitoring.tags.each do |tag|
        tags << tag.tag_name
      end
      @monitoring_form.tag_name = tags.join(' ')
    end
  end


  private

  def monitoring_form_params
    params.require(:monitoring_form).permit(:fact, :mind, :why_correct, :why_doubt, :new_thought, :tag_name, negative_feel_name:[], 
                                            positive_feel_name:[], distortion_name:[]).merge(user_id: current_user.id)
  end

  def move_to_index
    monitoring = Monitoring.find(params[:id])
    if current_user.id != monitoring.user.id
      redirect_to root_path
    end
  end

  def set_monitoring
    @monitoring = Monitoring.find(params[:id])
  end

  def set_step2
    session[:negative_feel_name] = monitoring_form_params[:negative_feel_name]
    session[:positive_feel_name] = monitoring_form_params[:positive_feel_name]
    session[:fact] = monitoring_form_params[:fact]
    session[:mind] = monitoring_form_params[:mind]
  end
end
