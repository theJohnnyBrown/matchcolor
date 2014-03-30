(ns matchcolor.core
  (:require [cljs.nodejs :as nodejs]

            [matchcolor.views :as views]
            [matchcolor.routes :as routes]
            [secretary.core :as secretary]))

; Node.js dirname
(def __dirname (js* "__dirname"))

(def express (nodejs/require "express"))
(def logfmt (nodejs/require "logfmt"))

(def app (express))

(def port (or (aget nodejs/process "env" "PORT") 3000))

; Logger
(.use app (.requestLogger logfmt))

; Body parser
(.use app (.urlencoded express))
(.use app (.json express))


; Set assets folder
(.use app (.static express (str __dirname "/../assets")))

; Redirect form to to /MYHEXCOLOR
(.post app "/" #(.redirect %2 (str "/" (aget %1 "body" "color"))))

(.get app "*"
      (fn [req resp next]
        (let [{:keys [template active] :as view}
              (secretary/dispatch! (.-url req))]
          (if view (.send resp (views/layout-render template active)) (next)))))

(.get app "*" #(.send %2 (views/layout-render views/four-oh-four "404") 404))

(defn -main [& args]
  (.listen app port))

(set! *main-cli-fn* -main)
