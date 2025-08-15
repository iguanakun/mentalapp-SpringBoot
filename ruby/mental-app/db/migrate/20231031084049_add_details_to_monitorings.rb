class AddDetailsToMonitorings < ActiveRecord::Migration[7.0]
  def change
    add_column :monitorings, :title, :string
    add_column :monitorings, :fact, :text
    add_column :monitorings, :mind, :text
    add_column :monitorings, :why_correct, :text
    add_column :monitorings, :why_doubt, :text
    add_column :monitorings, :new_thought, :text
  end
end
