# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# This file is the source Rails uses to define your schema when running `bin/rails
# db:schema:load`. When creating a new database, `bin/rails db:schema:load` tends to
# be faster and is potentially less error prone than running all of your
# migrations from scratch. Old migrations may fail to apply correctly if those
# migrations use external dependencies or application code.
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema[7.0].define(version: 2023_11_05_071016) do
  create_table "cbt_basic_negative_feels", charset: "utf8", force: :cascade do |t|
    t.bigint "cbt_basic_id"
    t.bigint "negative_feel_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["cbt_basic_id"], name: "index_cbt_basic_negative_feels_on_cbt_basic_id"
    t.index ["negative_feel_id"], name: "index_cbt_basic_negative_feels_on_negative_feel_id"
  end

  create_table "cbt_basic_positive_feels", charset: "utf8", force: :cascade do |t|
    t.bigint "cbt_basic_id"
    t.bigint "positive_feel_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["cbt_basic_id"], name: "index_cbt_basic_positive_feels_on_cbt_basic_id"
    t.index ["positive_feel_id"], name: "index_cbt_basic_positive_feels_on_positive_feel_id"
  end

  create_table "cbt_basic_tag_relations", charset: "utf8", force: :cascade do |t|
    t.bigint "cbt_basic_id"
    t.bigint "tag_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["cbt_basic_id"], name: "index_cbt_basic_tag_relations_on_cbt_basic_id"
    t.index ["tag_id"], name: "index_cbt_basic_tag_relations_on_tag_id"
  end

  create_table "cbt_basics", charset: "utf8", force: :cascade do |t|
    t.text "fact"
    t.text "mind"
    t.text "body"
    t.text "behavior"
    t.bigint "user_id", null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["user_id"], name: "index_cbt_basics_on_user_id"
  end

  create_table "distortion_lists", charset: "utf8", force: :cascade do |t|
    t.string "distortion_name", null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.string "info"
  end

  create_table "monitoring_distortion_relations", charset: "utf8", force: :cascade do |t|
    t.bigint "monitoring_id"
    t.bigint "distortion_list_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["distortion_list_id"], name: "index_monitoring_distortion_relations_on_distortion_list_id"
    t.index ["monitoring_id"], name: "index_monitoring_distortion_relations_on_monitoring_id"
  end

  create_table "monitoring_negative_feels", charset: "utf8", force: :cascade do |t|
    t.bigint "monitoring_id"
    t.bigint "negative_feel_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["monitoring_id"], name: "index_monitoring_negative_feels_on_monitoring_id"
    t.index ["negative_feel_id"], name: "index_monitoring_negative_feels_on_negative_feel_id"
  end

  create_table "monitoring_positive_feels", charset: "utf8", force: :cascade do |t|
    t.bigint "monitoring_id"
    t.bigint "positive_feel_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["monitoring_id"], name: "index_monitoring_positive_feels_on_monitoring_id"
    t.index ["positive_feel_id"], name: "index_monitoring_positive_feels_on_positive_feel_id"
  end

  create_table "monitoring_tag_relations", charset: "utf8", force: :cascade do |t|
    t.bigint "monitoring_id"
    t.bigint "tag_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["monitoring_id"], name: "index_monitoring_tag_relations_on_monitoring_id"
    t.index ["tag_id"], name: "index_monitoring_tag_relations_on_tag_id"
  end

  create_table "monitorings", charset: "utf8", force: :cascade do |t|
    t.bigint "user_id", null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.text "fact"
    t.text "mind"
    t.text "why_correct"
    t.text "why_doubt"
    t.text "new_thought"
    t.index ["user_id"], name: "index_monitorings_on_user_id"
  end

  create_table "negative_feels", charset: "utf8", force: :cascade do |t|
    t.string "negative_feel_name", null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "positive_feels", charset: "utf8", force: :cascade do |t|
    t.string "positive_feel_name", null: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "tags", charset: "utf8", force: :cascade do |t|
    t.string "tag_name", null: false
    t.bigint "user_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["user_id"], name: "index_tags_on_user_id"
  end

  create_table "users", charset: "utf8", force: :cascade do |t|
    t.string "email", default: "", null: false
    t.string "encrypted_password", default: "", null: false
    t.string "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["email"], name: "index_users_on_email", unique: true
    t.index ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true
  end

  add_foreign_key "cbt_basic_negative_feels", "cbt_basics"
  add_foreign_key "cbt_basic_negative_feels", "negative_feels"
  add_foreign_key "cbt_basic_positive_feels", "cbt_basics"
  add_foreign_key "cbt_basic_positive_feels", "positive_feels"
  add_foreign_key "cbt_basic_tag_relations", "cbt_basics"
  add_foreign_key "cbt_basic_tag_relations", "tags"
  add_foreign_key "cbt_basics", "users"
  add_foreign_key "monitoring_distortion_relations", "distortion_lists"
  add_foreign_key "monitoring_distortion_relations", "monitorings"
  add_foreign_key "monitoring_negative_feels", "monitorings"
  add_foreign_key "monitoring_negative_feels", "negative_feels"
  add_foreign_key "monitoring_positive_feels", "monitorings"
  add_foreign_key "monitoring_positive_feels", "positive_feels"
  add_foreign_key "monitoring_tag_relations", "monitorings"
  add_foreign_key "monitoring_tag_relations", "tags"
  add_foreign_key "monitorings", "users"
  add_foreign_key "tags", "users"
end
