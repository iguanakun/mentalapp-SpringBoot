class PositiveFeel < ApplicationRecord
  has_many :monitoring_positive_feels, dependent: :destroy
  has_many :monitorings, through: :monitoring_positive_feels
  has_many :cbt_basic_positive_feels, dependent: :destroy
  has_many :cbt_basics, through: :cbt_basic_positive_feels
  validates :positive_feel_name,  uniqueness: true
end
