class DistortionList < ApplicationRecord
  has_many :monitoring_distortion_relations, dependent: :destroy
  has_many :monitorings, through: :monitoring_distortion_relations

  validates :distortion_name,  uniqueness: true
end
