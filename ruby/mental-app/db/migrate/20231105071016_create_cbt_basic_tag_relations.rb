class CreateCbtBasicTagRelations < ActiveRecord::Migration[7.0]
  def change
    create_table :cbt_basic_tag_relations do |t|
      t.references :cbt_basic, foreign_key: true
      t.references :tag, foreign_key: true
      t.timestamps
    end
  end
end
