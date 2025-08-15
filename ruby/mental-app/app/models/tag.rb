class Tag < ApplicationRecord
  has_many :monitoring_tag_relations, dependent: :destroy
  has_many :monitorings, through: :monitoring_tag_relations
  has_many :cbt_basic_tag_relations, dependent: :destroy
  has_many :cbt_basics, through: :cbt_basic_tag_relations
  belongs_to :user

  validates :tag_name,  uniqueness: true

  def self.ransackable_attributes(auth_object = nil)
    ["tag_name"]
  end
end
