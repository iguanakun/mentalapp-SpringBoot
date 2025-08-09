# Pin npm packages by running ./bin/importmap

pin "application", preload: true
pin "@hotwired/turbo-rails", to: "turbo.min.js", preload: true
pin "@hotwired/stimulus", to: "stimulus.min.js", preload: true
pin "@hotwired/stimulus-loading", to: "stimulus-loading.js", preload: true
pin_all_from "app/javascript/controllers", under: "controllers"
pin "navbar", to: "navbar.js"
pin "feel-checkbox", to: "feel-checkbox.js"
# pin "meatball", to: "meatball.js"
# pin "vue", to: "vue--dist--vue.esm-browser.js.js" # @3.3.4
# pin "dropdown", to: "dropdown.js"
