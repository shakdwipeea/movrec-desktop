(ns ui.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [ui.ajax :refer [load-interceptors!]]
            [ui.handlers]
            [ui.subscriptions])
  (:import goog.History))

(enable-console-print!)

(defn search-bar []
  [:div.control.is-grouped.search
    [:p.control.is-expanded
      [:input.input {:placeholder "Search your wish!"}]]
    [:p.control
      [:a.button.is-info "Search"]]])

(defn root-component []
  [:div.container.is-fluid
    (search-bar)])

(def pages
  {:home #'root-component})

(defn page []
  [:div
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app-container")))

(defn init! []
  (println "op")
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))

(init!)
