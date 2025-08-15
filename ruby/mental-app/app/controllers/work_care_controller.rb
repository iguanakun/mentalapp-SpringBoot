class WorkCareController < ApplicationController
  before_action :authenticate_user!

  def index
    redirect_to  root_path
  end

  def content1
  end

  def page1
  end

  def page2
  end

  def page3
  end

  def page4
  end
end
