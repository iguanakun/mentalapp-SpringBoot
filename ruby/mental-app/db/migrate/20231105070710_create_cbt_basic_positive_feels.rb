class CreateCbtBasicPositiveFeels < ActiveRecord::Migration[7.0]
  def change
    create_table :cbt_basic_positive_feels do |t|
      t.references :cbt_basic, foreign_key: true
      t.references :positive_feel, foreign_key: true
      t.timestamps
    end
  end
end
