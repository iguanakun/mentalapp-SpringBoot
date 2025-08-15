class CreateMonitoringDistortionRelations < ActiveRecord::Migration[7.0]
  def change
    create_table :monitoring_distortion_relations do |t|
      t.references :monitoring, foreign_key: true
      t.references :distortion_list, foreign_key: true
      t.timestamps
    end
  end
end
