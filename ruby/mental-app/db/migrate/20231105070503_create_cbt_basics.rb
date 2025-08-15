class CreateCbtBasics < ActiveRecord::Migration[7.0]
  def change
    create_table :cbt_basics do |t|
      t.text :fact
      t.text :mind
      t.text :body
      t.text :behavior
      t.references :user, null:false, foreign_key:true
      t.timestamps
    end
  end
end
