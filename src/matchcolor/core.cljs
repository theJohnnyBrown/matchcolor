(ns matchcolor.core
  (:require [matchcolor.server :as server]
            [matchcolor.browser :as browser]
            [secretary.core :as secretary]))

(def browser? (exists? js/document))

(if browser?
  (let [{:keys [template state]}
        (secretary/dispatch! (-> js/window .-location .-pathname))]
    (.log js/console (str "setup template " template))
    (browser/setup-app template state))
  (set! *main-cli-fn* server/-main))
