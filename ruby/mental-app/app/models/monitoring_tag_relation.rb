class MonitoringTagRelation < ApplicationRecord
  belongs_to :monitoring
  belongs_to :tag
end
