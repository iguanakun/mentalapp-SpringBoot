class Monitoring < ApplicationRecord
  has_many :monitoring_tag_relations, dependent: :destroy
  has_many :tags, through: :monitoring_tag_relations
  has_many :monitoring_negative_feels, dependent: :destroy
  has_many :negative_feels, through: :monitoring_negative_feels
  has_many :monitoring_positive_feels, dependent: :destroy
  has_many :positive_feels, through: :monitoring_positive_feels
  has_many :monitoring_distortion_relations, dependent: :destroy
  has_many :distortion_lists, through: :monitoring_distortion_relations
  belongs_to :user

  encrypts :fact, :mind, :why_correct, :why_doubt, :new_thought
  
  def self.ransackable_attributes(auth_object = nil)
    ["fact"]
  end

  def self.ransackable_associations(auth_object = nil)
    ["tags"]
  end
end
