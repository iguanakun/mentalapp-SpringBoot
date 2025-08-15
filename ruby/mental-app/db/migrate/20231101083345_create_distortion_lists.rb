class CreateDistortionLists < ActiveRecord::Migration[7.0]
  def change
    create_table :distortion_lists do |t|
      t.string :distortion_name, null:false, uniqueness: true
      t.timestamps
    end
  end
end
