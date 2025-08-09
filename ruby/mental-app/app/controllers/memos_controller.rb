class MemosController < ApplicationController
  before_action :authenticate_user!

  def index
    @cbt_basics = current_user.cbt_basics.select('*, \'cbt_basic\' AS data_type')
    @monitorings = current_user.monitorings.select('*, \'monitoring\' AS data_type')
    @lists = (@cbt_basics + @monitorings).sort_by(&:created_at).reverse

    # 上位３つ表示機能のため、メソッドを実行
    @negative_feels = MaxNegativeService.negative_max_count(current_user)
    @distortions = MonitoringDistortionRelation.distortion_max_count(current_user)
  end
end
