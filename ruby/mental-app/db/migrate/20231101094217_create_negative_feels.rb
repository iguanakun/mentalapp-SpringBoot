class CreateNegativeFeels < ActiveRecord::Migration[7.0]
  def change
    create_table :negative_feels do |t|
      t.string :negative_feel_name, null:false, uniqueness: true
      t.timestamps
    end
  end
end
