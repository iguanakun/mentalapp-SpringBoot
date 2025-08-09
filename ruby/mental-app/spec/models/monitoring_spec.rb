require 'rails_helper'

RSpec.describe Monitoring, type: :model do
  describe 'モニタリング新規投稿' do
    let(:monitoring_form) { 
      user = FactoryBot.create(:user)
      FactoryBot.build(:monitoring_form, user_id: user.id)
    }

    context '新規投稿できる場合' do
      it '全ての投稿情報が存在すれば登録できる' do
        expect(monitoring_form).to be_valid
      end
      it 'factが空でも登録できる' do
        monitoring_form.fact = ''
        expect(monitoring_form).to be_valid
      end
      it 'mindが空でも登録できる' do
        monitoring_form.mind = ''
        expect(monitoring_form).to be_valid
      end
      it 'feelが空でも登録できる' do
        monitoring_form.feel = ''
        expect(monitoring_form).to be_valid
      end
      it 'bodyが空でも登録できる' do
        monitoring_form.body = ''
        expect(monitoring_form).to be_valid
      end
      it 'behaviorが空でも登録できる' do
        monitoring_form.behavior = ''
        expect(monitoring_form).to be_valid
      end
      it 'tagが空でも登録できる' do
        monitoring_form.tag_name = ''
        expect(monitoring_form).to be_valid
      end
    end
    context '新規投稿できない場合' do
      it '全ての情報が空では登録できない' do
        monitoring_form.fact = ''
        monitoring_form.mind = ''
        monitoring_form.feel = ''
        monitoring_form.body = ''
        monitoring_form.behavior = ''
        monitoring_form.tag_name = ''
        monitoring_form.valid?
        expect(monitoring_form.errors.full_messages).to include('Data is invalid') 
      end
      it 'タグ以外が空では登録できない' do
        monitoring_form.fact = ''
        monitoring_form.mind = ''
        monitoring_form.feel = ''
        monitoring_form.body = ''
        monitoring_form.behavior = ''
        monitoring_form.valid?
        expect(monitoring_form.errors.full_messages).to include('Data is invalid') 
      end
      it 'userが紐づいていなければ登録できない' do
        monitoring_form.user_id = nil
        monitoring_form.valid?
        expect(monitoring_form.errors.full_messages).to include("User can't be blank") 
      end
    end
  end
end
