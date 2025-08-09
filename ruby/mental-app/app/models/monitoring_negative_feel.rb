class MonitoringNegativeFeel < ApplicationRecord
  belongs_to :monitoring
  belongs_to :negative_feel

  def self.negative_max_count(user_id)
    # 特定のユーザーに関連づけられたmonitoring_idの最大値を取得
    max_monitoring_ids = Monitoring.where(user_id: user_id).pluck(:id)

    # 特定のユーザーに関連づけられたmonitoring_idと一致するレコードをフィルタリング
    relevant_records = where(monitoring_id: max_monitoring_ids)

    # negative_feel_idごとの出現回数をカウントして、降順でソート
    negative_id_counts = relevant_records.group(:negative_feel_id).count.sort_by { |_, v| -v }.to_h

    # 上位3つを取得
    top_three = negative_id_counts.to_a[0, 3].to_h


    rtn_record = []
    top_three.keys.each do |key|
      record = NegativeFeel.find_by(id: key)
      rtn_record << { negative_feel_name: record.negative_feel_name, count: top_three[key] }
    end

    return rtn_record
  end
end
