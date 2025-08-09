FactoryBot.define do
  factory :tag do
    tag_name {Faker::Lorem.sentence}
    association :user
  end
end
