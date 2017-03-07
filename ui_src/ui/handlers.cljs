(ns ui.handlers
  (:require [ui.db :as db]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]))

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(def tmdb-base-url "https://api.themoviedb.org/3")

(reg-event-fx
  :search-movie
  (fn [{:keys [db]} _]
    {:http-xhrio {
                  :method :get
                  :uri (str tmdb-base-url "/search/movie?api_key=4805f78ea7163d5fdd1295926e07b2ac&language=en-US&query=harry&page=1&include_adult=false")
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:process-response]
                  :on-failure [:bad-response]}
     :db (assoc db :loading? true)}))

(reg-event-db
  :process-response
  (fn [db [_ response]]
      (-> db
          (println response)
          (assoc :loading? false))))

(reg-event-db
  :bad-response
  (fn [db [_ response]]
    (.log js/console response)))


(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (println (str "Page " page))
    (assoc db :page page)))
