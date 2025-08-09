require 'rails_helper'

RSpec.describe User, type: :model do
    # @user = FactoryBot.build(:user)
    let(:user) { FactoryBot.build(:user) }

  describe 'ユーザー新規登録' do
    context '新規登録できる場合' do
      it '全てのユーザ登録情報が存在すれば登録できる' do
        expect(user).to be_valid
      end
    end
    context '新規登録できない場合' do
      it 'emailが空では登録できない' do
        user.email = ''
        user.valid?
        expect(user.errors.full_messages).to include("Email can't be blank")
      end
      it 'passwordが空では登録できない' do
        user.password = ''
        user.valid?
        expect(user.errors.full_messages).to include("Password can't be blank")
      end
      it 'emailが重複すると登録できない' do
        user.save
        another_user = FactoryBot.build(:user, email: user.email)
        another_user.valid?
        expect(another_user.errors.full_messages).to include('Email has already been taken')
      end
      it 'emailに@がなければ登録できない' do
        user.email = 'testmail'
        user.valid?
        expect(user.errors.full_messages).to include('Email is invalid')
      end
      it 'passwordが5文字以下では登録できない' do
        user.password = '1234a'
        user.password_confirmation = '1234a'
        user.valid?
        expect(user.errors.full_messages).to include('Password is too short (minimum is 6 characters)')
      end
      it 'passwordが129文字以上では登録できない' do
        user.password = Faker::Alphanumeric.alphanumeric(number: 129, min_alpha: 1, min_numeric: 1)
        user.password_confirmation = user.password
        user.valid?
        expect(user.errors.full_messages).to include('Password is too long (maximum is 128 characters)')
      end
      it 'passwordとpassword_confirmationが不一致では登録できない' do
        user.password = '12345a'
        user.password_confirmation = '12345b'
        user.valid?
        expect(user.errors.full_messages).to include("Password confirmation doesn't match Password")
      end
      it 'passwordが半角英字のみでは登録できない' do
        user.password = 'abcdef'
        user.password_confirmation = 'abcdef'
        user.valid?
        expect(user.errors.full_messages).to include('Password is invalid. Include both letters and numbers')
      end
      it 'passwordが半角数字のみでは登録できない' do
        user.password = '123456'
        user.password_confirmation = '123456'
        user.valid?
        expect(user.errors.full_messages).to include('Password is invalid. Include both letters and numbers')
      end
      it 'passwordが全角では登録できない' do
        user.password = '１２３４５ａ'
        user.password_confirmation = '１２３４５ａ'
        user.valid?
        expect(user.errors.full_messages).to include('Password is invalid. Include both letters and numbers')
      end
    end
  end
end