class RemoveTitleFromMonitorings < ActiveRecord::Migration[7.0]
  def change
    remove_column :monitorings, :title, :string
  end
end
