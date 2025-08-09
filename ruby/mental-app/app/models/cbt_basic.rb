class CbtBasic < ApplicationRecord
  has_many :cbt_basic_tag_relations, dependent: :destroy
  has_many :tags, through: :cbt_basic_tag_relations
  has_many :cbt_basic_negative_feels, dependent: :destroy
  has_many :negative_feels, through: :cbt_basic_negative_feels
  has_many :cbt_basic_positive_feels, dependent: :destroy
  has_many :positive_feels, through: :cbt_basic_positive_feels
  belongs_to :user

  encrypts :fact, :mind, :body, :behavior
  
  def self.ransackable_attributes(auth_object = nil)
    ["fact"]
  end

  def self.ransackable_associations(auth_object = nil)
    ["tags"]
  end
end
