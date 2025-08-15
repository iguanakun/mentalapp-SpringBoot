Rails.application.routes.draw do
  devise_for :users
  
  root to:"monitorings#index"
  resources :monitorings do
    collection do
      get 'lists'
      get 'step2'
    end
    member do
      get 'edit_step2'      
    end
  end

  resources :cbt_basics, except: [:index] do
    collection do
      get 'lists'
    end
  end

  resources :work_care, only: [:index] do
    collection do
      get 'content1'
      get 'content1/page1', to: 'work_care#page1'
      get 'content1/page2', to: 'work_care#page2'
      get 'content1/page3', to: 'work_care#page3'
      get 'content1/page4', to: 'work_care#page4'
    end
  end

  resources :memos, only: [:index] 
end
