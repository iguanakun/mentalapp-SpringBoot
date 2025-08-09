class User < ApplicationRecord
  validates :password, format: { with: /\A(?=.*?[a-z])(?=.*?\d)[a-z\d]+\z/i, message: 'は半角英数字で入力してください。' }

  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :validatable

  has_many :monitorings
  has_many :cbt_basics
  has_many :tags
end
