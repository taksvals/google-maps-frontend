(ns year-paper-frontend.store.db
  (:require [cljs.reader]))

(def default-db {;;Data
                 :current-route nil
                 :current-user  nil
                 :loading       {:app false}
                 :errors        {}
                 :map           {:markers []}})