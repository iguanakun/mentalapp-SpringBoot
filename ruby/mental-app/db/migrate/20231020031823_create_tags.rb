class CreateTags < ActiveRecord::Migration[7.0]
  def change
    create_table :tags do |t|
      t.string :tag_name, null:false, uniqueness: true
      t.references :user, foreign_key: true
      t.timestamps
    end
  end
end
