class MonitoringDistortionRelation < ApplicationRecord
  belongs_to :monitoring
  belongs_to :distortion_list

  def self.distortion_max_count(user_id)
    # 特定のユーザーに関連づけられたmonitoring_idの最大値を取得
    max_monitoring_ids = Monitoring.where(user_id: user_id).pluck(:id)

    # 特定のユーザーに関連づけられたmonitoring_idと一致するレコードをフィルタリング
    relevant_records = where(monitoring_id: max_monitoring_ids)

    # distortion_list_idごとの出現回数をカウントして、降順でソート
    dist_id_counts = relevant_records.group(:distortion_list_id).count.sort_by { |_, v| -v }.to_h

    # 上位3つを取得
    top_three = dist_id_counts.to_a[0, 3].to_h

    rtn_record = []
    top_three.keys.each do |key|
      record = DistortionList.find_by(id: key)
      rtn_record << { distortion_name: record.distortion_name, count: top_three[key] }
    end

    return rtn_record
  end
end
