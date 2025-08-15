class MaxNegativeService
  def self.negative_max_count(user_id)
    # 特定のユーザーが投稿したメモのIDのみ取得
    cbt_basic_ids = CbtBasic.where(user_id: user_id).pluck(:id)

    # 中間テーブルから、cbt_basic_idsと一致するレコードをフィルタリング
    target_records_basic = CbtBasicNegativeFeel.where(cbt_basic_id: cbt_basic_ids)

    # 特定のユーザーが投稿したメモのIDのみ取得
    monitoring_ids = Monitoring.where(user_id: user_id).pluck(:id)

    # 中間テーブルから、monitoring_idsと一致するレコードをフィルタリング
    target_records_monitoring = MonitoringNegativeFeel.where(monitoring_id: monitoring_ids)

    # negative_feel_idごとの出現回数をカウント
    negative_ids_basic = target_records_basic.group(:negative_feel_id).count
    negative_ids_monitoring = target_records_monitoring.group(:negative_feel_id).count

    # 両者をマージ。両者の感情が一致する場合、カウント数を加算。
    merge_counts = negative_ids_basic.merge(negative_ids_monitoring) { |_, basic_count, monitoring_count| basic_count + monitoring_count }

    # 降順でソート
    negative_id_counts = merge_counts.sort_by { |_, v| -v }.to_h

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