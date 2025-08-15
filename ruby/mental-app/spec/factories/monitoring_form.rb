FactoryBot.define do
  factory :monitoring_form do
    fact {Faker::Lorem.sentence}
    mind {Faker::Lorem.sentence}
    why_correct {Faker::Lorem.sentence}
    why_doubt {Faker::Lorem.sentence}
    new_thought {Faker::Lorem.sentence}
    tag_name {Faker::Lorem.sentence}
  end
end
