class CreateMonitorings < ActiveRecord::Migration[7.0]
  def change
    create_table :monitorings do |t|
      t.text :fact
      t.text :mind
      t.text :feel
      t.text :body
      t.text :behavior
      t.references :user, null:false, foreign_key:true
      t.timestamps
    end
  end
end
