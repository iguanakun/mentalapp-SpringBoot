class CreatePositiveFeels < ActiveRecord::Migration[7.0]
  def change
    create_table :positive_feels do |t|
      t.string :positive_feel_name, null:false, uniqueness: true
      t.timestamps
    end
  end
end
