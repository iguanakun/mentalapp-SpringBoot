FactoryBot.define do
  factory :monitoring do
    fact {Faker::Lorem.sentence}
    mind {Faker::Lorem.sentence}
    why_correct {Faker::Lorem.sentence}
    why_doubt {Faker::Lorem.sentence}
    new_thought {Faker::Lorem.sentence}
    association :user
  end
end
