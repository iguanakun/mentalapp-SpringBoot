FactoryBot.define do
  factory :temptation_form do
    event {Faker::Lorem.sentence}
    talk {Faker::Lorem.sentence}
    cost {Faker::Lorem.sentence}
    get_out {Faker::Lorem.sentence}
    tag_name {Faker::Lorem.sentence}
  end
end
