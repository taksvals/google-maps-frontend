(ns year-paper-frontend.screens.auth.register
  (:require
   [reagent.core  :as r]
   [re-frame.core :as rf]))

(defn register []
  (let [username         (r/atom "")
        email            (r/atom "")
        password         (r/atom "")
        confirm-password (r/atom "")]
    (fn []
      [:div.auth
       [:form {:on-submit (fn [e]
                            (.preventDefault e)
                            (when (= @password
                                     @confirm-password)
                              (rf/dispatch [:register-user {:username @username
                                                            :email    @email
                                                            :password @password}])))}
        [:h3 "Create an account!"]
        [:label
         {:for "username"} "Username"]
        [:input#username
         {:type          "text"
          :default-value @username
          :placeholder   "Username"
          :on-change     (fn [event]
                           (reset! username (-> event .-target .-value)))}]
        [:label
         {:for "email"} "Email"]
        [:input#email
         {:type          "email"
          :default-value @email
          :placeholder   "Email"
          :on-change     (fn [event]
                           (reset! email (-> event .-target .-value)))}]
        [:label
         {:for "password"} "Password"]
        [:input#password
         {:type          "password"
          :default-value @password
          :placeholder   "Password"
          :on-change     (fn [event]
                           (reset! password (-> event .-target .-value)))}]
        [:label
         {:for "password"} "Confirm Password"]
        [:input#password
         {:type          "password"
          :default-value @confirm-password
          :placeholder   "Password"
          :on-change     (fn [event]
                           (reset! confirm-password (-> event .-target .-value)))}]
        [:button {:type "submit"}
         "Create an Account"]
        [:span "Already have an account? "
         [:a {:on-click #(rf/dispatch [:navigate :auth/sign-in])} "Sign In"]]]])))