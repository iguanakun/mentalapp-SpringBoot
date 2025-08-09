class CreateCbtBasicNegativeFeels < ActiveRecord::Migration[7.0]
  def change
    create_table :cbt_basic_negative_feels do |t|
      t.references :cbt_basic, foreign_key: true
      t.references :negative_feel, foreign_key: true
      t.timestamps
    end
  end
end
