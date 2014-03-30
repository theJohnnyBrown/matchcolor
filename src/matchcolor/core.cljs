(ns matchcolor.core
  (:require [matchcolor.server :as server]
            [matchcolor.browser :as browser]
            [secretary.core :as secretary]))

(def browser? (exists? js/document))

(if browser?
  (let [{:keys [template active]}
        (secretary/dispatch! (-> js/window .-location .-pathname))]
    (.log js/console (str "setup template " template))
    ;; (browser/setup-app template active)
    (js* "debugger;;"))
  (set! *main-cli-fn* server/-main))
