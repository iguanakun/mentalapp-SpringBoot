class RemoveDetailsFromMonitorings < ActiveRecord::Migration[7.0]
  def change
    remove_column :monitorings, :fact, :text
    remove_column :monitorings, :mind, :text
    remove_column :monitorings, :feel, :text
    remove_column :monitorings, :body, :text
    remove_column :monitorings, :behavior, :text
  end
end
