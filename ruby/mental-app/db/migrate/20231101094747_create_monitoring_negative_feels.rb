class CreateMonitoringNegativeFeels < ActiveRecord::Migration[7.0]
  def change
    create_table :monitoring_negative_feels do |t|
      t.references :monitoring, foreign_key: true
      t.references :negative_feel, foreign_key: true
      t.timestamps
    end
  end
end
