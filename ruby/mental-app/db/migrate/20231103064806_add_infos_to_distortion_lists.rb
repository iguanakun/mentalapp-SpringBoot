class AddInfosToDistortionLists < ActiveRecord::Migration[7.0]
  def change
    add_column :distortion_lists, :info, :string
  end
end
